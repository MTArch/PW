/**
 * 
 */
package com.parserlabs.phr.security.captcha;

import static com.parserlabs.phr.constants.Constants.CAPTCHA_ANSWER_KEY;
import static com.parserlabs.phr.constants.Constants.CAPTCHA_TEXT_KEY;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.parserlabs.phr.cache.RedisCacheService;
import com.parserlabs.phr.enums.CaptchaStatus;
import com.parserlabs.phr.enums.CaptchaType;
import com.parserlabs.phr.exception.CaptchaCodeValidationException;
import com.parserlabs.phr.exception.CaptchaGenerationException;
import com.parserlabs.phr.exception.CaptchaSessionExpiredException;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.model.captcha.CaptchaAuthRequest;
import com.parserlabs.phr.model.captcha.CaptchaBuilderResponse;
import com.parserlabs.phr.model.captcha.CaptchaGenerateRequest;
import com.parserlabs.phr.model.captcha.CaptchaSecurityRedisData;
import com.parserlabs.phr.service.RedisRateLimiterService;
import com.parserlabs.phr.utils.CaptchaUtils;
import com.parserlabs.phr.utils.GenerateCaptchaText;
import com.parserlabs.phr.utils.PHRIdUtils;
import com.parserlabs.phr.utils.PhrDateUtility;
import com.parserlabs.phr.utils.PhrUtilits;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.text.producer.TextProducer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@Service
@Slf4j
public class CaptchaSecurityServiceImpl implements CaptchaSecurityService {

	private static final String CAPTCHA_SALT = "Cap_";

	@Value("${captcha.answer.attempt:5}")
	private String captchaAttempt;

	@Value("${auth.password.timeout:10800}")
	private long authTimeOutInSec;

	@Value("${captcha.text.length:6}")
	private int textCaptachaLength;

	@Value("${captcha.persecond.allowed:10}")
	private Integer captchaAllowed;

	@Value("${captcha.persecond.lock:10}")
	private Integer captchaLocked;

	@Autowired
	private RedisRateLimiterService limiterService;

	@Autowired
	private CaptchaGenerator captchaGenerator;

	@Autowired
	private CaptchaClient captchaClient;

	@Autowired
	private RedisCacheService redisCacheService;

	public CaptchaBuilderResponse generateCaptcha(String request) {
		String captchaPayload = DecryptRSAUtil.decrypt(CaptchaUtils.decodebase64(request));

		CaptchaGenerateRequest req = null;

		if (StringUtils.isNotBlank(captchaPayload)) {
			Gson gson = new Gson();
			req = gson.fromJson(captchaPayload, CaptchaGenerateRequest.class);
			if (Objects.nonNull(req)) {
				// Check Brute Force Attack
				if (StringUtils.isNotBlank(req.getHostIp())) {
					bruteForceCheck(req.getClientId(), req.getHostIp());
				}
				CaptchaGenerateRequest reqTrimData = CaptchaGenerateRequest.builder().clientId(req.getClientId().trim())
						.clientSecert(req.getClientSecert().trim()).type(req.getType().trim())
						.authTs(req.getAuthTs().trim()).hostIp(req.getHostIp()).build();

				return validateGenerateCaptcha(reqTrimData);
			}
		}
		throw new CaptchaGenerationException("$$Invalid Request.");

	}
	public Boolean validateSessionCaptcha(CaptchaAuthRequest request, int sessionTimeOut) {
		captchaClient.authenticate(request.getClientId(), request.getClientSecert());

		CaptchaSecurityRedisData captchaEntity = null;
		// Retrieve data from the redis cache store.
		String captchaRedisData = redisCacheService.get(request.getCaptchaId().toString());
		if (!StringUtils.isEmpty(captchaRedisData)) {
			log.info("retrive captcha data from the redis cached : {}", captchaRedisData);
			try {
				captchaEntity = new Gson().fromJson(captchaRedisData, CaptchaSecurityRedisData.class);
			} catch (Exception e) {
				log.error("Exception : {}", e.getMessage());
			}
			log.info("captchaEntity : {}", captchaEntity);
		}
		return Objects.nonNull(captchaEntity) ? validateCaptcha(captchaEntity, request.getUserAction(), sessionTimeOut)
				: false;

	}

