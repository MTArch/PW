package com.parserlabs.phr.security;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.entity.PhrAuthTransactionEntity;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.exception.OtpExpireException;
import com.parserlabs.phr.exception.OtpFloodException;
import com.parserlabs.phr.exception.OtpFloodLockOutException;
import com.parserlabs.phr.exception.ProfileEditFloodLockOutException;
import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.service.RedisRateLimiterService;
import com.parserlabs.phr.utils.PhrUtilits;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Rajesh
 *
 */

@Component
@Slf4j
public class RequestFlooding {

	private final static String DATA_DILIMITER = "|";

	@Value("${generate.otp.allowed:20}")
	private Integer GENERATE_OTP_ALLOWED;

	@Value("${generate.otp.lockout:1}")
	private Integer GENERATE_OTP_LOCKOUT;

	@Value("${mobileotp.expire.time:10}")
	private long OTP_EXPIRE_TIME_MIN;

	@Value("${edit.profile.allowed:20}")
	private Integer EDIT_PROFILE_ALLOWED;

	@Value("${edit.profile.lockout:24}")
	private Integer EDIT_PROFILE_LOCKOUT;

	@Autowired
	private RedisRateLimiterService rateLimiterService;

	/***
	 * Method To Check the OTP FLooding and Lock the User for Specific Time
	 * 
	 * @param value
	 * @param salt
	 * @return boolean
	 */
	public boolean check(String value, String salt) {
		int requestPerHours = GENERATE_OTP_ALLOWED;
		int requestPerHoursLock = GENERATE_OTP_LOCKOUT;
		String key = value.concat(salt);
		log.info("key: {}", key);
		boolean isAllowed = rateLimiterService.isAllowed(key, requestPerHours, requestPerHoursLock, TimeUnit.HOURS);
		if (!isAllowed) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("lockout").value(requestPerHoursLock + " hours")
					.build();
			throw new OtpFloodLockOutException("OTP attempts exhausted", attribute);
		}
		return isAllowed;

	}

	/***
	 * Method To Check the Edit profi;e FLooding and Lock the User for Specific Time
	 * 
	 * @param value
	 * @param salt
	 * @return boolean
	 */
	public boolean checkProfileEditFlooding(String value, String salt) {
		int requestPerHours = EDIT_PROFILE_ALLOWED;
		int requestPerHoursLock = EDIT_PROFILE_LOCKOUT;
		String key = value.concat(salt);
		log.info("key: {}", key);
		boolean isAllowed = rateLimiterService.isAllowed(key, requestPerHours, requestPerHoursLock, TimeUnit.HOURS);
		if (!isAllowed) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("lockout").value(requestPerHoursLock + " hours")
					.build();
			throw new ProfileEditFloodLockOutException("Profile Edit attempts exhausted", attribute);
		}
		return isAllowed;
	}

	/**
	 * Method to restrict the OTP generation for the 30 sec interval
	 * 
	 * @apiNote Please note that TTL is only applied on the each key only.
	 * 
	 * @param value
	 * @param salt
	 * @return
	 */
	public boolean check30SecOtpFlooding(String value, String salt) {
		int EXPIRE_TIME_IN_SEC = 30;
		String key = value.concat(salt);
		String payload = value.concat(DATA_DILIMITER)
				.concat(String.valueOf(PhrUtilits.expiryTime(EXPIRE_TIME_IN_SEC, ChronoUnit.SECONDS)));
		String redisData = rateLimiterService.fetchStoreDataToTempalteRedis(key, payload, EXPIRE_TIME_IN_SEC,
				TimeUnit.SECONDS);
		if (StringUtils.isNotBlank(redisData) && !redisData.equals("saved")) {
			throw new OtpFloodException();
		}
		return true;
	}

	public void check10MinutesExpire(PhrTransactionEntity txn) {
		if (PhrUtilits.checkExpiry(txn.getCreatedDate(), OTP_EXPIRE_TIME_MIN)) {
			throw new OtpExpireException();
		}
	}

	public void check10MinutesExpire(PhrAuthTransactionEntity phrAuthTransactionEntity) {
		if (PhrUtilits.checkExpiry(phrAuthTransactionEntity.getCreatedDate(), OTP_EXPIRE_TIME_MIN)) {
			throw new OtpExpireException();
		}
	}

	public String gatewayToken(String value, String salt, int expired) {
		log.info("gateway token storing in redis");
		String key = value.concat(salt);
		String payload = value.concat(DATA_DILIMITER)
				.concat(String.valueOf(PhrUtilits.expiryTime(expired, ChronoUnit.MINUTES)));
		value=rateLimiterService.fetchStoreDataToTempalteRedis(key, payload, expired, TimeUnit.MINUTES);
		log.info("gateway token {} fetch from  redis ", value);
		return value;

	}

}
