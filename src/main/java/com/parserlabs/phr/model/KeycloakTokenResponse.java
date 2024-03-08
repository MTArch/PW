package com.parserlabs.phr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakTokenResponse {

	private String expiresIn;
	private String accessToken;
	private String refreshExpiresIn;
	private String refreshToken;
	private String tokenType;

}
