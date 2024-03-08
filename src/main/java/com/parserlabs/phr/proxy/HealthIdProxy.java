package com.parserlabs.phr.proxy;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.config.HIDClientConfig;
import com.parserlabs.phr.model.District;
import com.parserlabs.phr.model.States;
import com.parserlabs.phr.model.adapter.request.ConfrimLoginRequest;
import com.parserlabs.phr.model.adapter.request.SearchAdapterRequest;
import com.parserlabs.phr.model.adapter.request.TransactionInitRequestPayLoad;
import com.parserlabs.phr.model.adapter.response.HidResponse;
import com.parserlabs.phr.model.adapter.response.HidTransaction;
import com.parserlabs.phr.model.adapter.response.SearchInterAdapterResponse;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.registration.ResendLoginOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.registration.VerifyOTPRequest;
import com.parserlabs.phr.model.request.CreatePHRFromMobileRequest;
import com.parserlabs.phr.model.request.GenerateAadhaarOTPRequest;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.proxy.model.CreateAccountByVerifiedMobileRequest;
import com.parserlabs.phr.proxy.model.CreateAccountByVerifiedMobileResponse;
import com.parserlabs.phr.proxy.model.PhrDeLinkedRequestPayLoad;
import com.parserlabs.phr.proxy.model.PhrLinkedOrDeLinkedRequestPayLoad;
import com.parserlabs.phr.proxy.model.SearchByHealthIdRequest;
import com.parserlabs.phr.proxy.model.SearchHealthIdNumberRequest;
import com.parserlabs.phr.proxy.model.UpdatePhrAddressRequest;

@FeignClient(value = "health-id-proxy", url = "${health.id.service.url}", configuration = HIDClientConfig.class)
@CustomSpanned
public interface HealthIdProxy {

	@PostMapping("v1/registration/mobile/generateOtp")
	TransactionResponse generateOTP(@RequestBody GenerateOTPRequest request);

	@PostMapping("/v1/registration/mobile/resendOtp")
	Boolean resentOTP(@RequestBody ResendOTPRequest request);

	@PostMapping("/v1/registration/mobile/verifyOtp")
	JwtResponse verifyOTP(@RequestBody VerifyOTPRequest request);

	@PostMapping("/v1/registration/mobile/createHealthId")
	CreateAccountByVerifiedMobileResponse createPhrUsingMobile(

			@RequestBody CreateAccountByVerifiedMobileRequest request);

	@GetMapping("/v1/ha/lgd/phr/districts")
	List<District> getDistrictsInState(@RequestParam String stateCode);

	@GetMapping(value = "/v1/ha/lgd/phr/states")
	List<States> getStates();

	@PostMapping(value = "/v1/search/existsByHealthId")
	Map<String, Boolean> doesUserExist(@RequestBody SearchByHealthIdRequest searchDTO);

	@PostMapping("/v2/registration/aadhaar/generateOtp")
	ResponseEntity<TransactionResponse> generatereAadhaarOTP(@RequestBody GenerateAadhaarOTPRequest request);

	@PostMapping("/v2/registration/aadhaar/verifyOTP")
	ResponseEntity<TransactionResponse> verifyAadharOTPOnly(@RequestBody VerifyOTPRequest request);

	@PostMapping("/v2/search/searchByHealthId")
	SearchInterAdapterResponse searchByHealthIdNumber(@RequestBody SearchAdapterRequest SearchAdapterRequest);

	@PostMapping("/v2/auth/init")
	HidTransaction authInitiate(@RequestBody TransactionInitRequestPayLoad transactionInitRequestPayLoad);
	
	@PostMapping("/v2/auth/resendAuthOTP")
	boolean resendOpt(@RequestBody ResendLoginOTPRequest request);
	
	@PostMapping("/v2/auth/login")
	JwtResponse confirmLogin(@RequestBody ConfrimLoginRequest confrimLoginRequest);

	@GetMapping("/v2/account/profile")
	HidResponse fetchUserProfile();

	@GetMapping("/v2/auth/cert")
	ResponseEntity<String> fetchCert();

	@PostMapping("/v2/search/searchHealthIdToLogin")
	SearchInterAdapterResponse searchHealthIdToLogin(@RequestBody SearchHealthIdNumberRequest SearchAdapterRequest);

	@PostMapping("/v2/account/phr-linked")
	Map<String, Boolean> linked(@RequestBody PhrLinkedOrDeLinkedRequestPayLoad request);

	@PostMapping("/v2/account/phr/delinked")
	Map<String, Boolean> delink(@RequestBody PhrDeLinkedRequestPayLoad request);

	@PostMapping("/v2/hid/benefit/update/phr-address")
	String createPhrAddressIfExists(@RequestBody UpdatePhrAddressRequest request);
	
	@GetMapping("/v2/search/userByPhrAddress")
	CreatePHRFromMobileRequest getAbhaAddressDataUsingPhr(@RequestParam("phrAddress") String phrAddress);

	@PostMapping("/v1/hid/benefit/phr/suggestion/exist")
	List<String> getExistAbhaList(@RequestBody Set<String> abhaList);

}
