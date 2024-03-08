package com.parserlabs.phr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakTokenRequest {

	private String clientId;
	private String clientSecret;

	public static  KeycloakTokenRequest of(String clientId, String clientSecret) {
		return KeycloakTokenRequest.builder().clientId(clientId).clientSecret(clientSecret).build();
	}
}
