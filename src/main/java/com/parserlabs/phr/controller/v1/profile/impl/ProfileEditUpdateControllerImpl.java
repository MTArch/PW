

/**
 * 
 */
package com.parserlabs.phr.controller.v1.profile.impl;

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
import com.parserlabs.phr.controller.v1.profile.ProfileEditUpdateController;
import com.parserlabs.phr.model.profile.ChangesPasswordRequest;
import com.parserlabs.phr.model.profile.ProfileEditRequest;
import com.parserlabs.phr.model.profile.ProfilePasswordUpdateRequest;
import com.parserlabs.phr.model.profile.ProfilePasswordUpdateRequestFromHid;
import com.parserlabs.phr.model.request.UpdatePhrAttributePayLoad;
import com.parserlabs.phr.model.response.SuccessResponse;
import com.parserlabs.phr.service.ProfileEditService;
import com.parserlabs.phr.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;
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
public class ProfileEditUpdateControllerImpl implements ProfileEditUpdateController {

	@Autowired
	private ProfileEditService profileEditService;
	
	@Autowired
	private  UserService userService;

	@PostMapping("/edit")
	@Override
	public ResponseEntity<SuccessResponse> profileEditUpdate(@RequestBody ProfileEditRequest request) {
		return ResponseEntity.ok(profileEditService.profileEdit(request));
	}

	@PostMapping("/update/password")
	@Override
	public ResponseEntity<SuccessResponse> updateProfilePassword(@RequestBody ProfilePasswordUpdateRequest request) {
		SuccessResponse updatePassResp = profileEditService.profilePasswordEdit(request);
		userService.logout();
		return ResponseEntity.ok(updatePassResp);
	}
	
	@PostMapping("/change/password")
	@Override
	public ResponseEntity<SuccessResponse> changesPassword(@Valid @RequestBody ChangesPasswordRequest request) {
		ProfilePasswordUpdateRequest UpdateRequest = ProfilePasswordUpdateRequest.builder().oldPassword(request.getOldPassword()).password(request.getPassword()).build();
		SuccessResponse updatePassResp=profileEditService.profilePasswordEdit(UpdateRequest);
		userService.logout();
		return ResponseEntity.ok(updatePassResp);
	}

	@PostMapping("/update/passwordFromHid")
	@Override
	public ResponseEntity<SuccessResponse> updateProfilePasswordFromHid(@RequestBody ProfilePasswordUpdateRequestFromHid request) {
		return ResponseEntity.ok(profileEditService.profilePasswordEditFromHid(request));
	}


	@PostMapping("/update/phrAttribute")
	@Hidden
	@Override
	public ResponseEntity<Boolean> updatePhrAttribute(@Valid @RequestBody UpdatePhrAttributePayLoad updatePhrAttributePayLoad)
	{
		log.info("update Attribute",updatePhrAttributePayLoad);
		return ResponseEntity.ok(profileEditService.changePhrAttribute(updatePhrAttributePayLoad));
				
	}

	
	
}
