package com.parserlabs.phr.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrAuthMethodEntity;
import com.parserlabs.phr.entity.PhrAuthTransactionEntity;
import com.parserlabs.phr.entity.PhrUserEntity;
import com.parserlabs.phr.enums.AccountStatus;
import com.parserlabs.phr.enums.AuthMethods;
import com.parserlabs.phr.enums.AuthStatusEnums;
import com.parserlabs.phr.enums.LoginMethodsEnum;
import com.parserlabs.phr.enums.OperationTypeEmail;
import com.parserlabs.phr.exception.AbhaAddressMobileOrEmailNotExistException;
import com.parserlabs.phr.exception.LoginMethodNotAvailableException;
import com.parserlabs.phr.exception.PHRStatusNotActive;
import com.parserlabs.phr.exception.SystemException;
import com.parserlabs.phr.exception.TransactionNotFoundException;
import com.parserlabs.phr.exception.UserNotFoundException;
import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.email.EmailNotificationRequest;
import com.parserlabs.phr.model.email.EmailVerifyAuthTransactionOTP;
import com.parserlabs.phr.model.login.phr.LoginViaPhrRequest;
import com.parserlabs.phr.model.request.ForgetPasswordRequest;
import com.parserlabs.phr.model.request.ForgetPasswordVerifyOtp;
import com.parserlabs.phr.model.request.GenerateForgetPassOtpRequest;
import com.parserlabs.phr.model.response.ForgetPassGenOtpTransResponse;
import com.parserlabs.phr.model.response.ForgetPassVerifyOtpResponse;
import com.parserlabs.phr.model.response.SuccessResponse;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.repository.AuthTransactionRepository;
import com.parserlabs.phr.repository.UserRepository;
import com.parserlabs.phr.service.email.EmailService;
import com.parserlabs.phr.utils.GeneralUtils;
import com.parserlabs.phr.utils.PHRIdUtils;
import com.parserlabs.phr.utils.PhrUtilits;

import lombok.AllArgsConstructor;

@Service
@CustomSpanned
@AllArgsConstructor
public class ForgetPasswordService {

	private final UserService userService;

	private EmailService emailService;

	private AuthenticationService authenticationService;

	private UserRepository userRepository;

	private AuthTransactionRepository authTransactionRepository;

	public ForgetPassGenOtpTransResponse sendOtpBasedOnMobileUsingPhr(
			GenerateForgetPassOtpRequest generateForgetPassOtpReq) {
		userService.doesPhrAddressExist(generateForgetPassOtpReq.getAbhaAddress());
		UserDTO user = userService.getUser(generateForgetPassOtpReq.getAbhaAddress());
		if (!user.getStatus().equalsIgnoreCase(AccountStatus.ACTIVE.name())) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("phrAddress")
					.value(generateForgetPassOtpReq.getAbhaAddress()).build();
			throw new PHRStatusNotActive(attribute);
		}
		ForgetPassGenOtpTransResponse forgetPassGeneratOtpRes = null;

