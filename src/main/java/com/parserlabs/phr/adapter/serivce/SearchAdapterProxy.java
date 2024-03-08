package com.parserlabs.phr.adapter.serivce;

import java.util.Set;
import java.util.stream.Collectors;

import com.parserlabs.phr.model.response.JwtResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.enums.AuthMethods;
import com.parserlabs.phr.exception.HealthIdNumberNotFoundException;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.model.adapter.request.ConfrimLoginRequest;
import com.parserlabs.phr.model.adapter.request.SearchAdapterRequest;
import com.parserlabs.phr.model.adapter.response.SearchInterAdapterResponse;
import com.parserlabs.phr.model.response.SearchResponsePayLoad;
import com.parserlabs.phr.proxy.HealthIdProxy;
import com.parserlabs.phr.proxy.model.SearchHealthIdNumberRequest;
import com.parserlabs.phr.proxy.model.UpdatePhrAddressRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class SearchAdapterProxy {

	private final HealthIdProxy healthIdProxy;

	public SearchResponsePayLoad featchAuthMethod(String healthIdNumber) {
		SearchResponsePayLoad searchResponsePayLoad = null;
		try {
			searchResponsePayLoad = SearchResponsePayLoad.builder().build();
			SearchAdapterRequest searchAdapterRequest = SearchAdapterRequest.builder().healthId(healthIdNumber).build();
			SearchInterAdapterResponse searchInterAdapterResponse = healthIdProxy
					.searchByHealthIdNumber(searchAdapterRequest);
			searchInterAdapterResponse.setAuthMethods(removeAuthMethod(searchInterAdapterResponse.getAuthMethods()));
			log.info("featchAuthMethod ", searchInterAdapterResponse);
			BeanUtils.copyProperties(searchInterAdapterResponse, searchResponsePayLoad);

		} catch (Exception e) {
			throw new HealthIdNumberNotFoundException();
		}
		return searchResponsePayLoad;
	}

	public SearchResponsePayLoad featchAuthMethod(String healthIdNumber, String yearOfBrithDay) {
		SearchResponsePayLoad searchResponsePayLoad = null;
			searchResponsePayLoad = SearchResponsePayLoad.builder().build();
			SearchHealthIdNumberRequest searchAdapterRequest = SearchHealthIdNumberRequest.builder()
					.healthId(healthIdNumber).yearOfBirth(yearOfBrithDay).build();
			SearchInterAdapterResponse searchInterAdapterResponse = healthIdProxy
					.searchHealthIdToLogin(searchAdapterRequest);
			log.info("featchAuthMethod ", searchInterAdapterResponse);
			searchInterAdapterResponse.setAuthMethods(removeAuthMethod(searchInterAdapterResponse.getAuthMethods()));
			BeanUtils.copyProperties(searchInterAdapterResponse, searchResponsePayLoad);
		return searchResponsePayLoad;
	}

	public JwtResponse confirm(String value, String txnId) {

		value = DecryptRSAUtil.encrypt(value, healthIdProxy.fetchCert().getBody());

		ConfrimLoginRequest confrimLoginRequest = ConfrimLoginRequest.builder().value(value).txnId(txnId).build();

		return healthIdProxy.confirmLogin(confrimLoginRequest);

	}

	private Set<String> removeAuthMethod(Set<String> methods) {
		return methods.stream().filter(data -> data.equalsIgnoreCase(AuthMethods.AADHAAR_OTP.name())
				|| data.equalsIgnoreCase(AuthMethods.MOBILE_OTP.name())).collect(Collectors.toSet());

	}

	public String createDefaultPhrAddress(String healthIdNumber) {
		return healthIdProxy.createPhrAddressIfExists(UpdatePhrAddressRequest.builder()
				                                  .healthIdNumber(healthIdNumber).build());		
	}

}
