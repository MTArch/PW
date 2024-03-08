/**
 * 
 */
package com.parserlabs.phr.transform;

import java.beans.FeatureDescriptor;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrAuthMethodEntity;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.entity.PhrUserEntity;
import com.parserlabs.phr.enums.AccountStatus;
import com.parserlabs.phr.enums.KycStatus;
import com.parserlabs.phr.enums.LoginMethodsEnum;
import com.parserlabs.phr.model.AddressDTO;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.request.CreatePHRFromMobileRequest;
import com.parserlabs.phr.model.request.CreatePHRRequest;
import com.parserlabs.phr.service.LocationService;
import com.parserlabs.phr.utils.CommonUtils;
import com.parserlabs.phr.utils.GeneralUtils;
import com.parserlabs.phr.utils.PhrUtilits;

/**
 * @author Rajesh
 *
 */
@Component
@CustomSpanned
public class TransformRegistrationDataToUserData {
	@Autowired
	private LocationService locationService;
	/**
	 * address entity
	 * 
	 * @param source
	 * @return
	 */
	public AddressDTO getAddress(PhrTransactionEntity source) {
		// Populate the Address
		AddressDTO address = AddressDTO.builder().stateCode(source.getStateCode())
				.districtCode(source.getDistrictCode()).addressLine(source.getAddressLine()).pincode(source.getPincode()).build();
		return address;
	}

	/***
	 * Transform the transaction data to the User DTO
	 * 
	 * @param source
	 * @param request
	 * @return User
	 */
	public UserDTO populateUserData(PhrTransactionEntity source, CreatePHRRequest request) {
		// Populate the User Data
		UserDTO destination = UserDTO.builder().build();
		BeanUtils.copyProperties(source, destination);
		destination.setPassword(PhrUtilits.getEncodedPassword(request.getPassword()));
		destination.setPhrAddress(GeneralUtils.sanetizePhrAddress(request.getPhrAddress()));
		destination.setAddress(getAddress(source));
		destination.setHealthIdNumber(GeneralUtils.actualHealthIdNumber(source.getHealthIdNumber()));
		destination.setKycStatus(source.getKycStatus());
		destination.setEmailVerified(source.getEmailVerified());
		//Update the status While saving details in the user table
		destination.setStatus(AccountStatus.ACTIVE.name());
		return destination;
	}

	
	private  String[] getNullPropertyNames(Object source) {
	    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
	    return Stream.of(wrappedSource.getPropertyDescriptors())
	            .map(FeatureDescriptor::getName)
	            .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
	            .toArray(String[]::new);
	}
	
	
	/**
	 * Transform
	 * 
	 * @param request
	 * @param user
	 * @param migrationFlag
	 */
	public void createUserPHRFromMobileTo(CreatePHRFromMobileRequest request, UserDTO user) {
		// TODO Auto-generated method stub
		BeanUtils.copyProperties(request, user);
		user.setPhrAddress(GeneralUtils.sanetizePhrAddress(request.getAbhaAddress()));
		user.setHealthIdNumber(request.getAbhaNumber());
		user.setAddress(AddressDTO.builder().stateCode(request.getStateCode()).districtCode(request.getDistrictCode())
				.addressLine(request.getAddressLine()).pincode(request.getPinCode())
				.countryCode(request.getCountryCode()).build());
		user.setAuthMode(request.getAuthMethods().stream().map(authMethod -> LoginMethodsEnum.valueOf(authMethod))
				.collect(Collectors.toSet()));
		user.setPassword(PhrUtilits.getEncodedPassword(request.getPassword()));
	}

	public void getUpdatePhrUserEntity(PhrUserEntity originalUser, CreatePHRFromMobileRequest request) {
		
		
		if (StringUtils.hasLength(request.getAddressLine()))
		{
			originalUser.getAddress().setAddressLine(request.getAddressLine());	
		}
		if (StringUtils.hasLength(request.getAbhaNumber()))
		{
			originalUser.setHealthIdNumber(request.getAbhaNumber());	
		}
		
		if (StringUtils.hasLength(request.getDayOfBirth()))
		{
			originalUser.setDayOfBirth(request.getDayOfBirth());
			
		}
		
		if (StringUtils.hasLength(request.getMonthOfBirth()))
		{
			originalUser.setMonthOfBirth(request.getMonthOfBirth());	
		}
		
		if (StringUtils.hasLength(request.getYearOfBirth()))
		{
			originalUser.setYearOfBirth(request.getYearOfBirth());	
		}
	
		if(StringUtils.hasLength(request.getYearOfBirth()) || 
				StringUtils.hasLength(request.getMonthOfBirth()) ||
				StringUtils.hasLength(request.getDayOfBirth()))
		{
			originalUser.setDateOfBirth(
					CommonUtils.populateDateOfBirth(originalUser.getDayOfBirth(),
							                        originalUser.getMonthOfBirth(),
							                        originalUser.getYearOfBirth()));
		}
		
		if ( StringUtils.hasLength(request.getPassword()))
		{
			originalUser.setPassword(PhrUtilits.getEncodedPassword(request.getPassword()));
		}	
		
		if ( StringUtils.hasLength(request.getPinCode()))
		{
			originalUser.getAddress().setPincode(request.getPinCode());
		}	
		
		if (StringUtils.hasLength(request.getStateCode()))
		{	
		originalUser.getAddress().setStateCode(request.getStateCode());
		originalUser.getAddress().setStateName(locationService.getStateName(request.getStateCode()));
		}
		if (StringUtils.hasLength(request.getPinCode()))
		{
		originalUser.getAddress().setDistrictCode(request.getDistrictCode());
		originalUser.getAddress()
				.setDistrictName(locationService.getDistrictName(request.getStateCode(), request.getDistrictCode()));
		}


		if (StringUtils.hasLength( originalUser.getKycStatus()) && KycStatus.PENDING.name().equalsIgnoreCase(originalUser.getKycStatus())) {
			
			BeanUtils.copyProperties(request, originalUser,getNullPropertyNames(request));
			if (StringUtils.hasLength(request.getPassword()))
			{
				originalUser.setPassword(PhrUtilits.getEncodedPassword(request.getPassword()));
			}
			
			if ( StringUtils.hasLength(request.getKycStatus()))
			{
				originalUser.setKycStatus(request.getKycStatus());			}
			else
			{
				originalUser.setKycStatus(KycStatus.PENDING.name());
			}	
			originalUser.setFullName(CommonUtils.populateFullName(request.getFirstName(), request.getMiddleName(),
					request.getLastName()));
			
			if (Objects.nonNull(request.getProfilePhoto()) && request.getProfilePhoto().length() > 0) {
				originalUser.setProfilePhoto(request.getProfilePhoto());
			}
		}
		
		if (!Objects.isNull(request.getAuthMethods()))
		{	
		  request.getAuthMethods().removeAll(
				                                     originalUser.getPhrAuthMethodEntity()
				                                      .stream()
				                                      .map(authMethodObj -> { return authMethodObj.getAuthMethod();})
				                                      .collect(Collectors.toList()));
		  
		  if (!Objects.isNull(request.getAuthMethods())) {
			  Set<PhrAuthMethodEntity> authmethods = request.getAuthMethods().stream()
						.map(auth -> authMethod(originalUser, auth.toString())).flatMap(Set::stream).collect(Collectors.toSet());
				originalUser.setPhrAuthMethodEntity(authmethods);
			}
		}

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


}
