package com.parserlabs.phr.captcha;

import org.springframework.stereotype.Component;

import com.parserlabs.phr.model.KeycloakTokenRequest;


@Component
public class HealthIdTokenGenerate  {
	
	public KeycloakTokenRequest getRequest(String clientId, String clientSecret ) {		
		return KeycloakTokenRequest.builder()
				.clientId(clientId)
				.clientSecret(clientSecret)
				.build();
	}
}
