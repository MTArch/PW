package com.parserlabs.phr.service;

import java.util.Objects;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.parserlabs.phr.adapter.serivce.AuthAdapterProxy;
import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrAuthTransactionEntity;
import com.parserlabs.phr.enums.AuthStatusEnums;
import com.parserlabs.phr.enums.LoginMethodsEnum;
import com.parserlabs.phr.enums.RedisRequestSalt;
import com.parserlabs.phr.enums.SMSTypeEnums;
import com.parserlabs.phr.exception.DatabaseException;
import com.parserlabs.phr.exception.OtpMismatchedException;
import com.parserlabs.phr.exception.PasswordMismatchedException;
import com.parserlabs.phr.exception.SystemException;
import com.parserlabs.phr.exception.TransactionNotFoundException;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.email.EmailVerifyAuthTransactionOTP;
import com.parserlabs.phr.model.login.phr.LoginViaMobileEmailRequest;
import com.parserlabs.phr.model.login.phr.LoginViaPhrRequest;
import com.parserlabs.phr.model.registration.ResendLoginOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.request.AuthIntRequestPayLoad;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.repository.AuthTransactionRepository;
import com.parserlabs.phr.security.RequestFlooding;
import com.parserlabs.phr.service.sms.SMSService;
import com.parserlabs.phr.utils.Argon2Encoder;
import com.parserlabs.phr.utils.GeneralUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author suraj
 *
 */
@Service
@Slf4j
@CustomSpanned
public class AuthenticationService {

	@Value("${mobileotp.length:6}")
	private long mobileotpLength;

	@Autowired
	private AuthAdapterProxy authAdapterProxy;

	@Autowired
	private SMSService smsService;

	@Autowired
	private AuthTransactionRepository authTransactionRepository;

	@Autowired
	private RequestFlooding otpFloodService;

	@Autowired
	private OtpPasswordService otpPasswordService;

	public TransactionResponse authInitiate(AuthIntRequestPayLoad authIntRequestPayLoad) {
		
		return saveAuthTransaction(authAdapterProxy.processAuthInitiate(authIntRequestPayLoad));
	}


	public TransactionResponse resendOtp(ResendOTPRequest resendOTPRequest) {
		// TODO Auto-generated method stub
		PhrAuthTransactionEntity phrAuthTransactionEntity = authTransactionRepository.findByAuthTransactionId(
				UUID.fromString(resendOTPRequest.getTransactionId())
				).get();

		ResendLoginOTPRequest resendOTPAadharRequest = ResendLoginOTPRequest.builder()
				                                               .txnId(phrAuthTransactionEntity.getHealthIdTransaction())
				                                               .authMethod(phrAuthTransactionEntity.getAuthMethod())
				                                               .build();
		return saveAuthTransaction(authAdapterProxy.processResendOtp(resendOTPAadharRequest,phrAuthTransactionEntity));
	}
	
	
	/**
	 * Start a new transaction with mobile OTP.
	 * 
	 * @param genOtpRequest
	 * @return String
	 */
	public TransactionResponse startPhrMobileLoginAuthTransaction(UserDTO user, LoginViaPhrRequest request) {
		// OTP Flood Check
		// 1. OTP Flooding Using REDIS Server Only.
		otpFloodService.check(request.getPhrAddress(), RedisRequestSalt.LOGIN_VERIFY_MOBILE_PHR_OTP.name());

		// Restrict the OTP generation to 30 seconds interval
		otpFloodService.check30SecOtpFlooding(request.getPhrAddress(),
				RedisRequestSalt.LOGIN_VERIFY_MOBILE_PHR_OTP.name());

		PhrAuthTransactionEntity otpMobileTxn = PhrAuthTransactionEntity.builder().build();
		otpMobileTxn.setStatus(AuthStatusEnums.ACTIVE.name());
		otpMobileTxn.setPhrAddress(request.getPhrAddress());
		otpMobileTxn.setMobile(user.getMobile());
		otpMobileTxn.setAuthMethod(request.getAuthMethod().name());
		String otp = GeneralUtils.getRandomCode(mobileotpLength);
		otpMobileTxn.setOtp(Argon2Encoder.encode(otp, user.getMobile()));
		log.info("Mobile Auth [Mobile :{}, OTP: {}]", user.getMobile(), otp);
		smsService.sendOtpAuthenticationSMS(user.getMobile(), otp, SMSTypeEnums.LOGIN_OTP);
		// Save transaction data
		saveAuthTransaction(otpMobileTxn);
		log.info("Auth txnId: {}", otpMobileTxn.getAuthTransactionId());
		return TransactionResponse.builder().transactionId(otpMobileTxn.getAuthTransactionId().toString()).build();
	}

