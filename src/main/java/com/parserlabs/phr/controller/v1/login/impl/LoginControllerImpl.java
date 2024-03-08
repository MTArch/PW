/**
 * 
 */
package com.parserlabs.phr.controller.v1.login.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.controller.v1.login.LoginController;
import com.parserlabs.phr.model.login.mobileemail.LoginPostVerificationRequest;
import com.parserlabs.phr.model.login.mobileemail.LoginPreVerificationRequest;
import com.parserlabs.phr.model.login.mobileemail.LoginPreVerificationResponse;
import com.parserlabs.phr.model.login.phr.LoginViaMobileEmailRequest;
import com.parserlabs.phr.model.login.phr.LoginViaPhrRequest;
import com.parserlabs.phr.model.login.phr.VerifyPasswordOtpLoginRequest;
import com.parserlabs.phr.model.profile.User;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.request.AuthIntRequestPayLoad;
import com.parserlabs.phr.model.request.SearchByHealthIdNumberRequest;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.model.response.SearchResponsePayLoad;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.model.search.SearchPhrAuthResponse;
import com.parserlabs.phr.service.AuthenticationService;
import com.parserlabs.phr.service.LoginService;
import com.parserlabs.phr.service.SearchService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@RestController
@RequestMapping(LOGIN_V1_ENDPOINT)
@AllArgsConstructor
@CustomSpanned
@CrossOrigin
@Slf4j
public class LoginControllerImpl implements LoginController {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private SearchService searchService;


	@PostMapping("/phrAddress/init")
	@Override
	public ResponseEntity<TransactionResponse> initLogin(@RequestBody LoginViaPhrRequest request) {
		return ResponseEntity.ok(loginService.initPhrLogin(request));
	}

	@PostMapping("/phrAddress/verify")
	@Override
	public ResponseEntity<JwtResponse> VerifyLogin(@Valid @RequestBody VerifyPasswordOtpLoginRequest request) {
		log.info("VerifyLogin is started with {} UUID and client {}", request.getTransactionId(),
				 PHRContextHolder.clientId());
		return ResponseEntity.ok(loginService.verifyCredentailsLogin(request));
	}

	@PostMapping("/mobileEmail/init")
	@Override
	public ResponseEntity<TransactionResponse> initLoginViaMobileEmail(@RequestBody LoginViaMobileEmailRequest request) {
		return ResponseEntity.ok(loginService.initMobileEmailLogin(request));
	}

	@PostMapping("/mobileEmail/preVerification")
	@Override
	public ResponseEntity<LoginPreVerificationResponse> preVerificationViaMobileEmail(
			@RequestBody LoginPreVerificationRequest request) {
		log.info("preVerificationViaMobileEmail is started with {} UUID and client {}", request.getTransactionId(),
				 PHRContextHolder.clientId());
		return ResponseEntity.ok(loginService.preVerifyMobileEmailLogin(request));
	}

	@PostMapping("/mobileEmail/getUserToken")
	@Override
	public ResponseEntity<JwtResponse> postVerificationViaMobileEmail(@RequestBody LoginPostVerificationRequest request) {
		log.info("postVerificationViaMobileEmail is started with {} UUID and client {}", request.getTransactionId(),
				 PHRContextHolder.clientId());
		return ResponseEntity.ok(loginService.postVerficationLogin(request));
	}

	
	@PostMapping("/resend/otp")
	@Override
	public ResponseEntity<TransactionResponse> resendOtpLogin(@RequestBody ResendOTPRequest request) {
		log.info("resendOtpLogin is started with {} UUID and client {}", request.getTransactionId(),
				 PHRContextHolder.clientId());
		return ResponseEntity.ok(loginService.regenerateAuthOTP(request));
	}
	
	@Override
	@PostMapping("/search-healthIdNumber")
	public ResponseEntity<SearchResponsePayLoad> SearchByHealthIdNumber(@RequestBody SearchByHealthIdNumberRequest request) {
		return ResponseEntity.ok(loginService.featchAuthMethod(request));
	}
	
	@Override
	@PostMapping("/init/transaction")
	public ResponseEntity<TransactionResponse> healthIdNumberInit(
			@Valid @RequestBody AuthIntRequestPayLoad authIntRequest) {
		return ResponseEntity.ok(authenticationService.authInitiate(authIntRequest));
	}
	
	@GetMapping("/search/authMethods")
	@Override
	public ResponseEntity<SearchPhrAuthResponse> fetchPhrAuthMode(@PathParam("phrAddress") String phrAddress) {
		return ResponseEntity.ok(searchService.getPhrAuthMode(phrAddress));
	}
	

	@GetMapping("/search/userByPhrAddress")
	@Override
	public ResponseEntity<User> searchPhrAdrees(@PathParam("phrAddress") String phrAddress) {
		log.info("LoginController searchPhrAdrees : {}",phrAddress);
		return ResponseEntity.ok(searchService.searchByPhrAddress(phrAddress));
	}
	
	

}