		// Check Authentication Methods
		if (generateForgetPassOtpReq.getAuthMode().contains(AuthMethods.MOBILE_OTP.name())) {
			if (StringUtils.hasLength(user.getMobile())) {
				LoginViaPhrRequest request = LoginViaPhrRequest.builder()
						.phrAddress(generateForgetPassOtpReq.getAbhaAddress()).authMethod(LoginMethodsEnum.MOBILE_OTP)
						.build();
				TransactionResponse transactionResponse = authenticationService.startPhrMobileLoginAuthTransaction(user,
						request);
				forgetPassGeneratOtpRes = ForgetPassGenOtpTransResponse.builder()
						.transactionId(transactionResponse.getTransactionId())
						.authMode(generateForgetPassOtpReq.getAuthMode()).mobileEmail(PHRIdUtils.maskedHealthCareProfessionalID(user.getMobile(), "*", 6, false)).build();
			} else {
				throw new AbhaAddressMobileOrEmailNotExistException("Abha Address does not have mobile number");
			}
		} else if (generateForgetPassOtpReq.getAuthMode().contains(AuthMethods.EMAIL_OTP.name())) {
			if (StringUtils.hasLength(user.getEmail())) {
				LoginViaPhrRequest request = LoginViaPhrRequest.builder()
						.phrAddress(generateForgetPassOtpReq.getAbhaAddress()).authMethod(LoginMethodsEnum.EMAIL_OTP)
						.build();
				EmailVerifyAuthTransactionOTP authTransaction = authenticationService
						.startPhrEmailLoginAuthTransaction(user, request);
				EmailNotificationRequest emailVerifyTxnIdOTP = EmailNotificationRequest.builder()
						.transactionId(authTransaction.getPhrAuthTransactionEntity().getAuthTransactionId().toString())
						.opType(OperationTypeEmail.LOGIN_OTP).email(user.getEmail().toLowerCase())
						.name(user.getFullName()).OTP(authTransaction.getOTP()).build();
				TransactionResponse transactionResponse = emailService.sendLoginOtp(emailVerifyTxnIdOTP);
				forgetPassGeneratOtpRes = ForgetPassGenOtpTransResponse.builder()
						.transactionId(transactionResponse.getTransactionId())
						.authMode(generateForgetPassOtpReq.getAuthMode()).mobileEmail(PHRIdUtils.maskedHealthCareProfessionalID(user.getEmail(), "*", 6, false)).build();
			} else {
				throw new AbhaAddressMobileOrEmailNotExistException("Abha Address does not have email  address");
			}

		} else {
			throw new LoginMethodNotAvailableException("Auth method not preset in this ABHA Address");
		}
		return forgetPassGeneratOtpRes;
	}

	public ForgetPassVerifyOtpResponse forgetPasswordVerifyOtp(ForgetPasswordVerifyOtp forgetPassVerifyOtp) {
		PhrAuthTransactionEntity phrAuthTransactionEntity = authenticationService.fetchAuthTransactionEntitybyIdStatus(
				forgetPassVerifyOtp.getTransactionId(), AuthStatusEnums.ACTIVE.name(), "forgetPasswordVerifyOtp");
		// Decrypt String OTP
		String decrryptedValued = DecryptRSAUtil.decrypt(forgetPassVerifyOtp.getValue());

		authenticationService.verifyOtp(phrAuthTransactionEntity, decrryptedValued);

		return ForgetPassVerifyOtpResponse.builder().abhaAddress(phrAuthTransactionEntity.getPhrAddress())
				.transactionId(phrAuthTransactionEntity.getAuthTransactionId().toString()).build();

	}

	public SuccessResponse forgetPasswordAndSetNewPassword(ForgetPasswordRequest forgetPasswordRequest) {
//	PhrAuthTransactionEntity phrAuthTransactionEntity = authenticationService.fetchAuthTransactionEntitybyIdStatus(
//				forgetPasswordRequest.getTransactionId(), AuthStatusEnums.DONE.name(),
//			"forgetPasswordAndSetNewPassword");
		PhrAuthTransactionEntity phrAuthTransactionEntity = null;
		try {
			phrAuthTransactionEntity = authTransactionRepository
					.findByAuthTransactionIdAndStatus(UUID.fromString(forgetPasswordRequest.getTransactionId()),
							AuthStatusEnums.DONE.name())
					.orElse(null);
		} catch (Exception exp) {
			throw new SystemException(String.format("Exception occured while fetching Auth Transaction for UUID '%s'",
					forgetPasswordRequest.getTransactionId()), exp);
		}
		if (Objects.isNull(phrAuthTransactionEntity)) {
			throw new TransactionNotFoundException(String.format(
					"forgetPasswordAndSetNewPassword  Transaction is not found for %s UUID and %s method ",
					forgetPasswordRequest.getTransactionId(), "", "forgetPasswordAndSetNewPassword"));
		}

		String decryptedNewPasswordValue = DecryptRSAUtil.decrypt(forgetPasswordRequest.getNewPassword());
		String decryptedConfirmPasswordValue = DecryptRSAUtil.decrypt(forgetPasswordRequest.getConfirmPassword());
		if (decryptedNewPasswordValue.equals(decryptedConfirmPasswordValue)) {
			PhrUserEntity user = userRepository
					.findByPhrAddress(GeneralUtils.sanetizePhrAddress(phrAuthTransactionEntity.getPhrAddress()))
					.orElseThrow(UserNotFoundException::new);
			user.setPassword(PhrUtilits.getEncodedPassword(decryptedNewPasswordValue)); // SET the new password

			boolean passAuthMethodPresent = user.getPhrAuthMethodEntity().stream()
					.filter(authmethod -> authmethod.getAuthMethod().equals(AuthMethods.PASSWORD.name())).findFirst()
					.isPresent();

			if (!passAuthMethodPresent) {
				user.setPhrAuthMethodEntity(authMethod(user, AuthMethods.PASSWORD.name()));
			}
			// Update to table
			userService.save(user);
			return SuccessResponse.builder().success(true).build();
		} else {
			return SuccessResponse.builder().success(false).build();
		}
	}
	
	private Set<PhrAuthMethodEntity> authMethod(PhrUserEntity phrUserEntity, String authMedhod) {
		Set<PhrAuthMethodEntity> phrAuthMethodEntities = new HashSet<>(phrUserEntity.getPhrAuthMethodEntity());
		PhrAuthMethodEntity passwordAuthMethod = PhrAuthMethodEntity.builder().authMethod(authMedhod)
				.user(phrUserEntity).build();
		phrAuthMethodEntities.add(passwordAuthMethod);
		return phrAuthMethodEntities;
	}

}
