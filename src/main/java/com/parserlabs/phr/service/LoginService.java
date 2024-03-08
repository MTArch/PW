package com.parserlabs.phr.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.config.security.JwtTokenUtil;
import com.parserlabs.phr.entity.PhrAuthTransactionEntity;
import com.parserlabs.phr.enums.AccountStatus;
import com.parserlabs.phr.enums.AuthStatusEnums;
import com.parserlabs.phr.enums.LoginMethodsEnum;
import com.parserlabs.phr.enums.OperationTypeEmail;
import com.parserlabs.phr.exception.LoginMethodNotAvailableException;
import com.parserlabs.phr.exception.MobileNumberNullException;
import com.parserlabs.phr.exception.PHRStatusNotActive;
import com.parserlabs.phr.exception.PhrIdNotMappedToMobileException;
import com.parserlabs.phr.exception.TransactionNotFoundException;
import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.email.EmailNotificationRequest;
import com.parserlabs.phr.model.email.EmailVerifyAuthTransactionOTP;
import com.parserlabs.phr.model.login.mobileemail.LoginPostVerificationRequest;
import com.parserlabs.phr.model.login.mobileemail.LoginPreVerificationRequest;
import com.parserlabs.phr.model.login.mobileemail.LoginPreVerificationResponse;
import com.parserlabs.phr.model.login.phr.LoginViaMobileEmailRequest;
import com.parserlabs.phr.model.login.phr.LoginViaPhrRequest;
import com.parserlabs.phr.model.login.phr.VerifyPasswordOtpLoginRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.request.SearchByHealthIdNumberRequest;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.model.response.SearchResponsePayLoad;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.service.email.EmailService;
import com.parserlabs.phr.utils.GeneralUtils;
import com.parserlabs.phr.utils.PhrUtilits;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@CustomSpanned
public class LoginService {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private HealthIdNumberLoginService healthIdNumberLoginService;

