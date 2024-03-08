package com.parserlabs.phr.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.enums.AuthStatusEnums;
import com.parserlabs.phr.enums.RedisRequestSalt;
import com.parserlabs.phr.exception.DatabaseException;
import com.parserlabs.phr.exception.OtpMismatchedException;
import com.parserlabs.phr.exception.SystemException;
import com.parserlabs.phr.exception.TransactionExpiredException;
import com.parserlabs.phr.exception.TransactionNotFoundException;
import com.parserlabs.phr.exception.UserNotFoundException;
import com.parserlabs.phr.model.PhrTransactionEntityLite;
import com.parserlabs.phr.model.email.EmailNotificationRequest;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.repository.TransactionRepository;
import com.parserlabs.phr.security.RequestFlooding;
import com.parserlabs.phr.service.sms.SMSService;
import com.parserlabs.phr.utils.Argon2Encoder;
import com.parserlabs.phr.utils.GeneralUtils;
import com.parserlabs.phr.utils.PhrUtilits;

import lombok.extern.slf4j.Slf4j;

/*
 * @author Rajesh
 * */

@Service
@CustomSpanned
@Slf4j
public class TransactionService {

	@Value("${mobileotp.length:6}")
	private long mobileotpLength;

	@Value("${transaction.overdue.interval:2}")
	private Integer transactionOverdueInterval;

	@Autowired
	private SMSService smsService;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private OtpPasswordService otpPasswordService;

	@Autowired
	private RequestFlooding otpFlooding;

	/**
	 * Start a new transaction with mobile OTP.
	 * 
	 * @param genOtpRequest
	 * @return String
	 */
	public TransactionResponse startMobileOTPTransaction(GenerateOTPRequest genOtpRequest) {
		String mobileNumber = genOtpRequest.getValue();
		// OTP Flood Check
		// 1. OTP Flooding Using REDIS Server Only.
		otpFlooding.check(mobileNumber, RedisRequestSalt.GENERATE_MOBILE_OTP.name());

		// Restrict the OTP generation to 30 seconds interval
		otpFlooding.check30SecOtpFlooding(mobileNumber, RedisRequestSalt.OTP_REG_30SEC_CHECK.name());

		PhrTransactionEntity otpTxn = PhrTransactionEntity.builder().build();
		otpTxn.setMobile(mobileNumber);
		otpTxn.setStatus(AuthStatusEnums.ACTIVE.name());
		String otp = GeneralUtils.getRandomCode(mobileotpLength);
		otpTxn.setOtp(Argon2Encoder.encode(otp, mobileNumber));
		log.info("Mobile OTP GENERATE - [Mobile :{}, OTP: {}]", mobileNumber, otp);
		smsService.sendVerificationOtp(mobileNumber, otp);
		// Save transaction data
		save(otpTxn);
		log.info("Mobile OTP GENERATE txnId: {}", otpTxn.getTransactionId());
		return TransactionResponse.builder().transactionId(otpTxn.getTransactionId().toString()).build();
	}

	/**
	 * Start a new transaction with mobile OTP when profile updated.
	 * 
	 * @param genOtpRequest
	 * @return String
	 */
	public TransactionResponse startMobileOTPTransactionForProfileUpdate(GenerateOTPRequest genOtpRequest) {
		String mobileNumber = genOtpRequest.getValue();
		// OTP Flood Check
		// 1. OTP Flooding Using REDIS Server Only.
		otpFlooding.check(mobileNumber, RedisRequestSalt.GENERATE_MOBILE_OTP.name());

		// Restrict the OTP generation to 30 seconds interval
		otpFlooding.check30SecOtpFlooding(mobileNumber, RedisRequestSalt.OTP_REG_30SEC_CHECK.name());

		PhrTransactionEntity otpTxn = PhrTransactionEntity.builder().build();
		otpTxn.setMobile(mobileNumber);
		otpTxn.setStatus(AuthStatusEnums.ACTIVE.name());
		String otp = GeneralUtils.getRandomCode(mobileotpLength);
		otpTxn.setOtp(Argon2Encoder.encode(otp, mobileNumber));
		log.info("Mobile OTP GENERATE - [Mobile :{}, OTP: {}]", mobileNumber, otp);
		smsService.sendUpdateVerificationOtp(mobileNumber, otp);
		// Save transaction data
		save(otpTxn);
		log.info("Mobile OTP GENERATE txnId: {}", otpTxn.getTransactionId());
		return TransactionResponse.builder().transactionId(otpTxn.getTransactionId().toString()).build();
	}
	
