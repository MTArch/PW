package com.parserlabs.phr.controller.v1.profile.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;
import static com.parserlabs.phr.utils.PhrCardHelper.*;

import java.io.IOException;
import java.util.Map;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;
import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.annotation.NotBlank;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.controller.v1.profile.ProfileController;
import com.parserlabs.phr.enums.AccountAction;
import com.parserlabs.phr.model.adapter.response.PHRLinkResponse;
import com.parserlabs.phr.model.authentication.ValidateTokenRequest;
import com.parserlabs.phr.model.login.phr.DeletePhrRequest;
import com.parserlabs.phr.model.profile.User;
import com.parserlabs.phr.model.request.LinkedHidPlayLoad;
import com.parserlabs.phr.model.request.LinkedPlayLoad;
import com.parserlabs.phr.model.request.PhrGenerateOtpRequest;
import com.parserlabs.phr.model.response.DeletedUserInfoResponse;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.service.AbhaCardService;
import com.parserlabs.phr.service.HealthIdService;
import com.parserlabs.phr.service.UserService;
import com.parserlabs.phr.utils.GeneralUtils;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(PROFILE_V1_ENDPOINT)
@AllArgsConstructor
@CustomSpanned
@CrossOrigin
public class ProfileControllerImpl implements ProfileController {

	private final UserService userService;

	@Autowired
	private AbhaCardService phrCardHelper;

	private HealthIdService healthIdService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> get() {
		return ResponseEntity.ok(userService.getUserProfile());
	}

//	@DeleteMapping("/delete")
	public ResponseEntity<DeletedUserInfoResponse> delete(@RequestBody @Valid DeletePhrRequest request) {
		DeletedUserInfoResponse delUserResp=userService.delete(request);
		userService.logout();
		return ResponseEntity.ok(delUserResp);
	}

	@PostMapping("/generateOtp")
	@Override
	public ResponseEntity<TransactionResponse> initLogin(@RequestBody PhrGenerateOtpRequest request) {
		return ResponseEntity.ok(userService.phrGenerateOtp(request));
	}

	@PostMapping("/link/hid")
	@Override
	public ResponseEntity<Map<String, Boolean>> link(@Valid @RequestBody LinkedPlayLoad linkedPlayLoad) {
		return ResponseEntity.ok(userService.linked(linkedPlayLoad));
	}

	@PostMapping("/link/phr/hidtoken")
	@Override
	public ResponseEntity<Map<String, Boolean>> linkHidToken(@Valid @RequestBody LinkedHidPlayLoad linkedHidPlayLoad) {
		LinkedPlayLoad linkedPlayLoad = LinkedPlayLoad.builder().action(AccountAction.LINK)
				.transactionId(linkedHidPlayLoad.getTransactionId()).build();
		PHRContextHolder.healthIdUserToken(linkedHidPlayLoad.getAuthToken());
		return ResponseEntity.ok(userService.linked(linkedPlayLoad));
	}

	@GetMapping("/qrCode")
	@Override
	public ResponseEntity<byte[]> getQrCode() {
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(phrCardHelper.getABHACard("STRING"));

	}

	@GetMapping("/qrCode/string/format")
	@Override
	public ResponseEntity<byte[]> getQrCodeInStringFormat() throws WriterException, IOException {
		return ResponseEntity.ok(phrCardHelper.getABHACard("STRING"));

	}

	@PostMapping("/link/profileDetails")
	@Override
	public ResponseEntity<PHRLinkResponse> linkProfileDetails(@Valid @RequestBody ValidateTokenRequest request) {
		return ResponseEntity.ok(healthIdService.fetchUserProfileToLinkPhrAddress(request.getAuthToken()));
	}

	@GetMapping("/{mediaType}/getCard")
	@Override
	public ResponseEntity<ByteArrayResource> generateCard(@PathVariable("mediaType") String mediaType) {
		return ResponseEntity.ok().headers(GeneralUtils.getHeaders(mediaType))
				.body(populateByteArray(phrCardHelper.getABHACard(mediaType)));
	}

	@GetMapping(value = "/qrCode/scan/fetchDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<User> fetchProfileDetailsByQrCodeScan(
			@NotBlank @RequestParam("abhaAddress") String abhaAddress, @NotBlank @RequestParam("code") String code) {
		return ResponseEntity.ok(userService.getQrCodeUserProfile(abhaAddress, code));
	}

	@Override
	@GetMapping("/logout")
	public ResponseEntity<Void> logout() {
		userService.logout();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(path="/switch/profile")
	public ResponseEntity<JwtResponse> switchProfileToken(@PathParam("phrAddress") String phrAddress) {
		return ResponseEntity.ok(userService.switchProfileToken(phrAddress));
	}

}
