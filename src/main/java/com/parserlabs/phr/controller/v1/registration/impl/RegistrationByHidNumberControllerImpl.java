package com.parserlabs.phr.controller.v1.registration.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.controller.v1.registration.RegistrationByHidNumberController;
import com.parserlabs.phr.model.adapter.request.SearchPHRWithPHRMetaDetails;
import com.parserlabs.phr.model.adapter.response.HidResponse;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.request.AuthIntRequestPayLoad;
import com.parserlabs.phr.model.request.CreatePHRRequest;
import com.parserlabs.phr.model.request.LoginRequestPayload;
import com.parserlabs.phr.model.request.SearchRequestPayLoad;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.model.response.SearchResponsePayLoad;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.service.AuthenticationService;
import com.parserlabs.phr.service.HealthIdService;
import com.parserlabs.phr.service.RegistrationByMobileOrEmailService;
import com.parserlabs.phr.service.SearchService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suraj
 *
 */
@RestController
@CrossOrigin
@RequestMapping(HID_REGISTRATION_V1_ENDPOINT)
@CustomSpanned
@AllArgsConstructor
@Slf4j
public class RegistrationByHidNumberControllerImpl implements RegistrationByHidNumberController {

	private AuthenticationService authenticationService;
	private HealthIdService healthIdService;
	private RegistrationByMobileOrEmailService registrationService;
	private SearchService searchService;

	@Override
	@PostMapping("/search/auth-methods")
	public ResponseEntity<SearchResponsePayLoad> sequence_1(
			@Valid @RequestBody SearchRequestPayLoad searchRequestPayLoad) {
		return ResponseEntity.ok(searchService.featchAuthMethod(searchRequestPayLoad));
	}

	@PostMapping("/searchPhr")
	public ResponseEntity<Set<String>> fetchPhrAddress(@Valid @RequestBody SearchPHRWithPHRMetaDetails searchPHRWithPHRMetaDetails)
	{
		return ResponseEntity.ok(searchService.fetchPhrLinktoGivenMeta(searchPHRWithPHRMetaDetails));
	}
	
	
	@Override
	@PostMapping("/init/transaction")
	public ResponseEntity<TransactionResponse> sequence_2(
			@Valid @RequestBody AuthIntRequestPayLoad authIntRequest) {
		return ResponseEntity.ok(authenticationService.authInitiate(authIntRequest));
	}

	
	@PostMapping("/init/resendOtp")
	public ResponseEntity<TransactionResponse> sequence_2_2(
			@Valid @RequestBody ResendOTPRequest resendOTPRequest) {
		return ResponseEntity.ok(authenticationService.resendOtp(resendOTPRequest));
	}

	
	@Override
	@PostMapping("/confirm/credential")
	public ResponseEntity<HidResponse> sequence_3(@Valid @RequestBody LoginRequestPayload loginRequestPayload) {
		log.info("sequence_3 is started with {} UUID and client {}", loginRequestPayload.getTransactionId(),
				 PHRContextHolder.clientId());
		return ResponseEntity.ok(healthIdService.fetchUserProfile(loginRequestPayload));
	}
	
	
	@Override
	@PostMapping("/create-phr-address")
	public ResponseEntity<JwtResponse> sequence_4(@Valid @RequestBody CreatePHRRequest createPHRRequest) {
		log.info("sequence_4 is started with {} UUID and client {}", createPHRRequest.getTransactionId(),
				 PHRContextHolder.clientId());
		return ResponseEntity.ok(registrationService.createPHR(createPHRRequest));
	}

}