	public Boolean validateSessionCaptcha(String captchaData, int sessionTimeOut) {
		String captchaPayload = DecryptRSAUtil.decrypt(CaptchaUtils.decodebase64(captchaData));
		CaptchaAuthRequest req = null;
		if (StringUtils.isNotBlank(captchaPayload)) {
			Gson gson = new Gson();
			req = gson.fromJson(captchaPayload, CaptchaAuthRequest.class);
			if (Objects.nonNull(req)) {
				CaptchaAuthRequest trimCaptchaReq = CaptchaAuthRequest.builder()
						.clientSecert(req.getClientSecert().trim()).clientId(req.getClientId().trim())
						.captchaId(req.getCaptchaId()).userAction(req.getUserAction().trim()).build();

				return validateSessionCaptcha(trimCaptchaReq, sessionTimeOut);
			}
		}
		throw new CaptchaSessionExpiredException("$$Invalid Session/Invalid Captcha.");

	}

	private boolean validateCaptcha(CaptchaSecurityRedisData captchaEntity, String userAction, int sessionTimeOut) {
		if (captchaEntity.getStatus().equals(CaptchaStatus.EXPIRED)) {
			return false;
		}
		boolean isValidCaptcha = false;

		// Captcha Timeout not meet, Mark status Expired and valid as false.
		if (Boolean.TRUE.equals(PhrDateUtility.checkExpiry(captchaEntity.getCreatedDate().getTime(), sessionTimeOut))) {
			// updatedata(captchaEntity, CaptchaStatus.EXPIRED, userAction, false, false);
			redisCacheService.remove(captchaEntity.getCaptchaId().toString());
			isValidCaptcha = false;
		} else if (captchaEntity.getStatus().equals(CaptchaStatus.GENERATE)) {
			return validateUserCaptchaAction(captchaEntity, userAction);
		} else if (captchaEntity.getStatus().equals(CaptchaStatus.ACTIVE) && captchaEntity.isCaptchaValid()) {

			// CAPTACHA max attempt meet, Mark status Expired and valid as false.
			if (captchaEntity.getRetryCount() >= Integer.parseInt(captchaAttempt)) {
				redisCacheService.remove(captchaEntity.getCaptchaId().toString());
				isValidCaptcha = false;
			} else {
				updatedata(captchaEntity, CaptchaStatus.ACTIVE, null, true, false);
				removeRedisLockValidSession(captchaEntity.getClientId(), captchaEntity.getClientIp());
				redisCacheService.put(captchaEntity.getCaptchaId().toString(), new Gson().toJson(captchaEntity));
				isValidCaptcha = true;
			}
		} else {
			isValidCaptcha = false;
		}
		return isValidCaptcha;
	}

	private Boolean validateUserCaptchaAction(CaptchaSecurityRedisData captchaEntity, String userAction)
			throws CaptchaSessionExpiredException {
		boolean isValidSession = false;
		String solution = captchaEntity.getCaptchaAnswer();
		if (solution.equals(userAction)) {
			updatedata(captchaEntity, CaptchaStatus.ACTIVE, userAction, true, false);
			isValidSession = true;
			// Update redis cache for the security captcha
			redisCacheService.put(captchaEntity.getCaptchaId().toString(), new Gson().toJson(captchaEntity));
			return isValidSession;
		} else {
			// CAPTACHA max attempt meet mark status Expired and valid as false.
			if (captchaEntity.getRetryCount() >= Integer.parseInt(captchaAttempt)) {
				redisCacheService.remove(captchaEntity.getCaptchaId().toString());
			} else {
				updatedata(captchaEntity, null, null, false, true);
				redisCacheService.put(captchaEntity.getCaptchaId().toString(), new Gson().toJson(captchaEntity));
			}
		}
		throw new CaptchaCodeValidationException("Captcha verification failed, Please enter valid answer/code.");

	}

	/**
	 * Validate Credentials and Generate Captcha
	 * 
	 * @param request
	 * @return
	 */
	private CaptchaBuilderResponse validateGenerateCaptcha(CaptchaGenerateRequest request) {
		// Valaidate Request
		authRequestTime(request.getAuthTs());
		// Validate Credentials
		captchaClient.authenticate(request.getClientId(), request.getClientSecert());
		if (StringUtils.isNotBlank(request.getType()) && CaptchaType.isValid(request.getType().toUpperCase())) {
			return getTextCaptcha(request);
		} else {
			throw new CaptchaGenerationException("$$Type is Invalid. Please enter valid type.");
		}

	}

