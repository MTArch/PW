package com.parserlabs.phr.service;

import static com.parserlabs.phr.constants.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.config.security.JwtTokenUtil;
import com.parserlabs.phr.exception.ResourceNotAuthorizedException;
import com.parserlabs.phr.model.authentication.RefreshTokenRequest;
import com.parserlabs.phr.model.response.AccessTokenResponse;

@Service
@CustomSpanned
public class RefreshTokenService {

	private static final String TOKEN_TYPE = "typ";
	private static final String TOKEN_TYPE_REFRESH = "Refresh";

	@Value("${jwt.access.token.validity.sec: 7200}") // Defaults to 10 Hrs
	public long JwtAccessTokenValidityInSec;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public AccessTokenResponse generateAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		String clientId = (String) jwtTokenUtil.getValueFromToken(refreshTokenRequest.getRefreshToken(), CLIENT_ID);
		String tokenType = (String) jwtTokenUtil.getValueFromToken(refreshTokenRequest.getRefreshToken(), TOKEN_TYPE);

		if (!jwtTokenUtil.isTokenExpired(refreshTokenRequest.getRefreshToken())
				&& TOKEN_TYPE_REFRESH.equalsIgnoreCase(tokenType)
				&& PHRContextHolder.clientId().equalsIgnoreCase(clientId)) {

			String phrAddress = jwtTokenUtil.getSubjectFromToken(refreshTokenRequest.getRefreshToken());
			String accessToken = jwtTokenUtil.generatePhrAddressToken(phrAddress);
			return AccessTokenResponse.builder().accessToken(accessToken).expiresIn(JwtAccessTokenValidityInSec)
					.build();
		}
		throw new ResourceNotAuthorizedException("You're not authorized to perform this operation");
	}
}