	/**
	 * Start a new transaction with mobile OTP.
	 * 
	 * @param genOtpRequest
	 * @return String
	 */
	public TransactionResponse startPasswordLoginAuthTransaction(UserDTO user, LoginViaPhrRequest request) {
		// OTP Flood Check
		// 1. OTP Flooding Using REDIS Server Only.
		otpFloodService.check(request.getPhrAddress(), RedisRequestSalt.LOGIN_PWD.name());

		PhrAuthTransactionEntity authPasswordTxn = PhrAuthTransactionEntity.builder().build();
		authPasswordTxn.setStatus(AuthStatusEnums.ACTIVE.name());
		authPasswordTxn.setPassword(user.getPassword());
		authPasswordTxn.setAuthMethod(request.getAuthMethod().name());
		authPasswordTxn.setPhrAddress(user.getPhrAddress());
		// Save transaction data
		saveAuthTransaction(authPasswordTxn);
		log.info("Auth pwd txnId: {}", authPasswordTxn.getAuthTransactionId());
		return TransactionResponse.builder().transactionId(authPasswordTxn.getAuthTransactionId().toString()).build();
	}

	/**
	 * Email verification transaction number.
	 * 
	 * @param PhrTransactionEntity, EmailVerificationRequest
	 * @return UUID
	 */
	public EmailVerifyAuthTransactionOTP startPhrEmailLoginAuthTransaction(UserDTO user, LoginViaPhrRequest request) {

		// OTP Flood Check
		// 1. PWD Flooding Using REDIS Server Only.
		otpFloodService.check(request.getPhrAddress(), RedisRequestSalt.LOGIN_EMAIL_OTP.name());

		// Restrict the OTP generation to 30 seconds interval
		otpFloodService.check30SecOtpFlooding(request.getPhrAddress(), RedisRequestSalt.LOGIN_EMAIL_OTP.name());

		PhrAuthTransactionEntity otpEmailAuthTxn = PhrAuthTransactionEntity.builder().build();
		otpEmailAuthTxn.setPhrAddress(user.getPhrAddress());
		otpEmailAuthTxn.setAuthMethod(request.getAuthMethod().name());
		otpEmailAuthTxn.setStatus(AuthStatusEnums.ACTIVE.name());
		otpEmailAuthTxn.setEmail(user.getEmail());
		String otp = GeneralUtils.getRandomCode(mobileotpLength);
		otpEmailAuthTxn.setOtp(Argon2Encoder.encode(otp, user.getEmail()));
		log.info("Email Auth [Email :{}, OTP: {}]", user.getEmail(), otp);
		// Save transaction data
		saveAuthTransaction(otpEmailAuthTxn);
		log.info("Email Auth txnId: {}", otpEmailAuthTxn.getAuthTransactionId());
		return EmailVerifyAuthTransactionOTP.builder().OTP(otp).phrAuthTransactionEntity(otpEmailAuthTxn).build();
	}

