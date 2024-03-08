package com.parserlabs.phr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.parserlabs.phr.adapter.serivce.HIDDataInsAdapterProxy;
import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.cache.RedisCacheService;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.config.security.JwtTokenUtil;
import com.parserlabs.phr.entity.PhrAddressEntity;
import com.parserlabs.phr.entity.PhrAuthMethodEntity;
import com.parserlabs.phr.entity.PhrAuthTransactionEntity;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.entity.PhrUserEntity;
import com.parserlabs.phr.enums.AccountAction;
import com.parserlabs.phr.enums.AccountStatus;
import com.parserlabs.phr.enums.AuthMethods;
import com.parserlabs.phr.enums.AuthStatusEnums;
import com.parserlabs.phr.enums.KycStatus;
import com.parserlabs.phr.enums.LoginMethodsEnum;
import com.parserlabs.phr.enums.OperationTypeEmail;
import com.parserlabs.phr.exception.DatabaseException;
import com.parserlabs.phr.exception.DistrictNotValidException;
import com.parserlabs.phr.exception.HealthIdNumberNotFoundException;
import com.parserlabs.phr.exception.InvalidTokenException;
import com.parserlabs.phr.exception.LoginMethodNotAvailableException;
import com.parserlabs.phr.exception.MobileNumberNullException;
import com.parserlabs.phr.exception.PHRStatusNotActive;
import com.parserlabs.phr.exception.PasswordMismatchedException;
import com.parserlabs.phr.exception.PhrAddressDuplicateException;
import com.parserlabs.phr.exception.PhrIdNotAvailableByEmailException;
import com.parserlabs.phr.exception.PhrIdNotAvailableByMobileException;
import com.parserlabs.phr.exception.PhrIdNotAvailableException;
import com.parserlabs.phr.exception.StateNotValidException;
import com.parserlabs.phr.exception.SystemException;
import com.parserlabs.phr.exception.UserNotFoundException;
import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.model.AddressDTO;
import com.parserlabs.phr.model.ShareCMRequestPlayLoad;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.adapter.request.SearchPHRWithPHRMetaDetails;
import com.parserlabs.phr.model.adapter.response.HidResponse;
import com.parserlabs.phr.model.email.EmailNotificationRequest;
import com.parserlabs.phr.model.email.EmailVerifyAuthTransactionOTP;
import com.parserlabs.phr.model.login.phr.DeletePhrRequest;
import com.parserlabs.phr.model.login.phr.LoginViaPhrRequest;
import com.parserlabs.phr.model.profile.AbhaAddress;
import com.parserlabs.phr.model.profile.Address;
import com.parserlabs.phr.model.profile.User;
import com.parserlabs.phr.model.request.CreatePHRFromMobileRequest;
import com.parserlabs.phr.model.request.LinkedPlayLoad;
import com.parserlabs.phr.model.request.PhrGenerateOtpRequest;
import com.parserlabs.phr.model.request.UpdatePhrAttributePayLoad;
import com.parserlabs.phr.model.request.UpdatePhrProfilePhoto;
import com.parserlabs.phr.model.response.DeletedUserInfoResponse;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.repository.UserRepository;
import com.parserlabs.phr.service.email.EmailService;
import com.parserlabs.phr.service.sms.SMSService;
import com.parserlabs.phr.transform.TansformTransactionToHidResponse;
import com.parserlabs.phr.transform.TransFormTransationToKyc;
import com.parserlabs.phr.transform.TransformEntity;
import com.parserlabs.phr.transform.TransformTransactionToPhrUserEntity;
import com.parserlabs.phr.utils.Argon2Encoder;
import com.parserlabs.phr.utils.CommonUtils;
import com.parserlabs.phr.utils.GeneralUtils;
import com.parserlabs.phr.utils.PHRIdUtils;
import com.parserlabs.phr.utils.PhrAuthTsUtility;
import com.parserlabs.phr.utils.PhrUtilits;

import lombok.extern.slf4j.Slf4j;

/**
 * @author suraj/rajesh
 *
 */
@Service
@Slf4j
@CustomSpanned
public class UserService {

	@Value("${cm.notification.flag: true}")
	private Boolean cmNotificationFlag;

	@Value("${authts.secretKey}")
	private String authTsEncryptionKey;

	@Autowired
	private NotifyIntegratedProgramService cmNotifyService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransformEntity transformEntity;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private TransformTransactionToPhrUserEntity transformTransactionToPhrUserEntity;

	@Autowired
	private TansformTransactionToHidResponse tansformTransactionToHidResponse;

	@Autowired
	private TransFormTransationToKyc transFormTransationToKyc;

	@Autowired
	private HealthIdService healthIdService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private LgdService locationService;

	@Autowired
	private SMSService smsService;

	@Autowired
	private RedisCacheService redisCacheService;

	@Autowired
	private OtpPasswordService otpPasswordService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private HIDDataInsAdapterProxy hidDataInsAdapterProxy;

	@Autowired
	private RegistrationByMobileOrEmailService registrationService;

	@Autowired
	private S3StorageService s3StorageService;

	public UserDTO save(UserDTO user) {
		UserDTO userObj = process(populateUserEntity(user));
		if (cmNotificationFlag)
			cmNotifyService.shareProfile(populatePhareCMRequestPlayLoad(userObj));

		return userObj;
	}

	private ShareCMRequestPlayLoad populatePhareCMRequestPlayLoad(UserDTO userObj) {

		ShareCMRequestPlayLoad shareCMRequestPlayLoad = new ShareCMRequestPlayLoad();
		BeanUtils.copyProperties(userObj, shareCMRequestPlayLoad);
		shareCMRequestPlayLoad.setHealthId(userObj.getPhrAddress());
		BeanUtils.copyProperties(userObj.getAddress(), shareCMRequestPlayLoad);
		return shareCMRequestPlayLoad;

	}