	public boolean resendVerificationOtp(UUID txnId) {
		PhrTransactionEntity txn = findTransaction(txnId);
		if (StringUtils.isEmpty(txn.getMobile())) {
			log.warn("Mobile number not found in txn to resend OTP.");
			throw new UserNotFoundException("No Transaction found with given Id.");
		}

		// OTP Flood Check
		otpFlooding.check(txn.getMobile(), RedisRequestSalt.RESEND_MOBILE_OTP.name());

		// Check Transaction Services.
		if (PhrUtilits.checkExpiry(txn.getCreatedDate(), 120)) {
			throw new TransactionExpiredException("Transaction is expired.");
		}
		String otp = GeneralUtils.getRandomCode(mobileotpLength);
		txn.setOtp(Argon2Encoder.encode(otp, txn.getMobile()));
		log.info("Sending OTP on mobile {} and OTP is {}", txn.getMobile(), otp);

		// Save transaction data
		save(txn);
		return smsService.sendVerificationOtp(txn.getMobile(), otp);
	}

	/**
	 * Verify OTP and mark transaction as mobileVerified.
	 * 
	 * @param txn
	 * @param otp
	 */
	public void verifyOtp(PhrTransactionEntity txn, String otp) {
		// Check OTP expire
		otpFlooding.check10MinutesExpire(txn);
		// 1: Check for OTP verify flooding
		otpFlooding.check(txn.getTransactionId().toString(), RedisRequestSalt.REG_VERIFY_EMAIL_MOBILE_OTP.name());

		if (otpPasswordService.isOtpSame(txn, otp)) {
			if (StringUtils.isNotBlank(txn.getMobile())) {
				txn.setMobileVerified(true);
			}
			if (StringUtils.isNotBlank(txn.getEmail())) {
				txn.setEmailVerified(true);
			}
			transactionRepository.save(txn);
		} else {
			throw new OtpMismatchedException();
		}

	}

	/**
	 * Verify OTP and mark transaction as mobileVerified.
	 * 
	 * @param txn
	 * @param otp
	 * @param status
	 */

	public void verifyOtp(PhrTransactionEntity txn, String otp, String status) {
		// Check OTP expire
		otpFlooding.check10MinutesExpire(txn);
		// 1: Check for OTP verify flooding
		otpFlooding.check(txn.getTransactionId().toString(), RedisRequestSalt.REG_VERIFY_EMAIL_MOBILE_OTP.name());

		if (otpPasswordService.isOtpSame(txn, otp)) {
			txn.setStatus(status);
			if (StringUtils.isNotBlank(txn.getMobile())) // MobileNumber-Flow
				txn.setMobileVerified(true);

			if (StringUtils.isNotBlank(txn.getEmail())) // Email-Flow
				txn.setEmailVerified(true);

			transactionRepository.save(txn);
		} else {
			throw new OtpMismatchedException();
		}
	}

	public PhrTransactionEntity findTransaction(String txnId) {
		return fetchTransaction(UUID.fromString(txnId));
	}

	public PhrTransactionEntity findTransaction(UUID txnId) {
		return fetchTransaction(txnId);
	}

	private PhrTransactionEntity fetchTransaction(UUID txnId) {
		PhrTransactionEntity phrTransactionEntity = null;
		try {
			phrTransactionEntity = transactionRepository.findByTransactionIdOrAuthTransactionId(txnId, txnId)
					.orElse(null);

		} catch (Exception exp) {
			throw new SystemException("Exception occured while fetching Transaction for " + txnId, exp);
		}
		if (Objects.isNull(phrTransactionEntity)) {
			throw new TransactionNotFoundException(String.format("Transaction is not found for %s UUID", txnId));
		}
		return phrTransactionEntity;
	}

	public PhrTransactionEntity fetchTransactionByPhrAddres(String phrAddress) {
		PhrTransactionEntity phrTransactionEntity = null;
		try {
			phrTransactionEntity = transactionRepository.findByPhrAddress(phrAddress).orElse(null);

		} catch (Exception exp) {
			throw new SystemException("Exception occured while fetching phrAddress for " + phrAddress, exp);
		}
		if (Objects.isNull(phrTransactionEntity)) {
			throw new TransactionNotFoundException(
					String.format("Transaction is not found for %s phrAddress", phrAddress));
		}
		return phrTransactionEntity;
	}

	public PhrTransactionEntity findTransaction(String txnId, String status,String methodName) {
		return fetchTransaction(UUID.fromString(txnId), status,methodName);
	}

	public PhrTransactionEntity findTransaction(UUID txnId, String status,String methodName) {
		return fetchTransaction(txnId, status,methodName);
	}

	private PhrTransactionEntity fetchTransaction(UUID txnId, String status,String methodName) {
		 log.info("Method name  {} is called with {} UUID and status {}",methodName,txnId,status);
		PhrTransactionEntity phrTransactionEntity = null;
		try {
			phrTransactionEntity = transactionRepository.findByTransactionId(txnId).orElse(null);

		} catch (Exception exp) {
			throw new SystemException("Exception occured while fetching Transaction for " + txnId, exp);
		}
		if (Objects.isNull(phrTransactionEntity)) {
			throw new TransactionNotFoundException(String.format("Transaction is not found for %s UUID and %s method", txnId,methodName));
		}
		return phrTransactionEntity;
	}