	/**
	 * Start a new transaction with mobile OTP( Pre-verified case.).
	 * 
	 * @param genOtpRequest
	 * @return String
	 */
	public TransactionResponse startMobileLoginAuthTransaction(LoginViaMobileEmailRequest request) {
		String mobile = request.getInput();
		// OTP Flood Check
		// 1. OTP Flooding Using REDIS Server Only.
		otpFloodService.check(mobile, RedisRequestSalt.LOGIN_MOBILE_OTP.name());

		// Restrict the OTP generation to 30 seconds interval
		otpFloodService.check30SecOtpFlooding(mobile, RedisRequestSalt.LOGIN_MOBILE_OTP.name());

		PhrAuthTransactionEntity otpMobileTxn = PhrAuthTransactionEntity.builder().build();
		otpMobileTxn.setMobile(mobile);
		otpMobileTxn.setAuthMethod(LoginMethodsEnum.MOBILE_OTP.name());
		otpMobileTxn.setStatus(AuthStatusEnums.PRE_OTP.name());
		String otp = GeneralUtils.getRandomCode(mobileotpLength);
		otpMobileTxn.setOtp(Argon2Encoder.encode(otp, mobile));
		log.info("Mobile Auth [Mobile :{}, OTP: {}]", mobile, otp);
		smsService.sendOtpAuthenticationSMS(mobile, otp, SMSTypeEnums.LOGIN_OTP);
		// Save transaction data
		saveAuthTransaction(otpMobileTxn);
		log.info("Auth txnId: {}", otpMobileTxn.getAuthTransactionId());
		return TransactionResponse.builder().transactionId(otpMobileTxn.getAuthTransactionId().toString()).build();
	}

	/**
	 * Email verification transaction number (Pre-verification Flow).
	 * 
	 * @param PhrTransactionEntity, EmailVerificationRequest
	 * @return UUID
	 */
	public EmailVerifyAuthTransactionOTP startEmailLoginAuthTransaction(LoginViaMobileEmailRequest request) {

		String emailAddress = request.getInput();
		// OTP Flood Check
		// 1. PWD Flooding Using REDIS Server Only.
		otpFloodService.check(emailAddress, RedisRequestSalt.LOGIN_EMAIL_OTP.name());

		// Restrict the OTP generation to 30 seconds interval
		otpFloodService.check30SecOtpFlooding(emailAddress, RedisRequestSalt.LOGIN_EMAIL_OTP.name());

		PhrAuthTransactionEntity otpEmailAuthTxn = PhrAuthTransactionEntity.builder().build();
		otpEmailAuthTxn.setAuthMethod(LoginMethodsEnum.EMAIL_OTP.name());
		otpEmailAuthTxn.setStatus(AuthStatusEnums.PRE_OTP.name());
		otpEmailAuthTxn.setEmail(emailAddress);
		String otp = GeneralUtils.getRandomCode(mobileotpLength);
		otpEmailAuthTxn.setOtp(Argon2Encoder.encode(otp, emailAddress));
		// Save transaction data
		saveAuthTransaction(otpEmailAuthTxn);
		log.info("Email Auth [Email :{}, OTP: {}]", emailAddress, otp);
		log.info("Email Auth txnId: {}", otpEmailAuthTxn.getAuthTransactionId());
		return EmailVerifyAuthTransactionOTP.builder().OTP(otp).phrAuthTransactionEntity(otpEmailAuthTxn).build();
	}

	/**
	 * Start a Re-send/ Regenerate transaction Mobile OTP.
	 * 
	 * @param otpMobileTxn
	 * @return TransactionResponse object
	 */
	public TransactionResponse startResendOtpLoginAuthTransaction(PhrAuthTransactionEntity otpMobileTxn) {

		// OTP Flood Check
		// 1. OTP Flooding Using REDIS Server Only.
		otpFloodService.check(otpMobileTxn.getAuthTransactionId().toString(),
				RedisRequestSalt.LOGIN_MOBILE_OTP_RESEND.name());

		// Restrict the OTP generation to 30 seconds interval
		otpFloodService.check30SecOtpFlooding(otpMobileTxn.getAuthMethod().toString(),
				RedisRequestSalt.LOGIN_MOBILE_OTP_RESEND.name());
		String otp = GeneralUtils.getRandomCode(mobileotpLength);
		otpMobileTxn.setOtp(Argon2Encoder.encode(otp, otpMobileTxn.getMobile()));
		otpMobileTxn.setRetryMethod(otpMobileTxn.getAuthMethod());
		otpMobileTxn.setRetryCount(otpMobileTxn.getRetryCount() + 1);
		smsService.sendOtpAuthenticationSMS(otpMobileTxn.getMobile(), otp, SMSTypeEnums.LOGIN_OTP);
		// Save transaction data
		saveAuthTransaction(otpMobileTxn);

		log.info("Mobile Auth Resend [Mobile :{}, OTP: {}]", otpMobileTxn.getMobile(), otp);
		log.info("Auth txnId: {}", otpMobileTxn.getAuthTransactionId());

		return TransactionResponse.builder().transactionId(otpMobileTxn.getAuthTransactionId().toString()).build();
	}