	@Transactional
	public PhrUserEntity save(PhrUserEntity phrUserEntity) {
		log.info("befor saving user data distName {} and  distcode {}:   ",
				phrUserEntity.getAddress().getDistrictName(), phrUserEntity.getAddress().getDistrictCode());
		try {
			phrUserEntity.setPhrAddress(phrUserEntity.getPhrAddress().toLowerCase());
			phrUserEntity = userRepository.save(phrUserEntity);

			log.info("after saving user data distName {} and  distcode {}:   ",
					phrUserEntity.getAddress().getDistrictName(), phrUserEntity.getAddress().getDistrictCode());
		} catch (Exception e) {
			log.error("Exception whle saving user data, {}", e);
			throw new DatabaseException(String.format("Db exception : %s", e.getMessage()));
		}

		return phrUserEntity;
	}

	public UserDTO process(PhrUserEntity phrUserEntity) {
		return populateUser(save(phrUserEntity));
	}

	/**
	 * Method return the list of the user
	 * 
	 * @param phrAddress
	 * @return user list
	 */
	public List<UserDTO> getUserByMobile(String mobile) {
		return userRepository.findByMobile(mobile).orElseThrow(UserNotFoundException::new).stream()
				.map(user -> populateUser(user)).collect(Collectors.toList());
	}

	/**
	 * Method return the list of the user
	 * 
	 * @param phrAddress
	 * @return user set list
	 */
	public Set<String> getMappedPhrAddressByMobile(String mobile) {
		return userRepository.getUserLiteByMobile(mobile).orElseThrow(UserNotFoundException::new).stream()
				.map(user -> GeneralUtils.sanetizePhrAddress(user.getPhrAddress())).collect(Collectors.toSet());
	}

	/**
	 * Method return the list of the user
	 * 
	 * @param phrAddress , mobileVerifed
	 * @return user set list
	 */
	public Set<String> getMappedPhrAddressByMobile(String mobile, Boolean mobileVerified) {
		return userRepository.getUserLiteByMobileAndMobileVerified(mobile, mobileVerified)
				.orElseThrow(UserNotFoundException::new).stream()
				.filter(userRepositoryObj -> AccountStatus.ACTIVE.name()
						.equalsIgnoreCase(userRepositoryObj.getStatus()))
				.map(user -> GeneralUtils.sanetizePhrAddress(user.getPhrAddress())).collect(Collectors.toSet());
	}

	/**
	 * Method return the list of the user
	 * 
	 * @param phrAddress
	 * @return user set list
	 */
	public Set<String> getMappedPhrAddressByEmail(String email) {
		return userRepository.getUserLiteByEmail(email).orElseThrow(UserNotFoundException::new).stream()
				.map(user -> GeneralUtils.sanetizePhrAddress(user.getPhrAddress())).collect(Collectors.toSet());
	}

	/**
	 * Method return the list of the user
	 * 
	 * @param phrAddress , emailVerified
	 * @return user set list
	 */
	public Set<String> getMappedPhrAddressByEmail(String email, Boolean emailVerified) {
		return userRepository.getUserLiteByEmailAndEmailVerified(email, emailVerified)
				.orElseThrow(UserNotFoundException::new).stream()
				.filter(userRepositoryObj -> AccountStatus.ACTIVE.name()
						.equalsIgnoreCase(userRepositoryObj.getStatus()))
				.map(user -> GeneralUtils.sanetizePhrAddress(user.getPhrAddress())).collect(Collectors.toSet());
	}

	/**
	 * Method return the list of the user
	 * 
	 * @param healthidNumber
	 * @return user set list
	 */

	public Set<String> fetchPhrAddress(String healthidNumber) {
		Optional<List<PhrUserEntity>> optional = userRepository
				.getUserLiteByHealthIdNumber(GeneralUtils.actualHealthIdNumber(healthidNumber));
		Set<String> phrAddress = null;

		if (optional.isPresent()) {
			phrAddress = optional.get().stream()
					.filter(data -> data.getStatus().equalsIgnoreCase(AccountStatus.ACTIVE.name()))
					.map(PhrUserEntity::getPhrAddress).collect(Collectors.toSet());
		}
		return phrAddress;

	}

	/**
	 * Method will return the User details.
	 * 
	 * @param phrAddress
	 * @return user object
	 */
	@Transactional
	public UserDTO getUser(String phrAddress) {
		PhrUserEntity user = userRepository.findByPhrAddress(GeneralUtils.sanetizePhrAddress(phrAddress))
				.orElseThrow(UserNotFoundException::new);
		return populateUser(user);
	}

	/**
	 * Method will return the User details.
	 * 
	 * @return user object
	 */
	@Transactional
	public UserDTO get() {
		PhrUserEntity user = userRepository
				.findByPhrAddress(GeneralUtils.sanetizePhrAddress(PHRContextHolder.phrAddress()))
				.orElseThrow(UserNotFoundException::new);
		return populateUser(user);
	}

	/**
	 * Method will return the User details.
	 * 
	 * @param phrAddress
	 * @return user object
	 */
	@Transactional
	public User getUserProfile(String phrAddress) {
		PhrUserEntity userEntity = userRepository.findByPhrAddress(GeneralUtils.sanetizePhrAddress(phrAddress))
				.orElseThrow(UserNotFoundException::new);
		UserDTO userDTO = populateUser(userEntity);
		User user = User.builder().build();

		BeanUtils.copyProperties(userDTO, user);
		return user;
	}

