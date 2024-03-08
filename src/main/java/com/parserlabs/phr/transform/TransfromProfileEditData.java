/**
 * 
 */
package com.parserlabs.phr.transform;

import java.util.Base64;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrAddressEntity;
import com.parserlabs.phr.entity.PhrUserEntity;
import com.parserlabs.phr.enums.KycStatus;
import com.parserlabs.phr.model.profile.ProfileEditRequest;
import com.parserlabs.phr.service.LocationService;
import com.parserlabs.phr.utils.CommonUtils;

/**
 * @author Rajesh
 *
 */
@Component
@CustomSpanned
public class TransfromProfileEditData {

	@Autowired
	private LocationService locationService;

	public PhrUserEntity getUpdatePhrUserEntity(PhrUserEntity phrUser, ProfileEditRequest request) {

		PhrAddressEntity phrAddressEntity = phrUser.getAddress();
		if (StringUtils.hasLength(request.getStateCode())) {
			phrAddressEntity.setStateCode(request.getStateCode());
			phrAddressEntity.setStateName(locationService.getStateName(request.getStateCode()));
		}

		if (StringUtils.hasLength(request.getPinCode())) {
			phrAddressEntity.setPincode(request.getPinCode());
		}

		if (StringUtils.hasLength(request.getDistrictCode())) {
			phrAddressEntity.setDistrictCode(request.getDistrictCode());
			phrAddressEntity.setDistrictName(
					locationService.getDistrictName(request.getStateCode(), request.getDistrictCode()));
		}
		phrAddressEntity.setAddressLine(request.getAddressLine());

		phrUser.setAddress(phrAddressEntity);


		if (KycStatus.PENDING.name().equalsIgnoreCase(phrUser.getKycStatus())) {
			BeanUtils.copyProperties(request, phrUser);
			phrUser.setFullName(CommonUtils.populateFullName(request.getFirstName(), request.getMiddleName(),
					request.getLastName()));
			phrUser.setDateOfBirth(CommonUtils.populateDateOfBirth(request.getDayOfBirth(), request.getMonthOfBirth(),
					request.getYearOfBirth()));
			if (Objects.nonNull(request.getProfilePhoto()) && request.getProfilePhoto().length > 0) {
				phrUser.setProfilePhoto(Base64.getEncoder().encodeToString(request.getProfilePhoto()));
			}
		}
		return phrUser;
	}

}