	/**
	 * PHR login initialize via PHR ADDRESS
	 * 
	 * @param request
	 * @return
	 */
	public TransactionResponse initPhrLogin(LoginViaPhrRequest request) {
		// Check PHR ADDRESS EXIST OR NOT
		userService.doesPhrAddressExist(request.getPhrAddress());
		// check PHR status
		UserDTO user = userService.getUser(request.getPhrAddress());

		if (!user.getStatus().equalsIgnoreCase(AccountStatus.ACTIVE.name())) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("phrAddress").value(request.getPhrAddress())
					.build();
			throw new PHRStatusNotActive(attribute);
		}
		return startPhrAuthTransaction(request);
	}

	/**
	 * PHR login initialize via MOBILE NUMBER
	 * 
	 * @param request
	 * @return
	 */
	public TransactionResponse initMobileEmailLogin(LoginViaMobileEmailRequest request) {
		// Check PHR ADDRESS EXIST OR NOT
		String decryptValue = DecryptRSAUtil.decrypt(request.getInput());
		request.setInput(decryptValue.toLowerCase());

		return startEmailMobileAuthTransaction(request);
	}

	/***
	 * PHR Address Login, Generate auth transaction id
	 * 
	 * @param request
	 * @return
	 */
	private TransactionResponse startPhrAuthTransaction(LoginViaPhrRequest request) {
		// Check PHR ADDRESS
		UserDTO user = userService.getUser(request.getPhrAddress());

		TransactionResponse transactionResponse = null;
		// Check Authentication Methods
		switch (request.getAuthMethod()) {
		case MOBILE_OTP:
			if (StringUtils.hasLength(user.getMobile())) {
				transactionResponse = authenticationService.startPhrMobileLoginAuthTransaction(user, request);
			} else {
				ErrorAttribute attribute = ErrorAttribute.builder().key("phrAddress").value(user.getPhrAddress())
						.build();
				throw new MobileNumberNullException("Mobile number is blank against {} ", attribute);
			}

			break;
		case EMAIL_OTP:
			EmailVerifyAuthTransactionOTP authTransaction = authenticationService
					.startPhrEmailLoginAuthTransaction(user, request);
			EmailNotificationRequest emailVerifyTxnIdOTP = EmailNotificationRequest.builder()
					.transactionId(authTransaction.getPhrAuthTransactionEntity().getAuthTransactionId().toString())
					.opType(OperationTypeEmail.LOGIN_OTP).email(user.getEmail().toLowerCase()).name(user.getFullName())
					.OTP(authTransaction.getOTP()).build();
			transactionResponse = emailService.sendLoginOtp(emailVerifyTxnIdOTP);
			break;
		case PASSWORD:
			transactionResponse = authenticationService.startPasswordLoginAuthTransaction(user, request);
			break;
		default:
			ErrorAttribute attribute = ErrorAttribute.builder().key("authMethod").value(request.getAuthMethod().name())
					.build();
			throw new LoginMethodNotAvailableException(
					String.format("Invalid Auth method for the PHR ADDRESS '%s'", request.getPhrAddress()), attribute);
		}
		return transactionResponse;
	}

	/***
	 * PHR Address Login, Generate auth transaction id
	 * 
	 * @param request
	 * @return
	 */
	private TransactionResponse startEmailMobileAuthTransaction(LoginViaMobileEmailRequest request) {

		TransactionResponse transactionResponse = null;
		// Check Authentication Methods
		if (PhrUtilits.isValidMobile(request.getInput())) {
			log.info("start checking mobile no : {} ",request.getInput());
			userService.doesPhrAddressExistByMobileAndMobileVerified(request.getInput(), true);
			return authenticationService.startMobileLoginAuthTransaction(request);
		} else if (PhrUtilits.isValidEmailAddress(request.getInput())) {
			log.info("start checking email : {} ",request.getInput());
			userService.doesPhrAddressExistByEmailAndEmailVerified(request.getInput(), true);
			EmailVerifyAuthTransactionOTP authTransaction = authenticationService
					.startEmailLoginAuthTransaction(request);
			EmailNotificationRequest emailVerifyTxnIdOTP = EmailNotificationRequest.builder()
					.transactionId(authTransaction.getPhrAuthTransactionEntity().getAuthTransactionId().toString())
					.opType(OperationTypeEmail.LOGIN_OTP).email(request.getInput()).OTP(authTransaction.getOTP())
					.build();
			transactionResponse = emailService.sendLoginOtp(emailVerifyTxnIdOTP);
		} else {
			throw new LoginMethodNotAvailableException(String.format("Invalid Request method for the PHR ADDRESS"));
		}
		return transactionResponse;
	}

	/**
	 * Method to verify the Login credentials.
	 * 
	 * @param request
	 * @return JWT token response
	 */
	public JwtResponse verifyCredentailsLogin(VerifyPasswordOtpLoginRequest request) {
		PhrAuthTransactionEntity phrAuthTransactionEntity = authenticationService
				.fetchAuthTransactionEntitybyIdStatus(request.getTransactionId(), AuthStatusEnums.ACTIVE.name(),"verifyCredentailsLogin");
		// Decrypt String OTP or Password
		String decrryptedValued = DecryptRSAUtil.decrypt(request.getInput());
		verifyOtpPassword(phrAuthTransactionEntity, decrryptedValued);
		UserDTO userDto=userService.getUser(phrAuthTransactionEntity.getPhrAddress());
		        userDto.setAuthTransactionId(phrAuthTransactionEntity.getAuthTransactionId().toString());
		JwtResponse jwtResponse = jwtTokenUtil.generateTokens(userDto);
		return JwtResponse.builder().token(jwtResponse.getToken()).expiresIn(jwtResponse.getExpiresIn())
				.refreshToken(jwtResponse.getRefreshToken()).refreshExpiresIn(jwtResponse.getRefreshExpiresIn())
				.phrAdress(phrAuthTransactionEntity.getPhrAddress())
				.firstName(userDto.getFirstName())
				.authTs(userService.encryptedAuthTs(PhrUtilits.getCurrentTimeStamp())).build();
	}

	/**
	 * Method to verify the Login credentials.
	 * 
	 * @param request
	 * @return LoginMobilePreResponse
	 */
	public LoginPreVerificationResponse preVerifyMobileEmailLogin(LoginPreVerificationRequest request) {
		PhrAuthTransactionEntity authEntity = authenticationService
				.fetchAuthTransactionEntitybyIdStatus(request.getTransactionId(), AuthStatusEnums.PRE_OTP.name(),"preVerifyMobileEmailLogin");
		// Decrypt String OTP or Password
		String decryptedValue = DecryptRSAUtil.decrypt(request.getOtp());

		Set<String> mappedPHRAddress = null;

		if (StringUtils.hasLength(authEntity.getHealthIdNumber())) {
			// Return transaction Id and list of PHR address.
			mappedPHRAddress = healthIdNumberLoginService.confirm(decryptedValue, authEntity);
		} else {
			authenticationService.verifyOtpEmailMobile(authEntity, decryptedValue);
			// Return transaction Id and list of PHR address.
			if (authEntity.getAuthMethod().equals(LoginMethodsEnum.MOBILE_OTP.name())) {
				mappedPHRAddress = userService.getMappedPhrAddressByMobile(authEntity.getMobile(), true);
			} else {
				mappedPHRAddress = userService.getMappedPhrAddressByEmail(authEntity.getEmail(), true);
			}
		}

		return LoginPreVerificationResponse.builder().transactionId(request.getTransactionId())
				.mobileEmail(authEntity.getMobile()).mappedPhrAddress(mappedPHRAddress).build();
	}

	public SearchResponsePayLoad featchAuthMethod(SearchByHealthIdNumberRequest searchByHealthIdNumberRequest) {
		return healthIdNumberLoginService.featchAuthMethod(searchByHealthIdNumberRequest);
	}

	/**
	 * Method to verify the Login credentials.
	 * 
	 * @param request
	 * @return JWT token response
	 */
	public JwtResponse postVerficationLogin(LoginPostVerificationRequest request) {
		PhrAuthTransactionEntity authTransaction = authenticationService
				.fetchAuthTransactionEntitybyIdStatus(request.getTransactionId(), AuthStatusEnums.OTP_VERIFIED.name(),"postVerficationLogin");

		Set<String> phraddress = null;
		if (StringUtils.hasLength(authTransaction.getHealthIdNumber())) {
			phraddress = userService.fetchPhrAddress(authTransaction.getHealthIdNumber());
		} else if (StringUtils.hasLength(authTransaction.getMobile())) {
			phraddress = userService.getMappedPhrAddressByMobile(authTransaction.getMobile(), true);
		} else if (StringUtils.hasLength(authTransaction.getEmail())) {
			phraddress = userService.getMappedPhrAddressByEmail(authTransaction.getEmail(), true);
		}
		// Decrypt String OTP or Password
		if (phraddress.contains(request.getPhrAddress())) {
			authTransaction.setStatus(AuthStatusEnums.DONE.name());
			authTransaction.setPhrAddress(GeneralUtils.sanetizePhrAddress(request.getPhrAddress()));
			authenticationService.saveAuthTransaction(authTransaction);

			UserDTO userDto=userService.getUser(authTransaction.getPhrAddress());
			        userDto.setAuthTransactionId(authTransaction.getAuthTransactionId().toString());
			
			JwtResponse jwtResponse = jwtTokenUtil.generateTokens(userDto);
			return JwtResponse.builder().token(jwtResponse.getToken()).expiresIn(jwtResponse.getExpiresIn())
					.refreshToken(jwtResponse.getRefreshToken()).refreshExpiresIn(jwtResponse.getRefreshExpiresIn())
					.phrAdress(authTransaction.getPhrAddress())
					.firstName(userDto.getFirstName())
					.authTs(userService.encryptedAuthTs(PhrUtilits.getCurrentTimeStamp())).build();
		} else {
			ErrorAttribute attribute = ErrorAttribute.builder().key("phrAddress").value(request.getPhrAddress())
					.build();
			throw new PhrIdNotMappedToMobileException("Not mapped with requested mobile", attribute);
		}
	}

	private void verifyOtpPassword(PhrAuthTransactionEntity phrAuthTransactionEntity, String otpPassword) {
		// Verify the OTP or password
		if (phrAuthTransactionEntity.getAuthMethod().equalsIgnoreCase(LoginMethodsEnum.PASSWORD.name())) {
			authenticationService.verifyPassword(phrAuthTransactionEntity, otpPassword);
		} else {
			authenticationService.verifyOtp(phrAuthTransactionEntity, otpPassword);
		}
	}

	// OTP RESEND
	public TransactionResponse regenerateAuthOTP(ResendOTPRequest request) {
		PhrAuthTransactionEntity phrAuthTransactionEntity = authenticationService
				.fetchAuthTransactionEntitybyId(request.getTransactionId(),"regenerateAuthOTP");

		List<String> authMethodsAllowedResendOTP = Arrays.asList("ACTIVE", "PRE_OTP");
		TransactionResponse transactionResponse = null;

		if (!authMethodsAllowedResendOTP.contains(phrAuthTransactionEntity.getStatus())) {
			throw new TransactionNotFoundException(String.format("Transaction is not found for %s UUID",
					phrAuthTransactionEntity.getAuthTransactionId()));
		}

		if (phrAuthTransactionEntity.getAuthMethod().equals(LoginMethodsEnum.MOBILE_OTP.name())) {
			// For Mobile Flow
			if (StringUtils.hasLength(phrAuthTransactionEntity.getMobile())) {
				transactionResponse = authenticationService
						.startResendOtpLoginAuthTransaction(phrAuthTransactionEntity);
			} else {
				ErrorAttribute attribute = ErrorAttribute.builder().key("phrAddress")
						.value(phrAuthTransactionEntity.getPhrAddress()).build();
				throw new MobileNumberNullException("mobile number is blank against {} ", attribute);
			}

		} else if (phrAuthTransactionEntity.getAuthMethod().equals(LoginMethodsEnum.EMAIL_OTP.name())) {
			// For Email Flow
			EmailVerifyAuthTransactionOTP authTransaction = authenticationService
					.startResendOtpEmailLoginAuthTransaction(phrAuthTransactionEntity);
			EmailNotificationRequest emailVerifyTxnIdOTP = EmailNotificationRequest.builder()
					.transactionId(authTransaction.getPhrAuthTransactionEntity().getAuthTransactionId().toString())
					.opType(OperationTypeEmail.LOGIN_OTP).email(phrAuthTransactionEntity.getEmail())
					.OTP(authTransaction.getOTP()).build();
			transactionResponse = emailService.sendLoginOtp(emailVerifyTxnIdOTP);
		} else {
			throw new LoginMethodNotAvailableException("Invalid Auth method for the PHR ADDRESS '%s'");
		}
		return transactionResponse;

	}

}