	/**
	 * Method will return the User details.
	 * 
	 * @param phrAddress
	 * @return user object
	 */
	public User getQrCodeUserProfile(String phrAddress, String code) {
		UserDTO userDTO = getUser(phrAddress);
		Boolean isValidCode = false;
		try {
			isValidCode = StringUtils.isEmpty(code) ? false
					: Argon2Encoder.verify(PHRIdUtils.getSaltedValue(userDTO), code);
		} catch (Exception e) {
			throw new InvalidTokenException("$$code is invalid");
		}
		if (!isValidCode) {
			User user = User.builder().build();
			Address address = Address.builder().build();
			BeanUtils.copyProperties(userDTO, user);
			AddressDTO addressDto = userDTO.getAddress();
			BeanUtils.copyProperties(addressDto, address);
			user.setAddress(address);
			return user;
		}
		throw new UserNotFoundException();
	}

	public User getUserProfile() {
		PhrUserEntity userEntity = userRepository
				.findByPhrAddress(GeneralUtils.sanetizePhrAddress(PHRContextHolder.phrAddress()))
				.orElseThrow(UserNotFoundException::new);
		if (!AccountStatus.ACTIVE.name().equalsIgnoreCase(userEntity.getStatus())) {
			throw new UserNotFoundException();
		}

		User user=userFromEntity(userEntity);
		user.setAbhaAddresses(getAbhaAddresses(userEntity));

		return user;
	}

	private User userFromEntity(PhrUserEntity userEntity) {
		UserDTO userDTO = populateUser(userEntity);
		if (!userDTO.getStatus().equalsIgnoreCase(AccountStatus.ACTIVE.name())) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("phrAddress").value(userDTO.getPhrAddress())
					.build();
			throw new PHRStatusNotActive(attribute);
		}

		User user = User.builder().build();

		Address address = Address.builder().build();
		BeanUtils.copyProperties(userDTO, user);
		AddressDTO addressDto = userDTO.getAddress();
		BeanUtils.copyProperties(addressDto, address);

