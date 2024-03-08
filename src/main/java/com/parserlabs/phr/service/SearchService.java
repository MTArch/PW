package com.parserlabs.phr.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.parserlabs.phr.adapter.serivce.SearchAdapterProxy;
import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.enums.AccountStatus;
import com.parserlabs.phr.exception.PHRStatusNotActive;
import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.adapter.request.SearchPHRWithPHRMetaDetails;
import com.parserlabs.phr.model.profile.User;
import com.parserlabs.phr.model.request.SearchRequestPayLoad;
import com.parserlabs.phr.model.response.SearchResponsePayLoad;
import com.parserlabs.phr.model.search.SearchPhrAuthResponse;
import com.parserlabs.phr.proxy.HealthIdProxy;
import com.parserlabs.phr.proxy.model.SearchByHealthIdRequest;
import com.parserlabs.phr.transform.TransformUserToSearchPhrAuthResponse;
import com.parserlabs.phr.utils.GeneralUtils;

import lombok.AllArgsConstructor;

@Service
@CustomSpanned
@AllArgsConstructor
public class SearchService {

	private final HealthIdProxy healthIdProxy;
	private final UserService userService;
	private final SearchAdapterProxy searchAdapterProxy;

	private final TransformUserToSearchPhrAuthResponse toSearchPhrAuthResponse;

	public boolean doesPhrAddressByHealthIdExists(String phrAddress) {
		boolean doesExist = userService.isPhrAddressAlreadyExist(phrAddress);
		if (!doesExist) {
			Map<String, Boolean> map = healthIdProxy
					.doesUserExist(SearchByHealthIdRequest.builder().healthId(phrAddress).build());
			doesExist = CollectionUtils.isEmpty(map) ? false : map.get("status");
		}
		return doesExist;
	}

	public SearchResponsePayLoad featchAuthMethod(SearchRequestPayLoad searchRequestPayLoad) {
		return searchAdapterProxy.featchAuthMethod(searchRequestPayLoad.getHealhtIdNumber());
	}

	/**
	 * Return the Authentication Methods of the PHR Address.
	 * 
	 * @param phrAddress
	 * @return SearchPhrAuthResponse
	 */
	public SearchPhrAuthResponse getPhrAuthMode(String phrAddress) {
		userService.doesPhrAddressExist(phrAddress);
		UserDTO user = userService.getUser(phrAddress);
		if (!user.getStatus().equalsIgnoreCase(AccountStatus.ACTIVE.name()))
		{
			ErrorAttribute attribute = ErrorAttribute.builder().key("phrAddress").value(phrAddress).build();
			throw new PHRStatusNotActive(attribute);
		}	
		return SearchPhrAuthResponse.builder().phrAddress(GeneralUtils.sanetizePhrAddress(phrAddress))
				.authMethods(user.getAuthMode()).name(user.getFullName()).status(user.getStatus())
				.healthIdNumber(user.getHealthIdNumber()).build();
	}

	/**
	 * Return the list of associated PHR address of the mobile.
	 * 
	 * @param phrAddress
	 * @return SearchPhrAuthResponse
	 */
	public Set<SearchPhrAuthResponse> getPhrAddressListMobile(String mobile) {
		userService.doesPhrAddressExistByMobile(mobile);
		List<UserDTO> users = userService.getUserByMobile(mobile);
		// Populate the response.
		Set<SearchPhrAuthResponse> searchPhrAuthResponseList = users.stream()
				.map(user -> toSearchPhrAuthResponse.apply(user)).collect(Collectors.toSet());
		return searchPhrAuthResponseList;
	}

	/**
	 * Check PHR account exist or not
	 * 
	 * @param phrAddress
	 * @return true/false
	 */
	public Boolean isPhrExists(String phrAddress) {
		return userService.isPhrAddressAlreadyExist(phrAddress)||!userService.getPHRFromHID(new HashSet<>(Arrays.asList(GeneralUtils.sanetizePhrAddress(phrAddress)))).isEmpty();
	}

	public Set<String> fetchPhrLinktoGivenMeta(SearchPHRWithPHRMetaDetails searchPHRWithPHRMetaDetails) {
		return userService.fetchPhrLinkToGivenMeta(searchPHRWithPHRMetaDetails);
	}

	/**
	 * 
	 * @param phrAddress
	 * @return
	 */
	public User searchByPhrAddress(String phrAddress) {
		return userService.searchByPhrAddress(phrAddress);
	}

}
