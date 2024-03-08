package com.parserlabs.phr.transform;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrKycEntity;
import com.parserlabs.phr.entity.PhrTransactionEntity;

@Component
@CustomSpanned
public class TransFormTransationToKyc implements Function<PhrTransactionEntity, PhrKycEntity> {

	@Override
	public PhrKycEntity apply(PhrTransactionEntity phrTransaction) {
		return PhrKycEntity.builder().healthIdNumber(phrTransaction.getHealthIdNumber())
				.phrAddress(phrTransaction.getPhrAddress()).yearOfBirth(phrTransaction.getYearOfBirth())
				.dateOfBirth(phrTransaction.getDateOfBirth()).monthOfBirth(phrTransaction.getMonthOfBirth())
				.firstName(phrTransaction.getFirstName()).middleName(phrTransaction.getMiddleName())
				.lastName(phrTransaction.getLastName()).kycDocumentType(phrTransaction.getKycType())
				.kycStatus(phrTransaction.getKycStatus()).fullName(phrTransaction.getFirstName())
				.stateCode(phrTransaction.getStateCode()).districtCode(phrTransaction.getDistrictCode())
				.stateName(phrTransaction.getStateName()).districtName(phrTransaction.getDistrictName())
				.profilePhoto(phrTransaction.getProfilePhoto()).subDistrictCode(phrTransaction.getSubDistrictCode())
				.subDistrictName(phrTransaction.getSubDistrictName()).build();
	}

}
