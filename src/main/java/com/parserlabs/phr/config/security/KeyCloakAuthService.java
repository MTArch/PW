package com.parserlabs.phr.config.security;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64.Decoder;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.RSATokenVerifier;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.parserlabs.phr.commons.PHRContextHolder;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Slf4j
@Component
public class KeyCloakAuthService {

	private static final Object HEALTH_ID_ACCESS_ROLE = "healthId";
	@Value("${apis.keycloak.certUrl}")
	private String certUrl;

	@Value("${apis.keycloak.keys.n}")
	private String keyN;

	@Value("${apis.keycloak.keys.e}")
	private String keyE;
	
	@Value("#{'${apis.keycloak.issuer}'.split(',')}")
	private List<String> issuer;
	
	private PublicKey publicKey = null;

	public String fetchClientId(String token, HttpServletRequest request) {
		// token = "Bearer " + token;
		String clientId = null;
		try {
			token = token.substring(7).trim();
			PublicKey pk = retrievePublicKeyFromCertsEndpoint();
			AccessToken accessToken = RSATokenVerifier.create(token).checkActive(true).publicKey(pk).getToken();
			
			//IMP: Check token issuer is same as expected.
			if (issuer.contains(accessToken.getIssuer().toLowerCase())) {
				if (accessToken.isActive() && hasRole(accessToken)) {
					log.info("This token is issued for {}", accessToken.getIssuedFor());
					clientId = accessToken.getIssuedFor();
					PHRContextHolder.authrizationToken(token);
				} else {
					log.info("This token is issued for {}, however is Expired or do not have HealthId Role.",
							accessToken.getIssuedFor());
				}
			}
		} catch (org.keycloak.common.VerificationException exp) {
			log.warn("Exception while verifying Keycloak Token: " + exp.toString());
		}
		return clientId;
	}

	private boolean hasRole(AccessToken accessToken) {
		Set<String> roles = Objects.nonNull(accessToken.getRealmAccess()) ? accessToken.getRealmAccess().getRoles()
				: null;
		PHRContextHolder.clientRole(roles);
		return !CollectionUtils.isEmpty(roles) && roles.contains(HEALTH_ID_ACCESS_ROLE);
	}

	private PublicKey retrievePublicKeyFromCertsEndpoint() {
		if (Objects.isNull(this.publicKey)) {
			try {

				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				String modulusBase64 = keyN;
				String exponentBase64 = keyE;

				Decoder urlDecoder = java.util.Base64.getUrlDecoder();
				BigInteger modulus = new BigInteger(1, urlDecoder.decode(modulusBase64));
				BigInteger publicExponent = new BigInteger(1, urlDecoder.decode(exponentBase64));

				publicKey = keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));

			} catch (Exception exp) {
				log.warn("Exception while calling certs " + exp.toString());
			} 
		}
		return publicKey;
	}
}
