package com.parserlabs.phr.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.parserlabs.phr.adapter.serivce.AuthAdapterProxy;
import com.parserlabs.phr.adapter.serivce.HidRegistrationProxy;
import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.entity.PhrAuthTransactionEntity;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.enums.AuthMethods;
import com.parserlabs.phr.enums.AuthStatusEnums;
import com.parserlabs.phr.model.adapter.response.HidResponse;
import com.parserlabs.phr.model.adapter.response.PHRLinkResponse;
import com.parserlabs.phr.model.request.LoginRequestPayload;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.transform.TansformTransactionToHidResponse;

import lombok.AllArgsConstructor;

@Service
@CustomSpanned
@AllArgsConstructor
public class HealthIdService {

	private final AuthAdapterProxy authAdapterProxy;

	private final HidRegistrationProxy hidRegistrationProxy;

	private final TansformTransactionToHidResponse tansformTransactionToHidResponse;

	private final AuthenticationService authTransactionService;

	private final TransactionService transactionService;

	public HidResponse fetchUserProfile(LoginRequestPayload loginRequestPayload) {

		PhrAuthTransactionEntity authTransactionEntity = authTransactionService
				.fetchAuthTransactionEntitybyId(loginRequestPayload.getTransactionId(),"fetchUserProfile");
		loginRequestPayload.setTransactionId(authTransactionEntity.getHealthIdTransaction());
		HidResponse hidResponse = null;
		JwtResponse tokenResponse = authAdapterProxy.confirmLogin(loginRequestPayload);
		if (Objects.nonNull(tokenResponse)) {
			PHRContextHolder.healthIdUserToken(tokenResponse.getToken());
			hidResponse = featchUserDetailsAfterAuthenticated();
			PhrTransactionEntity transaction = tansformTransactionToHidResponse.apply(hidResponse);
			transaction.setAuthTransactionId(UUID.fromString(loginRequestPayload.getTransactionId()));
			transaction.setStatus(AuthStatusEnums.REGISTER_ACCOUNT_DETAILS.name());
			transaction = transactionService.save(transaction);
			hidResponse.setTransactionId(transaction.getAuthTransactionId().toString());
			int numberOfPhrAdressCount = Objects.nonNull(hidResponse.getPhrAddress())
					? hidResponse.getPhrAddress().size()
					: 0;
			hidResponse.setLinkedPhrAddess(numberOfPhrAdressCount);
			hidResponse.getAuthMethods().stream()
					.filter(data -> data.name().equalsIgnoreCase(AuthMethods.AADHAAR_OTP.name())
							|| data.name().equalsIgnoreCase(AuthMethods.MOBILE_OTP.name()))
					.collect(Collectors.toSet());

		}
		return hidResponse;
	}

	public PHRLinkResponse fetchUserProfileToLinkPhrAddress(String jwtToken) {

		PHRContextHolder.healthIdUserToken(jwtToken);
		HidResponse hidResponse = hidRegistrationProxy.featchUserProfile();
		PhrTransactionEntity transaction = tansformTransactionToHidResponse.apply(hidResponse);
		transaction = transactionService.save(transaction);

		return PHRLinkResponse.builder().name(hidResponse.getName()).gender(hidResponse.getGender())
				.address(hidResponse.getAddress()).districtName(hidResponse.getDistrictName())
				.stateName(hidResponse.getStateName()).dayOfBirth(hidResponse.getDayOfBirth())
				.monthOfBirth(hidResponse.getMonthOfBirth()).yearOfBirth(hidResponse.getYearOfBirth())
				.transactionId(transaction.getTransactionId().toString()).build();
	}

	public HidResponse featchUserDetailsAfterAuthenticated() {
		return hidRegistrationProxy.featchUserProfile();
	}

	public Map<String, Boolean> link(String action, String phrAddress, String healthIdNumber) {
		return authAdapterProxy.linkageProcess(action, phrAddress, healthIdNumber);
	}
	
	public List<String> getPHRFromHID(Set<String> abhaList) {
		return hidRegistrationProxy.getPHRFromHID(abhaList);
	}

}
