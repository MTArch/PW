package com.parserlabs.phr.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.config.security.JwtTokenUtil;
import com.parserlabs.phr.entity.PhrErrorEntity;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.entity.PhrUserEntity;
import com.parserlabs.phr.enums.AccountAction;
import com.parserlabs.phr.enums.AuthStatusEnums;
import com.parserlabs.phr.enums.OperationTypeEmail;
import com.parserlabs.phr.enums.RedisRequestSalt;
import com.parserlabs.phr.enums.SMSTypeEnums;
import com.parserlabs.phr.exception.AuthenticationException;
import com.parserlabs.phr.exception.MobileNotVerifiedException;
import com.parserlabs.phr.exception.PhrAddressDuplicateException;
import com.parserlabs.phr.exception.PhrAddressNotValidException;
import com.parserlabs.phr.exception.UserNotFoundException;
import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.email.EmailNotificationRequest;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.registration.VerifyOTPRequest;
import com.parserlabs.phr.model.request.CreatePHRFromMobileRequest;
import com.parserlabs.phr.model.request.CreatePHRRequest;
import com.parserlabs.phr.model.request.PHRSuggestionRequst;
import com.parserlabs.phr.model.request.RegistrationByMobileOrEmailRequest;
import com.parserlabs.phr.model.request.UpdatePhrProfilePhoto;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.model.response.SuccessResponse;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.model.response.TransactionWithPHRResponse;
import com.parserlabs.phr.repository.PhrErrorRepository;
import com.parserlabs.phr.security.RequestFlooding;
import com.parserlabs.phr.service.email.EmailService;
import com.parserlabs.phr.service.sms.NicSmsService;
import com.parserlabs.phr.transform.TransformEntity;
import com.parserlabs.phr.transform.TransformRegistrationDataToUserData;
import com.parserlabs.phr.utils.GeneralUtils;
import com.parserlabs.phr.utils.PhrUtilits;
import com.parserlabs.phr.validator.DataValidator;
import com.parserlabs.phr.validator.PhrValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@CustomSpanned
@Slf4j
public class RegistrationByMobileOrEmailService {

	@Value("${phr.sugg.count: 3}")
	public long PHR_SUGG_COUNT;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private EmailService emailService;

	@Autowired
	private TransformEntity transformEntity;

	@Autowired
	private TransformRegistrationDataToUserData transformToUserData;

	@Autowired
	private DataValidator dataValidator;

	@Autowired
	private RequestFlooding otpFlooding;

	@Autowired
	private NicSmsService nicSmsService;

	@Autowired
	private PhrValidator phrValidator;

	@Autowired
	private PhrErrorRepository phrErrorRepo;

	@Autowired
	private S3StorageService s3StorageService;

	@Value("${jwt.user.token.validity.sec: 7200}") // Defaults to 120 MIN
	public long JWT_USER_TOKEN_VALIDITY_IN_SEC;

	@Value("${jwt.user.refresh.token.validity.sec: 432000}") // Defaults to 5 days
	public long JWT_USER_REFRESH_TOKEN_VALIDITY_IN_SEC;

	/***
	 * Generate the OTP on the Mobile Number for the PHR Registration
	 * 
	 * @param request
	 * @return TransactionResponse
	 */
	public TransactionResponse generateOTP(GenerateOTPRequest request) {
		// De-crypt the Request encrypted data
		String decryptedValue = DecryptRSAUtil.decrypt(request.getValue());
		GenerateOTPRequest genOtpRequest = GenerateOTPRequest.builder().value(decryptedValue).build();
		if (PhrUtilits.isValidMobile(decryptedValue)) {
			return transactionService.startMobileOTPTransaction(genOtpRequest);// Mobile Flow
		} else if (PhrUtilits.isValidEmailAddress(decryptedValue)) {
			return emailService.sendRegistrationOtp(genOtpRequest, ""); // Email Flow
		} else {
			throw new MobileNotVerifiedException("$$Invalid Request. Please try with valid mobile/email address.");
		}
	}