	public List<PhrTransactionEntityLite> findOverdueTransaction() {
		try {
			return transactionRepository.findOverdueTransaction(transactionOverdueInterval).orElse(null);
		} catch (Exception exp) {
			throw new SystemException("Exception occured while fetching overdue Transaction", exp);
		}
	}

	public void deleteTransaction(UUID transactionId) {
		try {
			transactionRepository.deleteByTransactionId(transactionId);
		} catch (Exception e) {
			throw new SystemException("Exception occured while deleting Transaction for " + transactionId, e);
		}
	}

	public void deleteTransactionById(long id) {
		try {
			transactionRepository.deleteById(id);
			;
		} catch (Exception e) {
			throw new SystemException("Exception occured while deleting Id for " + id, e);
		}
	}

	public void deleteTransactionAllByIdInBatch(List<Long> id) {
		try {
			transactionRepository.deleteAllByIdInBatch(id);
		} catch (Exception e) {
			throw new SystemException("Exception occured while deleting Id for " + id, e);
		}
	}

	@Transactional
	public PhrTransactionEntity save(PhrTransactionEntity phrTransactionEntity) {
		try {
			transactionRepository.save(phrTransactionEntity);
		} catch (Exception exp) {
			throw new DatabaseException(
					"Exception occured while saving Transaction for " + phrTransactionEntity.getTransactionId(), exp);
		}
		return phrTransactionEntity;
	}

	/**
	 * Email verification transaction number.
	 * 
	 * @param PhrTransactionEntity, EmailVerificationRequest
	 * @return UUID
	 */
	public EmailNotificationRequest getEmailVerificationTxnAndOTP(GenerateOTPRequest genOtpRequest) {

		String emailAddress = genOtpRequest.getValue();
		// OTP Flood Check
		// 1. OTP Flooding Using REDIS Server Only.
		otpFlooding.check(emailAddress, RedisRequestSalt.REG_EMAIL_OTP.name());

		// Restrict the OTP generation to 30 sec interval
		otpFlooding.check30SecOtpFlooding(emailAddress, RedisRequestSalt.OTP_REG_30SEC_CHECK.name());

		PhrTransactionEntity otpEmailTxn = PhrTransactionEntity.builder().build();
		otpEmailTxn.setEmail(emailAddress);
		otpEmailTxn.setStatus(AuthStatusEnums.ACTIVE.name());
		String otp = GeneralUtils.getRandomCode(mobileotpLength);
		otpEmailTxn.setOtp(Argon2Encoder.encode(otp, emailAddress));
		// log.info("Email Registration [Email :{}, OTP: {}]", emailAddress, otp);
		// Save transaction data
		save(otpEmailTxn);
		log.info("Email Registration txnId: {}", otpEmailTxn.getTransactionId());
		return EmailNotificationRequest.builder().OTP(otp).email(emailAddress)
				.transactionId(otpEmailTxn.getTransactionId().toString()).build();
	}

	public EmailNotificationRequest resendVerificationEmailOtp(String transactionId) {
		PhrTransactionEntity otpEmailTxn = findTransaction(transactionId);
		String emailAddress = otpEmailTxn.getEmail();
		if (StringUtils.isEmpty(otpEmailTxn.getEmail())) {
			log.warn("Email address not found");
			throw new UserNotFoundException("No Transaction found with given Id.");
		}

		// OTP Flood Check
		// 1. OTP Flooding Using REDIS Server Only.
		otpFlooding.check(otpEmailTxn.getEmail(), RedisRequestSalt.RESEND_MOBILE_EMAIL_OTP.name());

		// Restrict the OTP generation to 30 seconds interval
		otpFlooding.check30SecOtpFlooding(otpEmailTxn.getEmail(), RedisRequestSalt.OTP_REG_30SEC_CHECK.name());

		otpEmailTxn.setEmail(otpEmailTxn.getEmail());
		String otp = GeneralUtils.getRandomCode(mobileotpLength);
		otpEmailTxn.setOtp(Argon2Encoder.encode(otp, emailAddress));
		// log.info("Email Registration Resend [Email :{}, OTP: {}]",emailAddress, otp);
		save(otpEmailTxn); // Save transaction data
		log.info("Email Registration Resend txnId: {}", otpEmailTxn.getTransactionId());
		return EmailNotificationRequest.builder().OTP(otp).email(emailAddress)
				.transactionId(otpEmailTxn.getTransactionId().toString()).build();
	}

}
