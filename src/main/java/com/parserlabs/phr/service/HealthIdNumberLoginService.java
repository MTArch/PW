package com.parserlabs.phr.service;

import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.parserlabs.phr.adapter.serivce.SearchAdapterProxy;
import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrAuthTransactionEntity;
import com.parserlabs.phr.enums.AuthStatusEnums;
import com.parserlabs.phr.model.request.SearchByHealthIdNumberRequest;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.model.response.SearchResponsePayLoad;
import com.parserlabs.phr.utils.GeneralUtils;

import lombok.AllArgsConstructor;

@Service
@CustomSpanned
@AllArgsConstructor
public class HealthIdNumberLoginService {

	private final UserService userService;

	private final SearchAdapterProxy searchAdapterProxy;

	private AuthenticationService authenticationService;

	public SearchResponsePayLoad featchAuthMethod(SearchByHealthIdNumberRequest request) {
        
		boolean hidExistFlag = userService.doesHealhtIdNumberExistWithoutException(GeneralUtils.actualHealthIdNumber(request.getHealthIdNumber()), request.getYearOfBirth());
        
		if (!hidExistFlag)
		{
			searchAdapterProxy.createDefaultPhrAddress(request.getHealthIdNumber());	
		}	
		
		return searchAdapterProxy.featchAuthMethod(request.getHealthIdNumber(), request.getYearOfBirth());

	}

	public Set<String> confirm(String value, PhrAuthTransactionEntity authTransactionEntity) {

		JwtResponse tokenResponse = searchAdapterProxy.confirm(value, authTransactionEntity.getHealthIdTransaction());
		Set<String> phrAddress = null;

		if (Objects.nonNull(tokenResponse)) {
			phrAddress = userService.fetchPhrAddress(authTransactionEntity.getHealthIdNumber());
			authTransactionEntity.setStatus(AuthStatusEnums.OTP_VERIFIED.name());
			authTransactionEntity.setReferenceToken(tokenResponse.getToken());
			authenticationService.saveAuthTransaction(authTransactionEntity);
		}
		return phrAddress;
	}

}