	/**
	 * Verify the Mobile OTP
	 * 
	 * @param request
	 * @return TransactionResponse
	 */
	public TransactionWithPHRResponse verifyOTP(VerifyOTPRequest request) {

		PhrTransactionEntity transaction = transactionService.findTransaction(request.getTransactionId(),
				AuthStatusEnums.ACTIVE.name(), "verifyOTP");
		// get the transaction Entity details.
		transactionService.verifyOtp(transaction, DecryptRSAUtil.decrypt(request.getOtp()),
				AuthStatusEnums.OTP_VERIFIED.name());

		Set<String> mappedPhrAddress = null;
		try {
			if (Objects.nonNull(transaction.getMobile())) {
				mappedPhrAddress = userService.getMappedPhrAddressByMobile(transaction.getMobile(), Boolean.TRUE);
			} else if (Objects.nonNull(transaction.getEmail())) {
				mappedPhrAddress = userService.getMappedPhrAddressByEmail(transaction.getEmail(), Boolean.TRUE);
			}
		} catch (Exception e) {
			// Do Nothing
		}
		return TransactionWithPHRResponse.builder().transactionId(request.getTransactionId().toString())
				.mappedPhrAddress(mappedPhrAddress).build();
	}

	/**
	 * Validate the User Data and Save to the transaction table to futher step
	 * 
	 * @param request
	 * @return TransactionResponse
	 */
	public TransactionResponse registerDetails(RegistrationByMobileOrEmailRequest request) {
		// Fetch the transaction number and merge user data
		PhrTransactionEntity phrTransactionEntity = transactionService.findTransaction(request.getTransactionId(),
				AuthStatusEnums.OTP_VERIFIED.name(), "registerDetails");

		// Check for the Request Flooding
		otpFlooding.check(request.getTransactionId(), RedisRequestSalt.REG_DETAILS_PRE.name());

		// Data validation Pre-saving data
		dataValidator.registrationData(phrTransactionEntity, request);

		phrTransactionEntity = transformEntity.populateTransactionEntity(request, phrTransactionEntity);
		// save the tnxEntity Data

		return TransactionResponse.builder()
				.transactionId(transactionService.save(phrTransactionEntity).getTransactionId().toString()).build();

	}

	/***
	 * Create the PHR ID/Account . Push the Data to the User table if valid request
	 * 
	 * @param request
	 * @return TokenResponse
	 */
	public JwtResponse createPHR(CreatePHRRequest request) {
		UserDTO userDto = null;

		PhrTransactionEntity phrTransactionEntity = transactionService.findTransaction(request.getTransactionId());
		phrTransactionEntity.setPhrAddress(GeneralUtils.sanetizePhrAddress(request.getPhrAddress()));

		// CHECK USER EXIST OR NOT, IF exist Mark newPHRCreationFlag 'false'
		boolean newPHRCreationFlag = !userService.isPhrAddressAlreadyExist(request.getPhrAddress());

		boolean isHealthFlow = false;

		if (!StringUtils.isEmpty(phrTransactionEntity.getHealthIdNumber())) {
			isHealthFlow = true; // HealthID-Flow
		}

		log.info("The new PHR creation flow is : {}", newPHRCreationFlag);
		// If the PHR address already created return the token
		if (newPHRCreationFlag) {

			boolean isPhrValid = phrValidator.isValid(request.getPhrAddress());
			if (!isPhrValid) {
				log.info("phr address {} is not valid", request.getPhrAddress());
				throw new PhrAddressNotValidException();
			}
			// Check PHR address exist
			userService.checkPhrAddressExists(request.getPhrAddress());

			UserDTO user = transformToUserData.populateUserData(phrTransactionEntity, request);
			userDto = userService.save(user);
			phrTransactionEntity.setStateName(user.getAddress().getStateName());
			phrTransactionEntity.setDistrictName(user.getAddress().getDistrictName());
			// Send Notification to user email/mobile
			SMSTypeEnums smsType = isHealthFlow ? SMSTypeEnums.REGISTER_SUCCESS_ABHA_NUMBER
					: SMSTypeEnums.REGISTER_SUCCESS_EMAIL_MOBILE;
			sendSuccessNotification(user.getFirstName(), user.getLastName(), user.getPhrAddress(),
					user.getHealthIdNumber(), user.getMobile(), user.getEmail(), smsType);

			if (isHealthFlow) {
				PHRContextHolder.phrAddress(GeneralUtils.sanetizePhrAddress(request.getPhrAddress()));
				try {
					userService.link(AccountAction.LINK.name(), request.getTransactionId().toString(), "CREATE");
				} catch (Exception e) {
					log.info("Error in Linking");
				}
			}
		} else {
			if (!isHealthFlow) { // These validation works for non HID flows
				userDto = userService.getUser(request.getPhrAddress());
				if (StringUtils.isNoneEmpty(phrTransactionEntity.getMobile())
						&& !phrTransactionEntity.getMobile().equalsIgnoreCase(userDto.getMobile())) {
					throw new AuthenticationException(ErrorCode.UNAUTHORIZED);
				}
				if (StringUtils.isNoneEmpty(phrTransactionEntity.getEmail())
						&& !phrTransactionEntity.getEmail().equalsIgnoreCase(userDto.getEmail())) {
					throw new AuthenticationException(ErrorCode.UNAUTHORIZED);
				}
			} else if (isHealthFlow) {
				userDto = userService.getUser(request.getPhrAddress());
			}
		}
		userDto.setAuthTransactionId(phrTransactionEntity.getTransactionId().toString());
		// Delete the transaction number
		// currently commenting the deletion transaction from the DB
		// transactionService.deleteTransaction(phrTransactionEntity.getTransactionId());
		return JwtResponse.builder().token(jwtTokenUtil.generatePhrAddressToken(userDto))
				.expiresIn(JWT_USER_TOKEN_VALIDITY_IN_SEC)
				.refreshToken(jwtTokenUtil.generateRefreshToken(userDto.getPhrAddress()))
				.refreshExpiresIn(JWT_USER_REFRESH_TOKEN_VALIDITY_IN_SEC)
				.phrAdress(GeneralUtils.sanetizePhrAddress(request.getPhrAddress()))
				.authTs(userService.encryptedAuthTs(PhrUtilits.getCurrentTimeStamp())).build();
	}

