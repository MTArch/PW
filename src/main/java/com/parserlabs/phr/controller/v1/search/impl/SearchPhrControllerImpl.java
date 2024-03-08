/**
 * 
 */
package com.parserlabs.phr.controller.v1.search.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.controller.v1.search.SearchPhrController;
import com.parserlabs.phr.enums.LoginMethodsEnum;
import com.parserlabs.phr.model.profile.User;
import com.parserlabs.phr.model.search.SearchPhrAuthResponse;
import com.parserlabs.phr.service.SearchService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */

@RestController
@RequestMapping(SEARCH_V1_ENDPOINT)
@AllArgsConstructor
@CrossOrigin
@CustomSpanned
@Slf4j
public class SearchPhrControllerImpl implements SearchPhrController {
	@Autowired
	private SearchService searchService;
	

	@GetMapping("/authMethods")
	@Override
	public ResponseEntity<SearchPhrAuthResponse> fetchPhrAuthMode(@PathParam("phrAddress") String phrAddress) {
		SearchPhrAuthResponse searchPhrAuthResp=searchService.getPhrAuthMode(phrAddress);
		if(!searchPhrAuthResp.getAuthMethods().isEmpty())
		{
			searchPhrAuthResp.getAuthMethods().remove(LoginMethodsEnum.AADHAAR_OTP);
		}
		return ResponseEntity.ok(searchPhrAuthResp);
	}

	
	@GetMapping("/isExist")
	@Override
	public ResponseEntity<Boolean> isPhrExists(String phrAddress) {
		return ResponseEntity.ok(searchService.isPhrExists(phrAddress));
	}
	
	@GetMapping("/search/userByPhrAddress")
	public ResponseEntity<User> searchPhrAdrees(@PathParam("phrAddress") String phrAddress) {
		log.info("SearchPhrController searchPhrAdrees : {}",phrAddress);
		return ResponseEntity.ok(searchService.searchByPhrAddress(phrAddress));
	}

}
