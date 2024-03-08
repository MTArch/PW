/**
 * 
 */
package com.parserlabs.phr.controller.v1.profile.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.controller.v1.profile.MobileEmailUpdateController;
import com.parserlabs.phr.model.profile.UpdateProfileRequest;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.registration.VerifyOTPRequest;
import com.parserlabs.phr.model.response.SuccessResponse;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.service.ProfileEditService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */
@RestController
@RequestMapping(PROFILE_V1_ENDPOINT)
@AllArgsConstructor
@CustomSpanned
@CrossOrigin
@Slf4j
public class MobileEmailUpdateControllerImpl implements MobileEmailUpdateController {

	@Autowired
	private ProfileEditService editService;

	@PostMapping("/update/mobileEmail/new/generateOTP")
	@Override
	public ResponseEntity<TransactionResponse> generateOTP(@RequestBody GenerateOTPRequest request) {
		return ResponseEntity.ok(editService.generateOTP(request));
	}

	@PostMapping("/update/mobileEmail/new/verifyOTP")
	@Override
	public ResponseEntity<TransactionResponse> verifyMobileEmailOTP(@RequestBody VerifyOTPRequest request) {
		 log.info("verifyMobileEmailOTP is started with {} UUID and client {}", request.getTransactionId(),
                 PHRContextHolder.clientId());
		return ResponseEntity.ok(editService.verifyOTP(request));
	}

	@PostMapping("/update/mobileEmail/update")
	@Override
	public ResponseEntity<SuccessResponse> updateMobileEmail(@RequestBody UpdateProfileRequest request) {
		 log.info("updateMobileEmail is started with {} UUID and client {}", request.getTransactionId(),
                 PHRContextHolder.clientId());
		return ResponseEntity.ok(editService.updateMobileEmail(request));
	}

	@PostMapping("/update/mobileEmail/new/resendOTP")
	@Override
	public ResponseEntity<SuccessResponse> resendMobileEmailOTP(@RequestBody ResendOTPRequest request) {
		 log.info("resendMobileEmailOTP is started with {} UUID and client {}", request.getTransactionId(),
			                               PHRContextHolder.clientId());
		return ResponseEntity.ok(editService.resendOTP(request));
	}
}