	/**
	 * RESEND Email verification OTP on Email by transaction number.
	 * 
	 * @param PhrTransactionEntity, EmailVerificationRequest
	 * @return UUID
	 */
	public EmailVerifyAuthTransactionOTP startResendOtpEmailLoginAuthTransaction(
			PhrAuthTransactionEntity otpEmailAuthTxn) {

		// OTP Flood Check
		// 1. PWD Flooding Using REDIS Server Only.
		otpFloodService.check(otpEmailAuthTxn.getAuthTransactionId().toString(),
				RedisRequestSalt.LOGIN_EMAIL_OTP.name());

		// Restrict the OTP generation to 30 seconds interval
		otpFloodService.check30SecOtpFlooding(otpEmailAuthTxn.getAuthTransactionId().toString(),
				RedisRequestSalt.LOGIN_EMAIL_OTP.name());

		String otp = GeneralUtils.getRandomCode(mobileotpLength);
		otpEmailAuthTxn.setOtp(Argon2Encoder.encode(otp, otpEmailAuthTxn.getEmail()));
		otpEmailAuthTxn.setRetryMethod(otpEmailAuthTxn.getAuthMethod());
		otpEmailAuthTxn.setRetryCount(otpEmailAuthTxn.getRetryCount() + 1);
		// Save transaction data
		saveAuthTransaction(otpEmailAuthTxn);

		log.info("Email RESEND Auth [Email :{}, OTP: {}]", otpEmailAuthTxn.getEmail(), otp);
		log.info("Email Auth txnId: {}", otpEmailAuthTxn.getAuthTransactionId());
		return EmailVerifyAuthTransactionOTP.builder().OTP(otp).phrAuthTransactionEntity(otpEmailAuthTxn).build();
	}

	@Transactional
	public TransactionResponse saveAuthTransaction(PhrAuthTransactionEntity phrAuthTransactionEntity) {
		try {
			authTransactionRepository.save(phrAuthTransactionEntity);
		} catch (Exception e) {
			log.error("Exception saving to auth table. ", e);
			throw new DatabaseException("Exception occured while saving Auth transaction in DB");
		}
		return TransactionResponse.builder().transactionId(phrAuthTransactionEntity.getAuthTransactionId().toString())
				.build();
	}

	/**
	 * Verify PHR ADDRESS VIA (MOBILE EMAIL) Login OTP
	 * 
	 * @param phrAuthTransactionEntity
	 * @param otp
	 */
	public void verifyOtp(PhrAuthTransactionEntity phrAuthTransactionEntity, String otp) {
		// Check OTP expire
		otpFloodService.check10MinutesExpire(phrAuthTransactionEntity);
		// 1: Check for OTP verify flooding
		otpFloodService.check(phrAuthTransactionEntity.getAuthTransactionId().toString(),
				RedisRequestSalt.LOGIN_VERIFY_MOBILE_EMAIL_OTP.name());
		if (!otpPasswordService.isOtpSame(phrAuthTransactionEntity, otp)) {
			throw new OtpMismatchedException();
		}
		phrAuthTransactionEntity.setStatus(AuthStatusEnums.DONE.name());
		saveAuthTransaction(phrAuthTransactionEntity);
	}

	/**
	 * Verify Login EMAIL MOBILE OTP
	 * 
	 * @param phrAuthTransactionEntity
	 * @param otp
	 */
	public void verifyOtpEmailMobile(PhrAuthTransactionEntity phrAuthTransactionEntity, String otp) {
		// Check OTP expire
		otpFloodService.check10MinutesExpire(phrAuthTransactionEntity);
		// 1: Check for OTP verify flooding
		otpFloodService.check(phrAuthTransactionEntity.getAuthTransactionId().toString(),
				RedisRequestSalt.LOGIN_VERIFY_MOBILE_EMAIL_OTP.name());
		if (!otpPasswordService.isOtpSame(phrAuthTransactionEntity, otp)) {
			throw new OtpMismatchedException();
		}
		phrAuthTransactionEntity.setStatus(AuthStatusEnums.OTP_VERIFIED.name());
		saveAuthTransaction(phrAuthTransactionEntity);
	}