		user.setAddress(address);
		return user;
	}

	public User searchByPhrAddress(String phrAddress) {

		// Search ID inside the PHR System
		Optional<PhrUserEntity> userEntity = getByPhrAddress(GeneralUtils.sanetizePhrAddress(phrAddress));
		// Not found in PHR
		if (!userEntity.isPresent()) {
			User userResp = fetchFromHidAndSavePhr(phrAddress);
			if (Objects.nonNull(userResp)) {
				return userResp;
			}
			throw new UserNotFoundException();
		}
		return userFromEntity(userEntity.get());
	}

	
	public User fetchFromHidAndSavePhr(String phrAddress) {
		// Get Details from the HID System in-case not found in the PHR
		CreatePHRFromMobileRequest createPhrReq = fetchDataInsertionOfHID(GeneralUtils.sanetizePhrAddress(phrAddress));
		// Save to the database
		if (!Objects.isNull(createPhrReq)) {
			PhrUserEntity phrUserEntity = registrationService.createPHRMissingRegistration(createPhrReq);
			return userFromEntity(phrUserEntity);
		}
		return null;
	}
	
	public CreatePHRFromMobileRequest fetchDataInsertionOfHID(String phrAddress) {
		return hidDataInsAdapterProxy.featchHID(phrAddress);
	}

	/**
	 * Method will return the User details.
	 * 
	 * @param phrAddress
	 * @return user object
	 */
	public PhrUserEntity getPhrEntity() {
		PhrUserEntity userEntity = userRepository
				.findByPhrAddress(GeneralUtils.sanetizePhrAddress(PHRContextHolder.phrAddress()))
				.orElseThrow(UserNotFoundException::new);
		return userEntity;
	}

	/**
	 * Delete the User account by PHR Address.
	 * 
	 * @param request
	 * @return
	 * 
	 */
	public DeletedUserInfoResponse delete(DeletePhrRequest request) {
		// verify the otp
		boolean isPwdSame = false;
		// Decrypt String OTP or Password
		String decrryptedValued = DecryptRSAUtil.decrypt(request.getInput());
		if (request.getAuthMethod().equals(AuthMethods.MOBILE_OTP.name())) {
			PhrAuthTransactionEntity phrAuthTransactionEntity = authenticationService
					.fetchAuthTransactionEntitybyIdStatus(request.getTransactionId(), AuthStatusEnums.ACTIVE.name(),
							"delete");

			verifyOtpPassword(phrAuthTransactionEntity, decrryptedValued);
		}
		PhrUserEntity phrUserEntity = getPhrEntity();
		if (request.getAuthMethod().equals(AuthMethods.PASSWORD.name())) {
			isPwdSame = otpPasswordService.isPasswordSame(phrUserEntity.getPassword(), decrryptedValued);
			if (!isPwdSame) {
				throw new PasswordMismatchedException();
			}
		}

		// removed linking from hid call hid unlink api
		healthIdService.link(AccountAction.DELINK.name(), phrUserEntity.getPhrAddress(),
				phrUserEntity.getHealthIdNumber());
		phrUserEntity.setStatus(AccountStatus.DELETED.name());
		PhrAddressEntity address = phrUserEntity.getAddress();
		address.setStatus(AccountStatus.DELETED.name());
		phrUserEntity.setKycStatus(KycStatus.PENDING.name());
		phrUserEntity.setHealthIdNumber(null);
		phrUserEntity.setAddress(address);
		phrUserEntity.setReasonCode(reason(request.getReasons()));
		sendSuccessNotification(phrUserEntity.getFirstName(), phrUserEntity.getPhrAddress(),
				phrUserEntity.getHealthIdNumber(), phrUserEntity.getEmail(), phrUserEntity.getMobile(),
				OperationTypeEmail.DELINK_HID_SUCCESS);
		phrUserEntity.setHealthIdNumber(null);
		return transformTransactionToPhrUserEntity.tranform(save(phrUserEntity));
	}

	private void verifyOtpPassword(PhrAuthTransactionEntity phrAuthTransactionEntity, String otpPassword) {
		if (phrAuthTransactionEntity.getAuthMethod().equalsIgnoreCase(LoginMethodsEnum.PASSWORD.name())) {
			authenticationService.verifyPassword(phrAuthTransactionEntity, otpPassword);
		} else {
			authenticationService.verifyOtp(phrAuthTransactionEntity, otpPassword);
		}
	}

	private String reason(List<String> reason) {
		return String.join("-", reason);
	}

	public Boolean isPhrAddressAlreadyExist(String phrAddress) {
		return userRepository.existsByPhrAddress(GeneralUtils.sanetizePhrAddress(phrAddress));
	}

	public Boolean isPhrAddressAlreadyExistByMobile(String mobile) {
		return userRepository.existsByMobile(mobile).orElse(false);
	}

	public Boolean isPhrAddressAlreadyExistByMobileAndMobileVerified(String mobile, Boolean mobileVerified) {
		return userRepository.existsByMobileAndMobileVerified(mobile, mobileVerified).orElse(false);
	}

	public Boolean isPhrAddressAlreadyExistByEmail(String email) {
		return userRepository.existsByEmail(email).orElse(false);
	}

	public Boolean isPhrAddressAlreadyExistByEmailAndEmailVerified(String email, Boolean emailVerified) {
		return userRepository.existsByEmailAndEmailVerified(email, emailVerified).orElse(false);
	}

	public UserDTO populateUser(PhrUserEntity entity) {
		UserDTO user = UserDTO.builder().build();
		BeanUtils.copyProperties(entity, user);
		String photo = s3StorageService.fetchProfilePhoto(String.valueOf(entity.getId()));
		user.setProfilePhoto(photo);

		if (StringUtils.isEmpty(entity.getFirstName())) {
			user.setFullName(
					CommonUtils.populateFullName(entity.getFirstName(), entity.getMiddleName(), entity.getLastName()));
		}

		if (StringUtils.isEmpty(entity.getDateOfBirth())) {
			user.setDateOfBirth(CommonUtils.populateDateOfBirth(entity.getDayOfBirth(), entity.getMonthOfBirth(),
					entity.getYearOfBirth()));
		}

		if (Objects.nonNull(entity.getAddress())) {
			AddressDTO address = AddressDTO.builder().build();
			BeanUtils.copyProperties(entity.getAddress(), address);
			try {
				if (Objects.nonNull(address.getStateCode()) && !address.getStateCode().isEmpty()
						&& StringUtils.isBlank(address.getStateName())) {
					address.setStateName(locationService.getStateName(address.getStateCode()));
				}

				if (Objects.nonNull(address.getDistrictCode()) && !address.getDistrictCode().isEmpty()
						&& StringUtils.isBlank(address.getDistrictName())) {
					address.setDistrictName(
							locationService.getDistrictName(address.getStateCode(), address.getDistrictCode(), false));
				}
				user.setAddress(address);
			} catch (DistrictNotValidException exe) {
				address.setDistrictName("");
			} catch (StateNotValidException exe) {
				address.setStateName("");
			}
			user.setAddress(address);
		}
		if (!CollectionUtils.isEmpty(entity.getPhrAuthMethodEntity())) {
			Set<LoginMethodsEnum> authMethods = entity.getPhrAuthMethodEntity().stream()
					.filter(authMethodEntity -> StringUtils.isNoneBlank(authMethodEntity.getAuthMethod()))
					.map(authMethod -> LoginMethodsEnum.valueOf(authMethod.getAuthMethod().toString()))
					.collect(Collectors.toSet());
			if (!authMethods.isEmpty())
				user.setAuthMode(authMethods);
		}

		return user;
	}

	private PhrUserEntity populateUserEntity(UserDTO user) {
		PhrUserEntity userParententity = transformEntity.getUserEntity(user);
		// Populate Address
		if (Objects.nonNull(user.getAddress())) {
			PhrAddressEntity addressChildEntity = transformEntity.populateAddress(user);
			userParententity.setAddress(addressChildEntity);// Set Child Reference(addressEntity) in
			addressChildEntity.setUser(userParententity); // Set parent reference(userParententity) in
															// childEntity(addressChildEntity)
			user.getAddress().setStateName(addressChildEntity.getStateName());	
			user.getAddress().setDistrictName(addressChildEntity.getDistrictName());
		}
		// Populate Authentication Method Data
		if (userParententity.getPhrAuthMethodEntity().isEmpty()) {
			Set<PhrAuthMethodEntity> setAuthMethods = transformEntity.getAuthSets(user, userParententity);
			if (!setAuthMethods.isEmpty()) {
				userParententity.setPhrAuthMethodEntity(setAuthMethods);
			}
		}
		return userParententity;
	}

	/**
	 * Check PHR address data integrity
	 * 
	 * @param phrAddress
	 * @return
	 * @throws PhrAddressDuplicateException when PHR Address already exist
	 */
	public void checkPhrAddressExists(String phrAddress) {
		
		if (isPhrAddressAlreadyExist(phrAddress)||!getPHRFromHID(new HashSet<>(Arrays.asList(GeneralUtils.sanetizePhrAddress(phrAddress)))).isEmpty()) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("phrAddress").value(phrAddress).build();
			throw new PhrAddressDuplicateException(
					"Phr address is taken by someone. Please use different phr address.", attribute);
		}
	}

	/**
	 * Check account exist by PHR address
	 * 
	 * @param phrAddress
	 * @return true if PHR account exist
	 * @throws PhrIdNotAvailableException
	 */
	public boolean doesPhrAddressExist(String phrAddress) {
		Boolean isPhrExist = isPhrAddressAlreadyExist(phrAddress);
		if (Boolean.TRUE.equals(isPhrExist)) {
			return isPhrExist;
		}
		
		User userResp = fetchFromHidAndSavePhr(phrAddress);
		if (Objects.nonNull(userResp)) {
			isPhrExist = true;
			return isPhrExist;
		}
		
		ErrorAttribute attribute = ErrorAttribute.builder().key("phrAddress").value(phrAddress).build();
		throw new PhrIdNotAvailableException("No Account found by the PHR ADDRESS.", attribute);

	}

	/**
	 * Check account exist by PHR address
	 * 
	 * @param phrAddress
	 * @return true if PHR account exist
	 * @throws PhrIdNotAvailableException
	 */
	public boolean doesPhrAddressExistByMobile(String mobile) {
		Boolean isPhrExist = isPhrAddressAlreadyExistByMobile(mobile);
		if (!isPhrExist) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("mobile").value(mobile).build();
			throw new PhrIdNotAvailableByMobileException("No Account found by the mobile number.", attribute);
		}
		return isPhrExist;
	}

	/**
	 * Check account exist by PHR address
	 * 
	 * @param phrAddress , mobileVerified
	 * @return true if PHR account exist
	 * @throws PhrIdNotAvailableException
	 */
	public boolean doesPhrAddressExistByMobileAndMobileVerified(String mobile, Boolean mobileVerified) {
		Boolean isPhrExist = isPhrAddressAlreadyExistByMobileAndMobileVerified(mobile.trim(), mobileVerified);
		log.info("does mobile number existing : {} ,mobile :{} ", isPhrExist, mobile.trim());
		if (!isPhrExist) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("mobile").value(mobile).build();
			throw new PhrIdNotAvailableByMobileException("No Account found by the mobile number.", attribute);
		}
		return isPhrExist;
	}

	/**
	 * Check account exist by email
	 * 
	 * @param phrAddress
	 * @return true if PHR account exist
	 * @throws PhrIdNotAvailableException
	 */
	public boolean doesPhrAddressExistByEmail(String email) {
		Boolean isPhrExist = isPhrAddressAlreadyExistByEmail(email);
		if (!isPhrExist) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("email").value(email).build();
			throw new PhrIdNotAvailableByMobileException("No Account found by the email address.", attribute);
		}
		return isPhrExist;
	}

	/**
	 * Check account exist by email
	 * 
	 * @param phrAddress , emailVerified
	 * @return true if PHR account exist
	 * @throws PhrIdNotAvailableException
	 */
	public boolean doesPhrAddressExistByEmailAndEmailVerified(String email, Boolean emailVerified) {
		Boolean isPhrExist = isPhrAddressAlreadyExistByEmailAndEmailVerified(email.trim(), emailVerified);
		log.info("does email existing : {} ,email :{} ", isPhrExist, email.trim());
		if (!isPhrExist) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("email").value(email).build();
			throw new PhrIdNotAvailableByEmailException("No Account found by the email address.", attribute);
		}
		return isPhrExist;
	}

	/**
	 * Update the user Mobile Number
	 * 
	 * @param transactionEntity
	 * @return true/false
	 */
	public boolean updateMobileNumber(PhrTransactionEntity transactionEntity) {
		PhrUserEntity phrUser = getPhrEntity();
		phrUser.setMobile(transactionEntity.getMobile());
		Set<PhrAuthMethodEntity> authMethod = authMethod(phrUser, AuthMethods.MOBILE_OTP.name());
		phrUser.setMobileVerified(true);
		phrUser.setPhrAuthMethodEntity(authMethod);
		save(phrUser);
		return true;
	}

	private Set<PhrAuthMethodEntity> authMethod(PhrUserEntity phrUserEntity, String authMedhod) {
		Set<PhrAuthMethodEntity> authMethods = phrUserEntity.getPhrAuthMethodEntity();
		long count = authMethods.stream().filter(authodMethod -> authodMethod.getAuthMethod().equals(authMedhod))
				.count();
		if (count == 0) {
			authMethods = new HashSet<>(phrUserEntity.getPhrAuthMethodEntity());
			PhrAuthMethodEntity passwordAuthMethod = PhrAuthMethodEntity.builder().authMethod(authMedhod)
					.user(phrUserEntity).build();
			authMethods.add(passwordAuthMethod);
		}
		return authMethods;
	}

	/**
	 * Update the User Email Address
	 * 
	 * @param transactionEntity
	 * @return true/false
	 */
	public boolean updateEmailAddress(PhrTransactionEntity transactionEntity) {
		PhrUserEntity phrUser = getPhrEntity();
		Set<PhrAuthMethodEntity> authMethod = authMethod(phrUser, AuthMethods.EMAIL_OTP.name());
		phrUser.setEmail(transactionEntity.getEmail());
		phrUser.setEmailVerified(true);
		phrUser.setPhrAuthMethodEntity(authMethod);
		save(phrUser);
		return true;
	}

	/**
	 * Check account exist by HealhtIdNumber
	 * 
	 * @param healthIdNumber , yearOfBrithDay
	 * @return true if healthIdNumber account exist
	 * @throws HealthIdNumberNotFoundException
	 */
	public boolean doesHealhtIdNumberExist(String healthIdNumber, String yearOfBrithDay) {
		Boolean healhtIdNumber = userRepository.existsByHealthIdNumberAndYearOfBirth(healthIdNumber, yearOfBrithDay)
				.orElse(true);

		if (!healhtIdNumber) {
			ErrorAttribute attribute = ErrorAttribute.builder().key("healthIdNumber").value(healthIdNumber).build();
			throw new HealthIdNumberNotFoundException("No Account found by the healthIdNumber.", attribute);
		}
		return healhtIdNumber;
	}

	/**
	 * Check account exist by HealhtIdNumber and return status with out exception
	 * 
	 * @param healthIdNumber , yearOfBrithDay
	 * @return true if healthIdNumber account exist
	 * @throws HealthIdNumberNotFoundException
	 */
	public boolean doesHealhtIdNumberExistWithoutException(String healthIdNumber, String yearOfBrithDay) {

		Boolean healhtIdNumber=false;
		if(StringUtils.isNotEmpty(yearOfBrithDay)&&StringUtils.isNotEmpty(healthIdNumber)) {
		 healhtIdNumber = userRepository.existsByHealthIdNumberAndYearOfBirth(healthIdNumber, yearOfBrithDay)
				.orElse(true);
		}else {
			healhtIdNumber = userRepository.existsByHealthIdNumber(healthIdNumber)
					.orElse(true);
		}

		if (!healhtIdNumber) {
			return false;
		}
		return healhtIdNumber;
	}

	public Map<String, Boolean> linked(LinkedPlayLoad linkedPlayLoad) {

		Map<String, Boolean> map = null;
		switch (linkedPlayLoad.getAction().name()) {
		case "LINK":
			map = link(AccountAction.LINK.name(), linkedPlayLoad.getTransactionId(), "LINK");
			break;
		case "DELINK":
			map = delink(AccountAction.DELINK.name());
			break;
		default:
			break;
		}

		return map;

	}

	public Map<String, Boolean> link(String action, String txnId, String flow) {

		if ("CREATE".equalsIgnoreCase(flow)) {
			PhrTransactionEntity phrAuthTransactionEntity = transactionService.findTransaction(txnId);
			PHRContextHolder.healthIdUserToken(phrAuthTransactionEntity.getHidToken());
			// fetchTransactionByPhrAddres(PHRContextHolder.phrAddress());
		} else {

			PhrAuthTransactionEntity phrAuthTransactionEntity = authenticationService
					.fetchAuthTransactionEntitybyId(UUID.fromString(txnId), flow);
			if (StringUtils.isEmpty(PHRContextHolder.healthIdUserToken())) {
				PHRContextHolder.healthIdUserToken(phrAuthTransactionEntity.getReferenceToken());
			}
			if (StringUtils.isEmpty(PHRContextHolder.phrAddress())) {
				PHRContextHolder.phrAddress(phrAuthTransactionEntity.getPhrAddress());
			}

		}
		HidResponse hidResponse = healthIdService.featchUserDetailsAfterAuthenticated();
		PhrTransactionEntity transactionEntity = tansformTransactionToHidResponse.apply(hidResponse);
		PhrUserEntity phrUserEntity = userRepository.findByPhrAddress(PHRContextHolder.phrAddress()).get();
		phrUserEntity = transformTransactionToPhrUserEntity.trasnform(transactionEntity, phrUserEntity);

		transFormTransationToKyc.apply(transactionEntity);

		Map<String, Boolean> response = healthIdService.link(action, phrUserEntity.getPhrAddress(), null);
		if (response.get("status").booleanValue()) {
			log.info("@@@@@@@@@@@@@@@@@@@@@@ PhrUserEntity Profile Photo = {}", phrUserEntity.getProfilePhoto());
			boolean isPhotoEncoded = true;
			String uploadKey = s3StorageService.setProfilePhoto(String.valueOf(phrUserEntity.getId()), transactionEntity.getProfilePhoto().getBytes(), isPhotoEncoded);
			if (uploadKey == null) {
				log.error("Photo upload failed while linking");
			} else {
				log.info("Photo uploaded successfuly");
				phrUserEntity.setProfilePhoto(null);
			}
			phrUserEntity = save(phrUserEntity);
			sendSuccessNotification(phrUserEntity.getFirstName(), phrUserEntity.getPhrAddress(),
					phrUserEntity.getHealthIdNumber(), phrUserEntity.getEmail(), phrUserEntity.getMobile(),
					OperationTypeEmail.LINK_HID_SUCCESS);
		}

		return Collections.singletonMap("success", response.get("status").booleanValue());

	}

	private Map<String, Boolean> delink(String action) {
		PhrUserEntity phrUserEntity = userRepository.findByPhrAddress(PHRContextHolder.phrAddress()).get();
		Map<String, Boolean> response = healthIdService.link(action, phrUserEntity.getPhrAddress(),
				phrUserEntity.getHealthIdNumber());
		String healthid = phrUserEntity.getHealthIdNumber();
		if (response.get("status").booleanValue()) {
			phrUserEntity.setKycStatus(KycStatus.PENDING.name());
			phrUserEntity.setHealthIdNumber(null);
			userRepository.save(phrUserEntity);
			sendSuccessNotification(phrUserEntity.getFirstName(), phrUserEntity.getPhrAddress(), healthid,
					phrUserEntity.getEmail(), phrUserEntity.getMobile(), OperationTypeEmail.DELINK_HID_SUCCESS);
			phrUserEntity.setHealthIdNumber(null);
		}

		return Collections.singletonMap("success", response.get("status").booleanValue());
	}

	public List<String> checkPhrAddressExists(Set<String> listPHRSuggestion) {
		return userRepository.checkForPhrExists(listPHRSuggestion).get();

	}

	public Set<String> fetchPhrLinkToGivenMeta(SearchPHRWithPHRMetaDetails searchPHRWithPHRMetaDetails) {
		return userRepository
				.findPhrWithGivenMeta(searchPHRWithPHRMetaDetails.getName(), searchPHRWithPHRMetaDetails.getGender(),
						searchPHRWithPHRMetaDetails.getYearOfBirth(), searchPHRWithPHRMetaDetails.getMonthOfBirth(),
						searchPHRWithPHRMetaDetails.getDayOfBirth())
				.get().stream().map(PhrUserEntity::getPhrAddress).collect(Collectors.toSet());

	}

	public UserDTO fetchPhrDetails(String abhaAddress) {
		PhrUserEntity phrUserEntity = userRepository.findByPhrAddress(abhaAddress).orElse(null);
		return Objects.nonNull(phrUserEntity) ? populateUser(phrUserEntity) : null;
	}

	public PhrUserEntity getPhrEntity(String phrAddress) {
		return userRepository.findByPhrAddress(GeneralUtils.sanetizePhrAddress(phrAddress))
				.orElseThrow(UserNotFoundException::new);
	}

	public Optional<PhrUserEntity> getByPhrAddress(String phrAddress) {
		return userRepository.findByPhrAddress(GeneralUtils.sanetizePhrAddress(phrAddress));
	}

	public boolean update(UpdatePhrAttributePayLoad updatePhrAttributePayLoad) {
		Optional<List<PhrUserEntity>> usersOptional = null;
		boolean status = false;
		PhrUserEntity user = null;

		if (updatePhrAttributePayLoad.getPhrAddress().contains("@")) {
			user = getPhrEntity(updatePhrAttributePayLoad.getPhrAddress());
		} else {
			usersOptional = userRepository.findByHealthIdNumber(updatePhrAttributePayLoad.getPhrAddress());
			if (!usersOptional.isPresent()) {
				throw new UserNotFoundException("health-id-number not found exception");
			}

			user = !CollectionUtils.isEmpty(usersOptional.get()) ? usersOptional.get().get(0) : null;
		}
		List<PhrUserEntity> users = null;
		boolean isBatchUpdate = false;
		if (Objects.nonNull(user)) {
			String key = updatePhrAttributePayLoad.getKey().toLowerCase();
			switch (key) {
			case "status":
				if (updatePhrAttributePayLoad.getValue().equalsIgnoreCase(AccountStatus.DELETED.name())) {

					users = usersOptional.get().stream().map(this::transfrom).collect(Collectors.toList());
					processBatchUpdate(users);
					log.info("update the status of update Phr {} ", users);
					isBatchUpdate = true;
				} else {
					user.setStatus(updatePhrAttributePayLoad.getValue());
					isBatchUpdate = true;

				}
				break;

			case "emailverified":
				if (updatePhrAttributePayLoad.getValue().toLowerCase().contentEquals("true")
						|| updatePhrAttributePayLoad.getValue().toLowerCase().contentEquals("false")) {
					Boolean emailverified = Boolean.parseBoolean(updatePhrAttributePayLoad.getValue());
					if (Objects.nonNull(user.getPhrAuthMethodEntity())) {
						user.getPhrAuthMethodEntity().add(PhrAuthMethodEntity.builder()
								.authMethod(AuthMethods.EMAIL_OTP.name()).user(user).build());
					}
					user.setEmailVerified(emailverified);
					status = true;
				} else {
					status = false;
				}
				break;
			case "mobileVerified":
				if (updatePhrAttributePayLoad.getValue().toLowerCase().contentEquals("true")
						|| updatePhrAttributePayLoad.getValue().toLowerCase().contentEquals("false")) {
					Boolean emailverified = Boolean.parseBoolean(updatePhrAttributePayLoad.getValue());
					if (Objects.nonNull(user.getPhrAuthMethodEntity())) {
						user.getPhrAuthMethodEntity().add(PhrAuthMethodEntity.builder()
								.authMethod(AuthMethods.MOBILE_OTP.name()).user(user).build());
					}
					user.setEmailVerified(emailverified);
					status = true;
				} else {
					status = false;
				}
				break;

			default:
				status = false;
			}
		}

		if (status) {
			save(user);
		} else if (isBatchUpdate) {
			status = true;
		}

		return status;
	}

	/**
	 * Send Notification Success (Mobile and Email)
	 * 
	 * @param name
	 * @param abhaAddress
	 * @param abhaNumber
	 * @param email
	 * @param typeEmail
	 */
	private void sendSuccessNotification(String name, String abhaAddress, String abhaNumber, String email,
			String phoneNumber, OperationTypeEmail typeEmail) {

		// EMAIL NUMBER
		if (StringUtils.isNoneBlank(email)) {
			EmailNotificationRequest emailNotificationRequest = EmailNotificationRequest.builder().name(name)
					.abhaAddress(abhaAddress).abhaNumber(abhaNumber).email(email).opType(typeEmail)
					.transactionId(UUID.randomUUID().toString()).build();
			emailService.sendSuccessNotification(emailNotificationRequest);
		}

		// MOBILE NUMBER.
		if (StringUtils.isNoneBlank(phoneNumber) && PhrUtilits.isValidMobile(phoneNumber)) {
			switch (typeEmail) {
			case LINK_HID_SUCCESS:
				smsService.sendPhrLinkedNotification(abhaNumber, abhaAddress, phoneNumber);
				break;
			case DELINK_HID_SUCCESS:
				smsService.sendPhrUnLinkedNotification(abhaNumber, abhaAddress, phoneNumber);
				break;
			default:
				break;
			}
		}

	}

	public void logout() {
		// Saving the access token into Redis cache
		log.info("Logout intiated for phr address id {}.", PHRContextHolder.phrAddress());
		boolean flag = redisCacheService.put(PHRContextHolder.accessToken(), PHRContextHolder.phrAddress());
		log.info("Logout {} for phr address {}", flag ? "Successfully" : "Falied", PHRContextHolder.phrAddress());
	}

	public Boolean updateProfilePhoto(UpdatePhrProfilePhoto updatePhrProfilePhoto) {
		boolean status = false;
		boolean isPhotoEncoded = true;
		PhrUserEntity user = getPhrEntity(updatePhrProfilePhoto.getPhrAddress());
		if (Objects.nonNull(user) && !StringUtils.isEmpty(updatePhrProfilePhoto.getProfilePhoto())) {
			String uploadKey = s3StorageService.setProfilePhoto(String.valueOf(user.getId()), updatePhrProfilePhoto.getProfilePhoto().getBytes(), isPhotoEncoded);
			user.setProfilePhoto(null);
			user.setProfilePhotoCompressed(updatePhrProfilePhoto.getProfilePhotoCompressed());
			save(user);
			status = true;
			log.info("successfully inserted photo for phr {}", updatePhrProfilePhoto.getPhrAddress());
		}
		return status;

	}

	/**
	 * Method will return the User details.
	 * 
	 * @param phrAddress
	 * @return user object
	 */
