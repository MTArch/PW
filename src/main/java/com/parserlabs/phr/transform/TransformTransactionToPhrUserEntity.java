package com.parserlabs.phr.transform;

import java.util.Objects;
import java.util.function.Function;

import com.parserlabs.phr.service.S3StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrAddressEntity;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.entity.PhrUserEntity;
import com.parserlabs.phr.model.response.DeletedUserInfoResponse;

@Component
@CustomSpanned
@Slf4j
public class TransformTransactionToPhrUserEntity implements Function<PhrTransactionEntity, PhrUserEntity> {

	@Autowired
	private S3StorageService s3StorageService;

	@Override
	public PhrUserEntity apply(PhrTransactionEntity transactionEntity) {
		return null;
	}

	public PhrUserEntity trasnform(PhrTransactionEntity transactionEntity, PhrUserEntity phrUserEntity) {
		phrUserEntity.setFullName(transactionEntity.getFullName());
		phrUserEntity.setHealthIdNumber(transactionEntity.getHealthIdNumber());
		phrUserEntity.setYearOfBirth(transactionEntity.getYearOfBirth());
		phrUserEntity.setDateOfBirth(transactionEntity.getDateOfBirth());
		phrUserEntity.setMonthOfBirth(transactionEntity.getMonthOfBirth());
		phrUserEntity.setDayOfBirth(transactionEntity.getDayOfBirth());
		phrUserEntity.setFirstName(transactionEntity.getFirstName());
		phrUserEntity.setMiddleName(transactionEntity.getMiddleName());
		phrUserEntity.setLastName(transactionEntity.getLastName());
		phrUserEntity.setGender(transactionEntity.getGender());

		if (!StringUtils.hasLength(phrUserEntity.getMobile())) {
			phrUserEntity.setMobile(transactionEntity.getMobile());
		}

		if (!StringUtils.hasLength(phrUserEntity.getEmail())) {
			phrUserEntity.setEmail(transactionEntity.getEmail());
		}

		if (!phrUserEntity.isEmailVerified()) {
			phrUserEntity.setEmailVerified(transactionEntity.getEmailVerified());
		}

		if (!phrUserEntity.isMobileVerified()) {
			phrUserEntity.setMobileVerified(transactionEntity.getMobileVerified());
		}

		String profilePhoto = StringUtils.hasLength(phrUserEntity.getProfilePhoto()) ? phrUserEntity.getProfilePhoto()
				: transactionEntity.getProfilePhoto();
		if (Objects.nonNull(transactionEntity.getProfilePhoto())&& transactionEntity.getProfilePhoto().getBytes().length > 0) {
			Boolean isPhotoEncoded = true;
			String uploadKey = s3StorageService.setProfilePhoto(String.valueOf(phrUserEntity.getId()), transactionEntity.getProfilePhoto().getBytes(), isPhotoEncoded);
			log.info("----------photo-id:" + uploadKey);
			phrUserEntity.setProfilePhoto(null);
		}
		phrUserEntity.setKycStatus(transactionEntity.getKycStatus());
		phrUserEntity.setAddress(setPhrAddressEntity(transactionEntity, phrUserEntity));
		return phrUserEntity;
	}

	private PhrAddressEntity setPhrAddressEntity(PhrTransactionEntity txn, PhrUserEntity phrUserEntity) {
		PhrAddressEntity address = phrUserEntity.getAddress();
		address.setAddressLine(txn.getAddressLine());
		address.setStateCode(txn.getStateCode());
		address.setStateName(txn.getStateName());
		address.setDistrictCode(txn.getDistrictCode());
		address.setDistrictName(txn.getDistrictName());
		address.setWardCode(txn.getWardCode());
		address.setUser(phrUserEntity);
		address.setWardName(txn.getWardName());
		address.setPincode(txn.getPincode());
		return address;
	}

	public DeletedUserInfoResponse tranform(PhrUserEntity phrUserEntity) {
		return DeletedUserInfoResponse.builder().healthIdNumber(phrUserEntity.getHealthIdNumber())
				.fullName(phrUserEntity.getFullName()).kycStatus(phrUserEntity.getKycStatus())
				.phrAddress(phrUserEntity.getPhrAddress()).build();
	}

}