	/**
	 * Verify Login OTP
	 * 
	 * @param phrAuthTransactionEntity
	 * @param otp
	 */
	public void verifyPassword(PhrAuthTransactionEntity phrAuthTransactionEntity, String otp) {
		// 1: Check for OTP verify flooding
		otpFloodService.check(phrAuthTransactionEntity.getAuthTransactionId().toString(),
				RedisRequestSalt.LOGIN_PWD_VERIFY.name());
		if (!otpPasswordService.isPasswordSame(phrAuthTransactionEntity, otp)) {
			throw new PasswordMismatchedException();
		}
		phrAuthTransactionEntity.setStatus(AuthStatusEnums.PWD_VERIFIED.name());
		saveAuthTransaction(phrAuthTransactionEntity);
	}

	public PhrAuthTransactionEntity fetchAuthTransactionEntitybyId(String transactionID,String methodName) {
		return findAuthTransactionEntitybyId(UUID.fromString(transactionID),methodName);
	}

	public PhrAuthTransactionEntity fetchAuthTransactionEntitybyId(UUID transactionID,String methodName) {
		return findAuthTransactionEntitybyId(transactionID,methodName);
	}
	
	public PhrAuthTransactionEntity fetchTransactionByPhrAddres(String phrAddress) {
		PhrAuthTransactionEntity phrAuthTransactionEntity = null;
		try {
			phrAuthTransactionEntity = authTransactionRepository.findTop1ByPhrAddressOrderByIdDesc(phrAddress).orElse(null);
		} catch (Exception exp) {
			throw new SystemException(
					String.format("Exception occured while fetching phrAddress for phrAddress '%s'", phrAddress), exp);
		}
		if (Objects.isNull(phrAuthTransactionEntity)) {
			throw new TransactionNotFoundException(
					String.format("Transaction is not found for %s phrAddress", phrAddress));
		}
		return phrAuthTransactionEntity;
	}

	private PhrAuthTransactionEntity findAuthTransactionEntitybyId(UUID transactionID,String methodName) {
		log.info("Method name  {} is called with {} UUID",methodName,transactionID);
		PhrAuthTransactionEntity phrAuthTransactionEntity = null;
		try {
			phrAuthTransactionEntity = authTransactionRepository.findByAuthTransactionId(transactionID).orElse(null);
		} catch (Exception exp) {
			throw new SystemException(
					String.format("Exception occured while fetching Auth Transaction for UUID '%s'", transactionID), exp);
		}
		if (Objects.isNull(phrAuthTransactionEntity)) {
			throw new TransactionNotFoundException(
					String.format("Auth Transaction is not found for %s UUID and %s method", transactionID,methodName));
		}
		return phrAuthTransactionEntity;
	}

	public PhrAuthTransactionEntity fetchAuthTransactionEntitybyIdStatus(String transactionID, String status,String methodName) {
		return fetchAuthTransactionEntitybyIdStatus(UUID.fromString(transactionID), status,methodName);
	}

	public PhrAuthTransactionEntity fetchAuthTransactionEntitybyIdStatus(UUID transactionID, String status,String methodName) {
		return findAuthTransactionEntitybyIdStatus(transactionID, status,methodName);
	}
    
	private PhrAuthTransactionEntity findAuthTransactionEntitybyIdStatus(UUID transactionID, String status,String methodName) {
		log.info("Method name  {} is called with {} UUID and status {} ",methodName,transactionID,status);
		if (StringUtils.isBlank(status))
			status = AuthStatusEnums.ACTIVE.name();
		PhrAuthTransactionEntity phrAuthTransactionEntity = null;
		try {
			phrAuthTransactionEntity = authTransactionRepository.findByAuthTransactionId(transactionID)
					.orElse(null);
		} catch (Exception exp) {
			throw new SystemException(
					String.format("Exception occured while fetching Auth Transaction for UUID '%s'", transactionID), exp);
		}
		if (Objects.isNull(phrAuthTransactionEntity)) {
			throw new TransactionNotFoundException(
					String.format("Auth Transaction is not found for %s UUID and %s status and %s method ", transactionID, status,methodName));
		}
		return phrAuthTransactionEntity;
	}

}