	/**
	 * This call is for maintaining the backward compatibility for the create PHR
	 * using Mobile
	 * 
	 * @param request
	 * @param migrationFlag
	 * @return
	 */
	public JwtResponse createPHRForHIDMobileRegistration(CreatePHRFromMobileRequest request, boolean migrationFlag) {
		UserDTO user = userService.fetchPhrDetails(GeneralUtils.sanetizePhrAddress(request.getAbhaAddress()));
		if (Objects.nonNull(user)) {
			if (StringUtils.isEmpty(user.getHealthIdNumber()) && (migrationFlag || !request.isUpdateFlag())) {
				throwDuplicatePHRException(user.getPhrAddress());
			}

			if (!request.isUpdateFlag() && !user.getHealthIdNumber().contains(request.getAbhaNumber())) {
				throwDuplicatePHRException(user.getPhrAddress());
			}

//			if (Objects.nonNull(request.getProfilePhoto()) && request.getProfilePhoto().length > 0) {
//				ImageSizeUtils.imageValidation(request.getProfilePhoto(), ImageTypeEnum.PROFILE_IMAGE);
//			}

			PhrUserEntity originalUser = userService.getPhrEntity(request.getAbhaAddress());
			transformToUserData.getUpdatePhrUserEntity(originalUser, request);

			// Update to table
			user = userService.populateUser(userService.save(originalUser));

		} else {
			user = UserDTO.builder().build();
			transformToUserData.createUserPHRFromMobileTo(request, user);
			userService.save(user);
		}

		if (request.isNotify()) {
			sendSuccessNotification(user.getFirstName(), user.getLastName(), user.getPhrAddress(),
					user.getHealthIdNumber(), user.getMobile(), user.getEmail(),
					SMSTypeEnums.REGISTER_SUCCESS_ABHA_NUMBER);
		}
		return JwtResponse.builder().token(jwtTokenUtil.generatePhrAddressToken(user.getPhrAddress()))
				.expiresIn(JWT_USER_TOKEN_VALIDITY_IN_SEC)
				.refreshToken(jwtTokenUtil.generateRefreshToken(user.getPhrAddress()))
				.refreshExpiresIn(JWT_USER_REFRESH_TOKEN_VALIDITY_IN_SEC)
				.phrAdress(GeneralUtils.sanetizePhrAddress(user.getPhrAddress()))
				.authTs(userService.encryptedAuthTs(PhrUtilits.getCurrentTimeStamp())).build();
	}

