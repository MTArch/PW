package com.parserlabs.phr.captcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeyCloakTokenService {

	@Value("${api.client.url}")
	private String accessTokenUrl;
	
	@Value("${api.client.id}")
	private String clientId;
	
	@Value("${api.client.secret}")
	private String clientSecret;
	
//	@Autowired
//	private healthIdProxy<KeycloakTokenRequest, KeycloakTokenResponse> accessTokenProxy;
//
//	public KeycloakTokenResponse fetchKeycloakAccessToken() {
//		return accessTokenProxy.post(accessTokenUrl, KeycloakTokenRequest.of(clientId, clientSecret), KeycloakTokenResponse.class);
//	}

}
