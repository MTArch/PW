/**
 * 
 */
package com.parserlabs.phr.service;

import static com.parserlabs.phr.utils.PhrCardHelper.*;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;
import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.entity.PhrAuthMethodEntity;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.entity.PhrUserEntity;
import com.parserlabs.phr.enums.AuthMethods;
import com.parserlabs.phr.enums.AuthStatusEnums;
import com.parserlabs.phr.enums.ImageTypeEnum;
import com.parserlabs.phr.enums.RedisRequestSalt;
import com.parserlabs.phr.exception.MobileNotVerifiedException;
import com.parserlabs.phr.exception.OldAndNewValueSameException;
import com.parserlabs.phr.exception.PasswordSameAsPreviousException;
import com.parserlabs.phr.exception.UserNotFoundException;
import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.face.FaceResponse;
import com.parserlabs.phr.model.face.FaceValidationRequest;
import com.parserlabs.phr.model.face.FaceValidationResponse;
import com.parserlabs.phr.model.profile.ProfileEditRequest;
import com.parserlabs.phr.model.profile.ProfilePasswordUpdateRequest;
import com.parserlabs.phr.model.profile.ProfilePasswordUpdateRequestFromHid;
import com.parserlabs.phr.model.profile.UpdateProfileRequest;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.registration.VerifyOTPRequest;
import com.parserlabs.phr.model.request.UpdatePhrAttributePayLoad;
import com.parserlabs.phr.model.response.SuccessResponse;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.repository.UserRepository;
import com.parserlabs.phr.security.RequestFlooding;
import com.parserlabs.phr.service.email.EmailService;
import com.parserlabs.phr.transform.TransfromProfileEditData;
import com.parserlabs.phr.utils.GeneralUtils;
import com.parserlabs.phr.utils.ImageSizeUtils;
import com.parserlabs.phr.utils.OpenImajUtility;
import com.parserlabs.phr.utils.PhrCardHelper;
import com.parserlabs.phr.utils.PhrUtilits;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@Service
@Slf4j
@CustomSpanned
public class ProfileEditService {

	private int[] haarCascadeSizeValue = { 100, 130 };

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransfromProfileEditData transfromProfileEditData;

	@Autowired
	private RequestFlooding flooding;

	@Autowired
	private OtpPasswordService otpPasswordService;

	@Autowired
	private S3StorageService s3StorageService;

	/***
	 * Generate the OTP on the Mobile Number for the PHR update profile
	 * 
	 * @param request
	 * @return TransactionResponse
	 */
	public TransactionResponse generateOTP(GenerateOTPRequest request) {

		// De-crypt the Request encrypted data
		String decryptedValue = DecryptRSAUtil.decrypt(request.getValue());
		GenerateOTPRequest genOtpRequest = GenerateOTPRequest.builder().value(decryptedValue).build();
		if (PhrUtilits.isValidMobile(decryptedValue)) {
			UserDTO user = userService.get();
			if (Objects.nonNull(user.getMobile()) && user.getMobile().equals(genOtpRequest.getValue())
					&& user.isMobileVerified()) {
				ErrorAttribute error = ErrorAttribute.builder().key("input").value("Mobile Number").build();
				throw new OldAndNewValueSameException("Old and new are same.", error);
			}
			return transactionService.startMobileOTPTransactionForProfileUpdate(genOtpRequest);// Mobile Update Flow
		} else if (PhrUtilits.isValidEmailAddress(decryptedValue)) {
			UserDTO user = userService.get();
			if (!StringUtils.isEmpty(user.getEmail()) && user.getEmail().equals(genOtpRequest.getValue())
					&& user.isEmailVerified()) {

				ErrorAttribute error = ErrorAttribute.builder().key("input").value("Email Address").build();
				throw new OldAndNewValueSameException("Old and new are same.", error);
			}
			return emailService.sendAccountUpdateOtp(genOtpRequest, user.getFullName()); // Email Update Flow
		} else {
			throw new MobileNotVerifiedException("$$Invalid Request. Please try with valid mobile/email address.");
		}
	}

	/**
	 * Verify the Mobile/Email OTP
	 * 
	 * @param request
	 * @return TransactionResponse
	 */
	public TransactionResponse verifyOTP(VerifyOTPRequest request) {
		// get the transaction Entity details.
		transactionService.verifyOtp(transactionService.findTransaction(request.getTransactionId().toString()),
				DecryptRSAUtil.decrypt(request.getOtp()), AuthStatusEnums.UPDATE_OTP_VERIFIED.name());
		return TransactionResponse.builder().transactionId(request.getTransactionId().toString()).build();
	}

