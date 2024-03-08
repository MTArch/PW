package com.parserlabs.phr.controller.v1.forget.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.controller.v1.forget.ForgetPasswordByMobileController;
import com.parserlabs.phr.model.request.ForgetPasswordRequest;
import com.parserlabs.phr.model.request.ForgetPasswordVerifyOtp;
import com.parserlabs.phr.model.request.GenerateForgetPassOtpRequest;
import com.parserlabs.phr.model.response.ForgetPassGenOtpTransResponse;
import com.parserlabs.phr.model.response.ForgetPassVerifyOtpResponse;
import com.parserlabs.phr.model.response.SuccessResponse;
import com.parserlabs.phr.service.ForgetPasswordService;
import com.parserlabs.phr.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(FORGET_V1_ENDPOINT)
@AllArgsConstructor
@CrossOrigin
@CustomSpanned
@Slf4j
public class ForgetPassworByMobileControllerImpl implements ForgetPasswordByMobileController{

	
	@Autowired
	private ForgetPasswordService forgetPasswordService;
	
	@Autowired
	private  UserService userService;
	
	@PostMapping("/mobileEmail/generateotp")
	@Override
	public ResponseEntity<ForgetPassGenOtpTransResponse> sendOtpBasedOnPhrAddress(@Valid @RequestBody GenerateForgetPassOtpRequest generateForgetPassOtpReq) {
		return ResponseEntity.ok(forgetPasswordService.sendOtpBasedOnMobileUsingPhr(generateForgetPassOtpReq));
	}
	
	@PostMapping("/mobileEmail/verifyotp")
	@Override
	public ResponseEntity<ForgetPassVerifyOtpResponse> forgetPasswordVerifyOtp(@Valid @RequestBody ForgetPasswordVerifyOtp forgetPassVerifyOtp) {
		return ResponseEntity.ok(forgetPasswordService.forgetPasswordVerifyOtp(forgetPassVerifyOtp));
	}

	@PostMapping("/mobileEmail/update-password")
	@Override
	public ResponseEntity<SuccessResponse> forgetPasswordAndSetNewPassword(@Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest) {
		SuccessResponse forgetPassResp=forgetPasswordService.forgetPasswordAndSetNewPassword(forgetPasswordRequest);
		userService.logout();
		return ResponseEntity.ok(forgetPassResp);
	}

}
