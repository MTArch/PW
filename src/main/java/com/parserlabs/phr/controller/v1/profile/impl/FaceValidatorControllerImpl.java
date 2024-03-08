
/**
 * 
 */
package com.parserlabs.phr.controller.v1.profile.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.controller.v1.profile.FaceValidatorController;
import com.parserlabs.phr.model.face.FaceValidationRequest;
import com.parserlabs.phr.model.face.FaceValidationResponse;
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
public class FaceValidatorControllerImpl implements FaceValidatorController {

	@Autowired
	private ProfileEditService profileEditService;

	//@PostMapping("/faceValidator")
	@Override
	public ResponseEntity<FaceValidationResponse> valideProfilePhotoFace(@RequestBody FaceValidationRequest request) {
		return ResponseEntity.ok(profileEditService.validateFaceInPhoto(request));
	}

}