	public SuccessResponse updateMobileEmail(UpdateProfileRequest request) {

		// check request flooding
		flooding.checkProfileEditFlooding(PHRContextHolder.phrAddress(), RedisRequestSalt.PROFILE_EDIT_CHECK.name());
		Boolean isUpdated = false;
		PhrTransactionEntity transactionEntity = transactionService.findTransaction(
				request.getTransactionId().toString(), AuthStatusEnums.UPDATE_OTP_VERIFIED.name(), "updateMobileEmail");
		if (StringUtils.isNotBlank(transactionEntity.getMobile())) {
			isUpdated = updateMobileNumber(transactionEntity);
		}
		if (StringUtils.isNotBlank(transactionEntity.getEmail())) {
			isUpdated = updateEmailAddress(transactionEntity);
		}
		return SuccessResponse.builder().success(isUpdated).build();
	}

	/**
	 * Update Mobile Number
	 * 
	 * @param request
	 * @return
	 */
	private Boolean updateMobileNumber(PhrTransactionEntity transactionEntity) {
		userService.updateMobileNumber(transactionEntity);
		// Delete the transaction number
		transactionService.deleteTransaction(transactionEntity.getTransactionId());
		return true;
	}

	/**
	 * update email address
	 * 
	 * @param request
	 * @return
	 */
	private Boolean updateEmailAddress(PhrTransactionEntity transactionEntity) {
		userService.updateEmailAddress(transactionEntity);
		// Delete the transaction number
		transactionService.deleteTransaction(transactionEntity.getTransactionId());
		return true;
	}

	/**
	 * Re-send the Mobile/EMAIL OTP
	 * 
	 * @param request
	 * @return
	 */
	public SuccessResponse resendOTP(ResendOTPRequest request) {
		Boolean isSend = false;
		PhrTransactionEntity txn = transactionService.findTransaction(request.getTransactionId());
		if (StringUtils.isNotBlank(txn.getMobile())) {
			isSend = transactionService.resendVerificationOtp(UUID.fromString(request.getTransactionId()));
		}
		if (StringUtils.isNotBlank(txn.getEmail())) {
			isSend = emailService.resendRegistrationOtp(request);
		}
		return SuccessResponse.builder().success(isSend).build();
	}

	public SuccessResponse profileEdit(ProfileEditRequest request) {
		// check request flooding
		boolean isPhotoEncoded = false;
		flooding.checkProfileEditFlooding(PHRContextHolder.phrAddress(), RedisRequestSalt.PROFILE_EDIT_CHECK.name());

		// Validate the Profile Image size
		if (Objects.nonNull(request.getProfilePhoto()) && request.getProfilePhoto().length > 0) {
			log.info("request.getProfilePhoto() : {}, Profile length : {} ", request.getProfilePhoto(),
					request.getProfilePhoto().length);
			ImageSizeUtils.imageValidation(request.getProfilePhoto(), ImageTypeEnum.PROFILE_IMAGE);
		}

		PhrUserEntity user = userService.getPhrEntity();
		user = transfromProfileEditData.getUpdatePhrUserEntity(user, request);

		if (Objects.nonNull(request.getProfilePhoto()) && request.getProfilePhoto().length > 0) {
			log.info("request.getProfilePhoto() : {}, Profile length  check: {} ", request.getProfilePhoto(),
					request.getProfilePhoto().length);
			//user.setProfilePhoto(Base64.getEncoder().encodeToString(request.getProfilePhoto()));
			String uploadKey = s3StorageService.setProfilePhoto(String.valueOf(user.getId()),
					request.getProfilePhoto(),isPhotoEncoded);
			if (uploadKey == null) {
				return SuccessResponse.builder().success(false).build();
			}
			user.setProfilePhoto(null);
			user.setProfilePhotoCompressed(Boolean.FALSE);
		}
		// Update to table
		userService.save(user);
		return SuccessResponse.builder().success(true).build();
	}

	public SuccessResponse profilePasswordEdit(ProfilePasswordUpdateRequest request) {
		// check request flooding
//		flooding.checkProfileEditFlooding(PHRContextHolder.phrAddress(),
//				RedisRequestSalt.PROFILE_EDIT_PWD_CHECK.name());

		String decryptedValue = DecryptRSAUtil.decrypt(request.getPassword());
		PhrUserEntity user = userService.getPhrEntity();
		if (StringUtils.isNotBlank(user.getPassword()) && !user.getPassword().equalsIgnoreCase("null")) {
			if (StringUtils.isNotBlank(request.getOldPassword())) {
				checkOldPassword(request.getOldPassword(), user.getPassword());
			}
			Boolean isPwdSame = otpPasswordService.isPasswordSame(user.getPassword(), decryptedValue);
			if (isPwdSame) {
				ErrorAttribute errorAttribute = ErrorAttribute.builder().key("password")
						.value(PhrUtilits.maskedData(decryptedValue, "*")).build();
				throw new PasswordSameAsPreviousException("Old and New Passwords are same", errorAttribute);
			}
			
		}
		
		user.setPassword(PhrUtilits.getEncodedPassword(decryptedValue)); // SET the new password
		user.setPhrAuthMethodEntity(authMethod(user, AuthMethods.PASSWORD.name()));
		
		// Update to table
		userService.save(user);
		return SuccessResponse.builder().success(true).build();

	}

