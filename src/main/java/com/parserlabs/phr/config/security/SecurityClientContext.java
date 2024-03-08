package com.parserlabs.phr.config.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.model.UserDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class SecurityClientContext {

	private String clientId;
	private UserDTO user;
	private String keycloakAccessToken;
	private String keycloakRefreshToken;
	private String xhipId;
	private String facilityId;
	private boolean isCaptch;
	private String clientIp;
	private String requesterType;
	private String purpose;
	private String requesterId;

	public static SecurityClientContext of(String clientId, String xhipId) {
		return SecurityClientContext.builder().clientId(clientId).xhipId(xhipId).build();
	}

	public static SecurityClientContext of(String clientId, String keycloakAccessToken, String keycloakRefreshToken,
			String xhipId, Boolean isCaptcha) {
		return SecurityClientContext.builder().clientId(clientId).keycloakAccessToken(keycloakAccessToken)
				.keycloakRefreshToken(keycloakRefreshToken).xhipId(xhipId).isCaptch(isCaptcha).build();
	}
}