	/**
	 * Generate the Captcha Entity and Save to DB
	 * 
	 * @param captchaObject
	 * @return
	 */
	private CaptchaSecurityRedisData generateCaptchaTransaction(Map<String, String> captchaObject,
			CaptchaGenerateRequest request) {
		String captchaText = "";
		String captchaAnswer = "";
		if (!captchaObject.isEmpty()) {
			captchaText = captchaObject.get(CAPTCHA_TEXT_KEY);
			captchaAnswer = captchaObject.get(CAPTCHA_ANSWER_KEY);
		} else {
			throw new CaptchaGenerationException();
		}

		CaptchaSecurityRedisData captchaSecurityEntity = CaptchaSecurityRedisData.builder().captchaText(captchaText)
				.captchaAnswer(captchaAnswer).status(CaptchaStatus.GENERATE)
				// SET REQ INFO
				.clientIp(request.getHostIp()).longitude(request.getLongitude()).latitude(request.getLatitude())
				.createdDate(new Date()).updateDate(new Date()).captchaId(UUID.randomUUID())
				.clientId(PhrUtilits.clientId()).lastUpdatedBy(PhrUtilits.clientId()).build();
		String captchaJson = new Gson().toJson(captchaSecurityEntity);
		// Store security captcha in the redis cache
		log.info("captchaSecurityEntity  : {} ", captchaJson);
		redisCacheService.put(captchaSecurityEntity.getCaptchaId().toString(), captchaJson);
		return captchaSecurityEntity;
	}

	/**
	 * Generate the Mathematical Captcha
	 * 
	 * @return
	 */
	private CaptchaBuilderResponse getTextCaptcha(CaptchaGenerateRequest request) {

		TextProducer textProducer = null;
		Captcha captcha = null;
		CaptchaSecurityRedisData captchaSecurityEntity = null;
		if (request.getType().equalsIgnoreCase(CaptchaType.MATH.name())) {
			captchaSecurityEntity = generateCaptchaTransaction(GenerateCaptchaText.generateCaptchaMathTextModel(2),
					request);
			textProducer = new CaptchaTextProducer(captchaSecurityEntity.getCaptchaText());
		}
		if (request.getType().equalsIgnoreCase(CaptchaType.TEXT.name())) {
			captchaSecurityEntity = generateCaptchaTransaction(
					GenerateCaptchaText.generateCaptchaTextModel(textCaptachaLength), request);
			textProducer = new CaptchaTextProducer(captchaSecurityEntity.getCaptchaText());
		}
		try {
			captchaGenerator = new CaptchaGenerator(textProducer);
		} catch (Exception e) {
			throw new CaptchaGenerationException();
		}
		captcha = captchaGenerator.createCaptcha(200, 100);
		CaptchaUtils.encodeBase64(captcha);
		return CaptchaBuilderResponse.builder().captchaId(captchaSecurityEntity.getCaptchaId())
				.captcha(CaptchaUtils.encodeBase64(captcha)).build();

	}

	private boolean authRequestTime(String timeStamp) {
		Date authTimeStamp = null;
		boolean isExpire = false;
		try {
			long timeStampInMillis = Long.parseLong(timeStamp);
			authTimeStamp = new Date(timeStampInMillis);
		} catch (Exception e) {
			throw new CaptchaGenerationException("$$Invalid AuthTs Value, Please enter valid data");
		}
		isExpire = PhrDateUtility.checkPasswordLoginReqExpiry(authTimeStamp.getTime(), authTimeOutInSec);
		if (isExpire) {
			throw new CaptchaGenerationException("$$Invalid Request");
		}

		return isExpire;

	}

	private void bruteForceCheck(String clientId, String hostIp) {

		String clientIP = hostIp;
		log.info("Generating Captcha,  Clinet IP:: {}", clientIP);
		String checksum = PHRIdUtils.checksum(CAPTCHA_SALT + clientIP + clientId);

		int requestPerSec = captchaAllowed;
		int requestLock = captchaLocked;
		boolean isValid = limiterService.checkBruteAttack(checksum, requestPerSec, requestLock, TimeUnit.MINUTES);

		if (!isValid) {
			log.info("Multiple Req Exception, clientID: {}", clientId);
			throw new CaptchaGenerationException(String
					.format("$$Multiple captcha requests are not allowed. Please try after %s MINUTES.", requestLock));
		}

	}

	private CaptchaSecurityRedisData updatedata(CaptchaSecurityRedisData captchaEntity, CaptchaStatus captchStatus,
			String userAction, Boolean isValid, Boolean updateAttempt) {
		if (captchaEntity != null) {
			if (captchStatus != null) {
				captchaEntity.setStatus(captchStatus);
			}
			if (Boolean.TRUE.equals(updateAttempt)) {
				captchaEntity.setRetryCount(captchaEntity.getRetryCount() + 1);
			}
			if (userAction != null) {
				captchaEntity.setCaptchAction(userAction);
			}
			captchaEntity.setCaptchaValid(isValid);
		}

		return captchaEntity;
	}

	private void removeRedisLockValidSession(String clientId, String hostIp) {
		String clientIP = hostIp;
		log.info("RemovingRedisLock Captcha,  Clinet IP:: {}", clientIP);
		String key = PHRIdUtils.checksum(CAPTCHA_SALT + clientIP + clientId);
		limiterService.flushKey(key);
	}

}