	private Set<PhrAuthMethodEntity> authMethod(PhrUserEntity phrUserEntity, String authMedhod) {
		Set<PhrAuthMethodEntity> authMethods = phrUserEntity.getPhrAuthMethodEntity();
		long count = authMethods.stream().filter(authodMethod -> authodMethod.getAuthMethod().equals(authMedhod))
				.count();
		if (count == 0) {
			Set<PhrAuthMethodEntity> phrAuthMethodEntities = new HashSet<>(phrUserEntity.getPhrAuthMethodEntity());
			authMethods = new HashSet<>(phrUserEntity.getPhrAuthMethodEntity());
			PhrAuthMethodEntity passwordAuthMethod = PhrAuthMethodEntity.builder().authMethod(authMedhod)
					.user(phrUserEntity).build();
			phrAuthMethodEntities.add(passwordAuthMethod);
			authMethods.add(passwordAuthMethod);
		}
		return authMethods;
	}

	public SuccessResponse profilePasswordEditFromHid(ProfilePasswordUpdateRequestFromHid request) {
		// check request flooding
//		flooding.checkProfileEditFlooding(PHRContextHolder.phrAddress(),
//				RedisRequestSalt.PROFILE_EDIT_PWD_CHECK.name());

		String password = request.getPassword();
		PhrUserEntity user = userRepository.findByPhrAddress(GeneralUtils.sanetizePhrAddress(request.getPhrAddress()))
				.orElseThrow(UserNotFoundException::new);

		if (StringUtils.isNotBlank(user.getPassword())) {
			Boolean isPwdSame = otpPasswordService.isPasswordSame(user.getPassword(), password);
			if (isPwdSame) {
				ErrorAttribute errorAttribute = ErrorAttribute.builder().key("password")
						.value(PhrUtilits.maskedData(password, "*")).build();
				throw new PasswordSameAsPreviousException("Old and New Passwords are same", errorAttribute);
			}
			user.setPassword(PhrUtilits.getEncodedPassword(password)); // SET the new password
			boolean ifAuthMethodPresent = user.getPhrAuthMethodEntity().stream()
					.anyMatch(authObj -> authObj.getAuthMethod().equalsIgnoreCase(AuthMethods.PASSWORD.name()));

			if (!ifAuthMethodPresent) {
				user.getPhrAuthMethodEntity()
						.add(PhrAuthMethodEntity.builder().authMethod(AuthMethods.PASSWORD.name()).user(user).build());
			}
		} else {
			user.setPassword(PhrUtilits.getEncodedPassword(password)); // SET the new password
			user.getPhrAuthMethodEntity()
					.add(PhrAuthMethodEntity.builder().authMethod(AuthMethods.PASSWORD.name()).user(user).build());
		}
		// Update to table
		userService.save(user);
		return SuccessResponse.builder().success(true).build();
	}

	public byte[] getQrCodeOfAccount() {
		byte[] qrCodeByte = null;
		try {
			qrCodeByte = populateByteArray(PhrCardHelper.getQrCodeOfUserWithLogo(userService.get())).getByteArray();
		} catch (WriterException e) {
			log.error("WriterException occured:", e);
		} catch (IOException e) {
			log.error("IOException occured:", e);
		}
		return qrCodeByte;
	}

	public Boolean changePhrAttribute(UpdatePhrAttributePayLoad updatePhrAttributePayLoad) {
		return userService.update(updatePhrAttributePayLoad);
	}

	private boolean checkOldPassword(String givenPassword, String dbPassword) {

		String decryptedValue = DecryptRSAUtil.decrypt(givenPassword);
		if (!otpPasswordService.isPasswordSame(dbPassword, decryptedValue)) {
			ErrorAttribute errorAttribute = ErrorAttribute.builder().key("password")
					.value(PhrUtilits.maskedData(decryptedValue, "*")).build();
			throw new PasswordSameAsPreviousException("Old and given Passwords are not same", errorAttribute);
		}
		return true;
	}

	/***
	 * 
	 * @param request
	 * @return
	 */

	public FaceValidationResponse validateFaceInPhoto(FaceValidationRequest request) {
		return validateFaceInPhoto(request.getProfilePhoto());
	}

	private FaceValidationResponse validateFaceInPhoto(Object request) {
		FaceResponse faceResponse = OpenImajUtility.isFaceFoundProfile(request, haarCascadeSizeValue[0]);
		if (!faceResponse.isFaceFound()) {
			faceResponse = OpenImajUtility.isFaceFoundProfile(request, haarCascadeSizeValue[1]);
		}
		return FaceValidationResponse.builder().faceFound(faceResponse.isFaceFound())
				.noOfFaces(faceResponse.getNoOfFaces()).build();
	}

}
