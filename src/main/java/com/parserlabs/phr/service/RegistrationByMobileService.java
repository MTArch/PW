package com.parserlabs.phr.service;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.enums.AccountStatus;
import com.parserlabs.phr.enums.KycType;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.registration.VerifyOTPRequest;
import com.parserlabs.phr.model.request.RegistrationByMobileOrEmailRequest;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.proxy.HealthIdProxy;
import com.parserlabs.phr.proxy.model.CreateAccountByVerifiedMobileRequest;
import com.parserlabs.phr.proxy.model.CreateAccountByVerifiedMobileResponse;
import com.parserlabs.phr.utils.CommonUtils;

import lombok.AllArgsConstructor;

@Service
@CustomSpanned
@AllArgsConstructor
public class RegistrationByMobileService {

	private HealthIdProxy healthIdProxy;
	private UserService userService;

	public TransactionResponse generateOTP(GenerateOTPRequest generateOtpRequest) {
		return healthIdProxy.generateOTP(generateOtpRequest);
	}

	public Boolean resendOTP(ResendOTPRequest resendRequest) {
		return healthIdProxy.resentOTP(resendRequest);
	}

	public JwtResponse verifyOTP(@RequestBody VerifyOTPRequest verifyOtpRequest) {
		return healthIdProxy.verifyOTP(verifyOtpRequest);
	}

	public UserDTO createPhrUsingMobile(RegistrationByMobileOrEmailRequest request) {
		UserDTO user = null;
		CreateAccountByVerifiedMobileResponse response = healthIdProxy
				.createPhrUsingMobile(populateCreateAccountByVerifiedMobileRequest(request));

		if (Objects.nonNull(response) && StringUtils.hasText(response.getToken())) {
			user = UserDTO.builder().build();
			BeanUtils.copyProperties(request, user);
			user.setMobile(response.getMobile());
			user.setHealthIdNumber(response.getHealthIdNumber());
			user.setKycDocumentType(KycType.MOBILE.name());
			user.setStatus(AccountStatus.ACTIVE.name());

			user = userService.save(user);
			user.setToken(response.getToken());
		}
		return user;
	}

	private CreateAccountByVerifiedMobileRequest populateCreateAccountByVerifiedMobileRequest(
			RegistrationByMobileOrEmailRequest arg1) {

		CreateAccountByVerifiedMobileRequest mobileRequest = CreateAccountByVerifiedMobileRequest.builder().build();
		BeanUtils.copyProperties(arg1, mobileRequest);

//		mobileRequest.setHealthId(arg1.getPhrAddress());
		mobileRequest
				.setName(CommonUtils.populateFullName(arg1.getFirstName(), arg1.getMiddleName(), arg1.getLastName()));

		// Setting address
//		Address address = arg1.getAddress();
//		if (Objects.nonNull(address)) {
//			mobileRequest.setStateCode(address.getStateCode());
//			mobileRequest.setDistrictCode(address.getDistrictCode());
//			mobileRequest.setAddress(address.getAddressLine());
//		}
		return mobileRequest;
	}
}
