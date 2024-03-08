package com.parserlabs.phr.controller.v1.registration.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.controller.v1.registration.RegistrationByMobileController;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.registration.VerifyOTPRequest;
import com.parserlabs.phr.model.request.CreatePHRFromMobileRequest;
import com.parserlabs.phr.model.request.CreatePHRRequest;
import com.parserlabs.phr.model.request.PHRSuggestionRequst;
import com.parserlabs.phr.model.request.RegistrationByMobileOrEmailRequest;
import com.parserlabs.phr.model.request.UpdatePhrProfilePhoto;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.model.response.SuccessResponse;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.model.response.TransactionWithPHRResponse;
import com.parserlabs.phr.service.RegistrationByMobileOrEmailService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(MOBILE_OR_EMAIL_REGISTRATION_V1_ENDPOINT)
@AllArgsConstructor
@CustomSpanned
@CrossOrigin
@Slf4j
public class RegistrationByMobileControllerImpl implements RegistrationByMobileController {

	private final RegistrationByMobileOrEmailService registrationService;

	@PostMapping("/generate/otp")
	public ResponseEntity<TransactionResponse> generateOTP(@RequestBody GenerateOTPRequest request) {
		return ResponseEntity.ok(registrationService.generateOTP(request));
	}

	@PostMapping("/resend/otp")
	public ResponseEntity<SuccessResponse> resendOTP(@RequestBody ResendOTPRequest request) {
		log.info("resendOTP is started with {} UUID and client {} and rquest payload {}", request.getTransactionId(),
				 PHRContextHolder.clientId(),request);
		return ResponseEntity.ok(registrationService.resendOTP(request));
	}

	@PostMapping("/details")
	public ResponseEntity<TransactionResponse> registerDetails(
			@RequestBody @Valid RegistrationByMobileOrEmailRequest request) {
		log.info("registerDetails is started with {} UUID and client {} and rquest payload {}", request.getTransactionId(),
				 PHRContextHolder.clientId(), request);	
		return ResponseEntity.ok(registrationService.registerDetails(request));
	}

	@PostMapping("/create/phr")
	public ResponseEntity<JwtResponse> createPHR(@RequestBody CreatePHRRequest request) {
		log.info("createPHR is started with {} UUID and client {} and rquest payload {}", request.getTransactionId(),
				 PHRContextHolder.clientId(),request);
		return ResponseEntity.ok(registrationService.createPHR(request));
	}

	@PostMapping("/create/phr/HidMobile")
	@ApiOperation(value = "create phr in  phr system", hidden=true)
	public ResponseEntity<JwtResponse> createPHRFromHidSystem(@RequestBody CreatePHRFromMobileRequest request) {
		return ResponseEntity.ok(registrationService.createPHRForHIDMobileRegistration(request,false));
	}

	@PostMapping("/create/phr/migration")
	@ApiOperation(value = "create phr in  phr system for migration", hidden=true)
	public ResponseEntity<JwtResponse> dataMigration(@RequestBody CreatePHRFromMobileRequest request) {
		return ResponseEntity.ok(registrationService.createPHRForHIDMobileRegistration(request,true));
	}

	@PostMapping("/update/phr/photo/migration")
	@ApiOperation(value = " migration of profile photo", hidden=true)
	public ResponseEntity<Boolean> updatePhrProfilePhotoAttribute( @RequestBody UpdatePhrProfilePhoto updatePhrProfilePhoto)
	{
		return ResponseEntity.ok(registrationService.changePhrAttribute(updatePhrProfilePhoto));
				
	}

	
	
	@PostMapping("/phr/suggestion")
	public ResponseEntity<List<String>> getPHRSuggestion(@RequestBody PHRSuggestionRequst request) {
		return ResponseEntity.ok(registrationService.getPHRSuggestion(request));
	}	
	
	@PostMapping("/verify/otp")
	public ResponseEntity<TransactionWithPHRResponse> verifyOTP(@Valid @RequestBody VerifyOTPRequest request) {
		log.info("verifyOTP is started with {} UUID and client {} and rquest payload {}", request.getTransactionId(),
				 PHRContextHolder.clientId(),request);
		return ResponseEntity.ok(registrationService.verifyOTP(request));
	}
	
	
}
