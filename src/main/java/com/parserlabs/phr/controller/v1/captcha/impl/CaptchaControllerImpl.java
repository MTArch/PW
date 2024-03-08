package com.parserlabs.phr.controller.v1.captcha.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.annotation.NotBlank;
import com.parserlabs.phr.controller.v1.captcha.CaptchaController;
import com.parserlabs.phr.model.captcha.CaptchaBuilderResponse;
import com.parserlabs.phr.security.captcha.CaptchaSecurityService;

@RestController()
@CrossOrigin
@CustomSpanned
@RequestMapping(SECURITY_CAPTCHA_V1)
public class CaptchaControllerImpl implements CaptchaController {

	@Autowired
	private CaptchaSecurityService captchaSecurityService;

	@GetMapping("/generate")
	@Override
	public ResponseEntity<CaptchaBuilderResponse> generateCaptcha(@NotBlank @RequestParam("request")String request) {
		return ResponseEntity.ok(captchaSecurityService.generateCaptcha(request));
	}
	
	
	

	

}