//	public List<PhrUserEntity> fetchHealthIdNumberAndKycStatus(String healthNumber) {
//		return userRepository.getUserByHealthIdNumber(healthNumber).get();

//	public List<PhrUserEntity> fetchHealthIdNumberAndKycStatus(String healthNumber) {
//
//		return userRepository.findByHealthIdNumber(healthNumber).get();
//
//	}

	void processBatchUpdate(List<PhrUserEntity> phrUserEntity) {
		userRepository.saveAll(phrUserEntity);
	}

	private PhrUserEntity transfrom(PhrUserEntity phrUserEntity) {
		phrUserEntity.setHealthIdNumber(null);
		phrUserEntity.setKycStatus(KycStatus.PENDING.name());
		return phrUserEntity;
	}

	public TransactionResponse phrGenerateOtp(PhrGenerateOtpRequest request) {

		return startPhrAuthTransaction(request);
	}

	private TransactionResponse startPhrAuthTransaction(PhrGenerateOtpRequest generateOtpRequest) {
		UserDTO user = getUser(GeneralUtils.sanetizePhrAddress(PHRContextHolder.phrAddress()));
		LoginViaPhrRequest request = LoginViaPhrRequest.builder().authMethod(generateOtpRequest.getAuthMethod())
				.phrAddress(user.getPhrAddress()).build();
		TransactionResponse transactionResponse = null;

		// check if the auth method is present
		if (!user.getAuthMode().contains(generateOtpRequest.getAuthMethod())) {
			throw new LoginMethodNotAvailableException("Invalid Auth method provided");

		}
		// Check Authentication Methods
		switch (request.getAuthMethod()) {
		case MOBILE_OTP:
			if (StringUtils.isNotEmpty(user.getMobile())) {
				transactionResponse = authenticationService.startPhrMobileLoginAuthTransaction(user, request);
			} else {
				ErrorAttribute attribute = ErrorAttribute.builder().key("phrAddress").value(user.getPhrAddress())
						.build();
				throw new MobileNumberNullException("mobile number is blank against {} ", attribute);
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
			throw new LoginMethodNotAvailableException("Invalid Auth method provided");
		}
		return transactionResponse;

	}

	public String encryptedAuthTs(String authTs) {
		try {
			return PhrAuthTsUtility.encrypt(authTs, PhrAuthTsUtility.encodeKey(authTsEncryptionKey));
		} catch (Exception e) {
			throw new SystemException("Error while encoding Ts");

		}
	}
	
	public List<AbhaAddress> getAbhaAddresses(PhrUserEntity userEntity)
	{
		Optional<List<PhrUserEntity>> optional = java.util.Optional.empty();
		if(Objects.nonNull(userEntity.getMobile())&&userEntity.isMobileVerified())
		{
			 optional=userRepository.getUserLiteByMobileAndMobileVerifiedAndStatus(userEntity.getMobile(),userEntity.isMobileVerified(),userEntity.getStatus());
			
		}else if(Objects.nonNull(userEntity.getEmail())&&userEntity.isEmailVerified()){
			 optional=userRepository.getUserLiteByEmailAndEmailVerifiedAndStatus(userEntity.getEmail(),userEntity.isEmailVerified(),userEntity.getStatus());	
		}
		if (optional.isPresent()) {
			
			return optional.get().stream().map(userEnt -> populateAbhaAddress(userEnt)).collect(Collectors.toList());
			
		}else {
			return new ArrayList<>();
		}
	}
	
	public AbhaAddress populateAbhaAddress(PhrUserEntity userEntity)
	{
		AbhaAddress abhaAddressList = AbhaAddress.builder().build();
		String photo = s3StorageService.fetchProfilePhoto(String.valueOf(userEntity.getId()));
		log.info("New Code Profile photo -------------  {}",photo);
		userEntity.setProfilePhoto(photo);
		BeanUtils.copyProperties(userEntity, abhaAddressList);
		return abhaAddressList;		         
	}
	
	public JwtResponse switchProfileToken(String phrAddress) {
		UserDTO userDto=getUser(phrAddress);
      //  userDto.setAuthTransactionId(authTransaction.getAuthTransactionId().toString());
		
		JwtResponse jwtResponse = jwtTokenUtil.generateTokens(userDto);
		return JwtResponse.builder().token(jwtResponse.getToken()).expiresIn(jwtResponse.getExpiresIn())
				.refreshToken(jwtResponse.getRefreshToken()).refreshExpiresIn(jwtResponse.getRefreshExpiresIn())
				.phrAdress(phrAddress)
				.firstName(userDto.getFirstName())
				.authTs(encryptedAuthTs(PhrUtilits.getCurrentTimeStamp())).build();
	}
	public List<String> getPHRFromHID(Set<String> abhaList) {
		try {
			List<String> hidAbhaList = healthIdService.getPHRFromHID(abhaList);
			log.info("List of phr address received from HID systen : {}", hidAbhaList);
			return hidAbhaList;
		} catch (Exception ex) {
			log.error("exception while calling HID Api for suggestion : {}", ex);
			return Collections.emptyList();
		}
	}
}