	/***
	 * Save the Missing PHR , During the HID Creation
	 * 
	 * @param request
	 * @return
	 */
	public PhrUserEntity createPHRMissingRegistration(CreatePHRFromMobileRequest request) {
		PhrUserEntity userEntity = null;
		boolean isPhotoEncoded = false;
		try {
			UserDTO user = UserDTO.builder().build();
			transformToUserData.createUserPHRFromMobileTo(request, user);
			user.setProfilePhoto(null);
			userService.save(user);
			userEntity = userService.getByPhrAddress(GeneralUtils.sanetizePhrAddress(request.getAbhaAddress()))
					.orElseThrow(UserNotFoundException::new);
			if(Objects.nonNull(request.getProfilePhoto()))
			{
				String uploadKey = s3StorageService.setProfilePhoto(String.valueOf(userEntity.getId()),request.getProfilePhoto().getBytes(),isPhotoEncoded);
				log.info("Profile Photo:  ", uploadKey);
			}
		} catch (Exception e) {
			log.error("Exception Occurred [phr missing flow PHR Number {}, Address {}]:  {}", request.getAbhaNumber(),
					request.getAbhaAddress(), e);
			request.setProfilePhoto(null);// Remove the profile pic
			PhrErrorEntity phrError = PhrErrorEntity.builder().healthIdNumber(request.getAbhaNumber())
					.reqPayload(request.toString()).respPayload("createPHRMissingRegistration exp: " + e.toString())
					.createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();
			phrErrorRepo.saveAndFlush(phrError);
		}
		return userEntity;

	}

	public void createPHRUsingKafka(CreatePHRFromMobileRequest request) {
		PhrUserEntity userEntity = null;
		boolean isPhotoEncoded = false;
		boolean isPhrAdressExist = false;
		UserDTO user = userService.fetchPhrDetails(GeneralUtils.sanetizePhrAddress(request.getAbhaAddress()));
		if (Objects.nonNull(user)) {
			PhrUserEntity originalUser = userService.getPhrEntity(request.getAbhaAddress());
			transformToUserData.getUpdatePhrUserEntity(originalUser, request);
			originalUser.setProfilePhoto(null);
			// Update to table
			if(Objects.nonNull(request.getProfilePhoto()))
			{
				String uploadKey = s3StorageService.setProfilePhoto(String.valueOf(originalUser.getId()),request.getProfilePhoto().getBytes(),isPhotoEncoded);
				log.info("Profile Photo:  ", uploadKey);
			}
			user = userService.populateUser(userService.save(originalUser));
			isPhrAdressExist = true;
		} else {
			try {
				user = UserDTO.builder().build();
				transformToUserData.createUserPHRFromMobileTo(request, user);
				userEntity = userService.getByPhrAddress(GeneralUtils.sanetizePhrAddress(request.getAbhaAddress()))
						.orElseThrow(UserNotFoundException::new);
				user.setProfilePhoto(null);
				if(Objects.nonNull(request.getProfilePhoto()))
				{
					String uploadKey = s3StorageService.setProfilePhoto(String.valueOf(userEntity.getId()),request.getProfilePhoto().getBytes(),isPhotoEncoded);
					log.info("Profile Photo:  ", uploadKey);
				}
				userService.save(user);
			} catch (Exception exe) {
				log.error("Exception while inserting PHR Account   HID Number {}, Address: {}, Exception: {}",
						request.getAbhaAddress(), request.getAbhaNumber(), exe);
				
				//Flusing the profile pic (Not required to save inside the error  table)
				request.setProfilePhoto(null);
				PhrErrorEntity phrError = PhrErrorEntity.builder().healthIdNumber(request.getAbhaNumber())
						.reqPayload(request.toString()).respPayload(exe.toString())
						.createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();
				try {
				phrErrorRepo.saveAndFlush(phrError);
				}catch(Exception e) {
					log.error("Exception while inserting PHR Error Table  HID Number {}, Address: {}, Exception: {}",
							request.getAbhaAddress(), request.getAbhaNumber(), e);
				}

			}
		}
		// TODO Implement, Kafka Sync for HICM topic
		if (request.isNotify() && !isPhrAdressExist) {
			sendSuccessNotification(user.getFirstName(), user.getLastName(), user.getPhrAddress(),
					user.getHealthIdNumber(), user.getMobile(), user.getEmail(),
					SMSTypeEnums.REGISTER_SUCCESS_ABHA_NUMBER);
		}

	}

