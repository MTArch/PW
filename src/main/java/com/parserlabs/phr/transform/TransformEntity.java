/**
 * 
 */
package com.parserlabs.phr.transform;

import static com.parserlabs.phr.utils.GeneralUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrAddressEntity;
import com.parserlabs.phr.entity.PhrAuthMethodEntity;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.entity.PhrUserEntity;
import com.parserlabs.phr.enums.AuthMethods;
import com.parserlabs.phr.enums.AuthStatusEnums;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.model.AddressDTO;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.request.RegistrationByMobileOrEmailRequest;
import com.parserlabs.phr.service.LocationService;
import com.parserlabs.phr.utils.CommonUtils;

import lombok.AllArgsConstructor;

/**
 * @author Rajesh
 *
 */
@Component
@AllArgsConstructor
@CustomSpanned
public class TransformEntity {

	private LocationService locationService;

	public PhrTransactionEntity populateTransactionEntity(RegistrationByMobileOrEmailRequest source,
			PhrTransactionEntity destination) {
		BeanUtils.copyProperties(source, destination);
		destination.setMobile(StringUtils.isNotBlank(DecryptRSAUtil.decrypt(source.getMobile()))
				? DecryptRSAUtil.decrypt(source.getMobile())
				: source.getMobile());
		destination.setEmail(StringUtils.isNotBlank(DecryptRSAUtil.decrypt(source.getEmail()))
				? DecryptRSAUtil.decrypt(source.getEmail())
				: source.getEmail());
		destination.setAddressLine(source.getAddress());
		destination.setPincode(source.getPinCode());
		
		//ADD THE TRANSACTION STATUS TO STREAM LINE THE PROCESS
		destination.setStatus(AuthStatusEnums.REGISTER_ACCOUNT_DETAILS.name());
		return destination;
	}

	public PhrUserEntity getUserEntity(UserDTO user) {
		PhrUserEntity userParententity = PhrUserEntity.builder().build();
		BeanUtils.copyProperties(user, userParententity);
//		entity.setProfilePhoto(ImageUtils.compress(user.getProfilePhoto()));
		userParententity.setFullName(
				CommonUtils.populateFullName(user.getFirstName(), user.getMiddleName(), user.getLastName()));
		userParententity.setDateOfBirth(
				CommonUtils.populateDateOfBirth(user.getDayOfBirth(), user.getMonthOfBirth(), user.getYearOfBirth()));
		userParententity.setKycStatus(CommonUtils.calculateKycStatus(user.getKycDocumentType()));
		userParententity.setPhrProvider(CommonUtils.calculatePhrProvider(sanetizePhrAddress(user.getPhrAddress())));
		userParententity.setKycStatus(user.getKycStatus());
		userParententity.setPhrAuthMethodEntity(user.getAuthMode().stream().map(data -> {
			                                     return getPhrAuthMethodEntity(
			                                    		              data.toString()
			                                    		              ,userParententity);
			                                    		                 
		                                        }).collect(Collectors.toSet()));		
		return userParententity;
	}

	@SuppressWarnings("unused")
	private PhrUserEntity breakName(PhrUserEntity userParententity) {
		String firstName = "";
		String lastName = "";
		String middleName = "";

		if (!StringUtils.isEmpty(userParententity.getFullName())) {
			List<String> name = new ArrayList<String>(Arrays.asList(userParententity.getFullName().split(" ")));
			if (name.size() == 1) {
				firstName = name.get(0);
			} else if (name.size() == 2) {
				firstName = name.get(0);
				lastName = name.get(1);
			} else {
				firstName = name.get(0);
				lastName = name.get(name.size() - 1);
				name.remove(0);
				name.remove(name.size() - 1);
				middleName = String.join(" ", name);
			}

		}
		userParententity.setFirstName(CommonUtils.stringTrimmer(firstName));
		userParententity.setLastName(CommonUtils.stringTrimmer(lastName));
		userParententity.setMiddleName(CommonUtils.stringTrimmer(middleName));
		return userParententity;
		
	}

	public PhrAddressEntity populateAddress(UserDTO user) {
		// Populate Address
		PhrAddressEntity phrAddressEntity = PhrAddressEntity.builder().build();
		AddressDTO address = user.getAddress();
		if (Objects.nonNull(address)) {
			BeanUtils.copyProperties(address, phrAddressEntity);

			if (Objects.nonNull(address.getStateCode()) && !address.getStateCode().isEmpty()){
				phrAddressEntity.setStateName(locationService.getStateName(address.getStateCode()));
			}

			if(Objects.nonNull(address.getDistrictCode()) && !address.getDistrictCode().isEmpty()){
				phrAddressEntity.setDistrictName(
						locationService.getDistrictName(address.getStateCode(), address.getDistrictCode()));
			}
		}
		return phrAddressEntity;
	}

	public Set<PhrAuthMethodEntity> getAuthSets(UserDTO user, PhrUserEntity userParententity) {
		// Populate Authentication Method Data
		Set<PhrAuthMethodEntity> setAuthMethods = new HashSet<PhrAuthMethodEntity>();

		if (user.isMobileVerified()) {// Mobile is verified
			setAuthMethods.add(getPhrAuthMethodEntity(AuthMethods.MOBILE_OTP.name(), userParententity));
		}
		if (user.isEmailVerified()) {// Email is verified
			setAuthMethods.add(getPhrAuthMethodEntity(AuthMethods.EMAIL_OTP.name(), userParententity));
		}
		if (StringUtils.isNotBlank(user.getPassword())) {// Password is not Empty
			setAuthMethods.add(getPhrAuthMethodEntity(AuthMethods.PASSWORD.name(), userParententity));
		}
		
		return setAuthMethods;
	}

	private PhrAuthMethodEntity getPhrAuthMethodEntity(String authMethod, PhrUserEntity userParententity) {
		// Set parent reference(userParententity) in childEntity(PhrAuthMethodEntity)
		return PhrAuthMethodEntity.builder().authMethod(authMethod).user(userParententity).build();

	}
}
