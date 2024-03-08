package com.parserlabs.phr.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.model.KeycloakTokenRequest;
import com.parserlabs.phr.model.KeycloakTokenResponse;
import com.parserlabs.phr.proxy.GatewayProxy;

@Component
public class GatewayTokenConfig {

	@Autowired
	private GatewayProxy gatewayProxy;

	@Value("${api.client.id}")
	private String clientId;

	@Value("${api.client.secret}")
	private String clientSecret;

	public String fetchGatewayToken() {
		KeycloakTokenRequest keycloakTokenRequest = KeycloakTokenRequest.builder().clientId(clientId)
				.clientSecret(clientSecret).build();
		KeycloakTokenResponse fetchGatewayToken = gatewayProxy.fetchGatewayToken(keycloakTokenRequest);
		return fetchGatewayToken.getAccessToken();
	}

}
