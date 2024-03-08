package com.parserlabs.phr.transform;

import java.util.function.Function;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.enums.AuthMethods;
import com.parserlabs.phr.enums.KycStatus;
import com.parserlabs.phr.enums.KycType;
import com.parserlabs.phr.model.adapter.response.HidResponse;
import com.parserlabs.phr.utils.GeneralUtils;

@Component
@CustomSpanned
public class TansformTransactionToHidResponse implements Function<HidResponse, PhrTransactionEntity> {

	@Override
	public PhrTransactionEntity apply(HidResponse hidResponse) {
		PhrTransactionEntity phrTransactionEntity = PhrTransactionEntity.builder().build();
		BeanUtils.copyProperties(hidResponse, phrTransactionEntity);
		phrTransactionEntity.setKycType(KycType.kycType(hidResponse.getVerificationType()));
		phrTransactionEntity.setFullName(hidResponse.getName());
		phrTransactionEntity.setAddressLine(hidResponse.getAddress());
		phrTransactionEntity.setHidToken(PHRContextHolder.healthIdUserToken());
		phrTransactionEntity.setReferenceId(hidResponse.getTransactionId());
		phrTransactionEntity.setDateOfBirth(GeneralUtils.populateDOB(hidResponse.getDayOfBirth(), hidResponse.getMonthOfBirth(), hidResponse.getYearOfBirth()));
		phrTransactionEntity.setKycStatus(KycStatus.VERIFIED.name());
		phrTransactionEntity.setEmailVerified(hidResponse.isEmailVerified());
		phrTransactionEntity.setEmail(hidResponse.getEmail());
		phrTransactionEntity.setDayOfBirth(hidResponse.getDayOfBirth());
		phrTransactionEntity.setMonthOfBirth(hidResponse.getMonthOfBirth());
		phrTransactionEntity.setYearOfBirth(hidResponse.getYearOfBirth());		
		phrTransactionEntity.setMobileVerified(hidResponse.getAuthMethods().contains(AuthMethods.MOBILE_OTP));
		phrTransactionEntity.setAddressLine(hidResponse.getAddress());;
		phrTransactionEntity.setDistrictCode(hidResponse.getDistrictCode());
		phrTransactionEntity.setStateCode(hidResponse.getStateCode());
		phrTransactionEntity.setStateName(hidResponse.getStateName());
		phrTransactionEntity.setDistrictName(hidResponse.getDistrictName());
		phrTransactionEntity.setPincode(hidResponse.getPincode());
		return phrTransactionEntity;
	}
	

	
}