	/**
	 * Re-send the Mobile/EMAIL OTP
	 * 
	 * @param request
	 * @return
	 */
	public SuccessResponse resendOTP(ResendOTPRequest request) {
		Boolean isSend = false;
		PhrTransactionEntity txn = transactionService.findTransaction(request.getTransactionId(),
				AuthStatusEnums.ACTIVE.name(), "resendOTP");
		if (StringUtils.isNotBlank(txn.getMobile())) {
			isSend = transactionService.resendVerificationOtp(UUID.fromString(request.getTransactionId()));
		}
		if (StringUtils.isNotBlank(txn.getEmail())) {
			isSend = emailService.resendRegistrationOtp(request);
		}
		return SuccessResponse.builder().success(isSend).build();
	}

	public List<String> getPHRSuggestion(PHRSuggestionRequst request) {
		final PhrTransactionEntity phrTransactionEntity = transactionService
				.findTransaction(request.getTransactionId());
		Set<String> listPHRSuggestion = GeneralUtils.populatePHRAddress(phrTransactionEntity);
		List<String> listOfPHRPresentInDb = userService.checkPhrAddressExists(listPHRSuggestion);
		List<String> listOfPHRPresentInHID=userService.getPHRFromHID(listPHRSuggestion);
		listPHRSuggestion.removeAll(listOfPHRPresentInDb);
		listPHRSuggestion.removeAll(listOfPHRPresentInHID);
		Set<String> listPHRSuggestionNew = new HashSet<String>();
		for (String data : listPHRSuggestion) {
			if (data.length() >= 8 && data.length() <= 18) {
				listPHRSuggestionNew.add(data);
			}

		}
		Predicate<String> isEmpty = String::isEmpty;
		Predicate<String> notEmpty = isEmpty.negate();
		Predicate<String> stringWithSpace = strObj -> strObj.contains(" ");
		Predicate<String> filterPredicate = stringWithSpace.or(notEmpty);
		return listPHRSuggestionNew.stream().map(GeneralUtils::deSanetizePhrAddress).limit(PHR_SUGG_COUNT)
				.filter(filterPredicate).collect(Collectors.toList());

	}

	/**
	 * Send Notification Success (Mobile and Email)
	 * 
	 * @param name
	 * @param abhaAddress
	 * @param abhaNumber
	 * @param mobileNumber
	 * @param email
	 */
	private void sendSuccessNotification(String fname, String lName, String abhaAddress, String abhaNumber,
			String mobileNumber, String email, SMSTypeEnums smsType) {
		if (StringUtils.isNoneBlank(mobileNumber)) {
			nicSmsService.sendSuccessNotification(fname, lName, abhaAddress, abhaNumber, mobileNumber, smsType);
		}
		if (StringUtils.isNoneBlank(email)) {
			EmailNotificationRequest emailNotificationRequest = EmailNotificationRequest.builder().name(fname)
					.abhaAddress(abhaAddress).abhaNumber(abhaNumber).email(email)
					.opType(OperationTypeEmail.REGISTRATION_SUCCESS).transactionId(UUID.randomUUID().toString())
					.build();
			emailService.sendSuccessNotification(emailNotificationRequest);
		}

	}

	public Boolean changePhrAttribute(UpdatePhrProfilePhoto updatePhrProfilePhoto) {
		return userService.updateProfilePhoto(updatePhrProfilePhoto);
	}

	private void throwDuplicatePHRException(String phrAddress) {
		ErrorAttribute attribute = ErrorAttribute.builder().key("phrAddress").value(phrAddress).build();
		throw new PhrAddressDuplicateException("Phr address is taken by someone. Please user different phr address.", attribute);
	}

}