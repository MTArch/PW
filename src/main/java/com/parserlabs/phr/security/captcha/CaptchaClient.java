/**
 * 
 */
package com.parserlabs.phr.security.captcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.parserlabs.phr.exception.AuthenticationException;
import com.parserlabs.phr.exception.model.ErrorCode;


/**
 * @author Rajesh
 *
 */
@Service
public class CaptchaClient {

	@Value("${captcha.web.clientID}")
	private String clientID;

	@Value("${captcha.web.clientSecret}")
	private String clientSecert;

	public Boolean authenticate(String userId, String userSecert) {
		return authenticateClient(userId, userSecert);
	}

	private Boolean authenticateClient(String userId, String userSecert) {
		if (clientID.equals(userId) && clientSecert.equals(userSecert)) {
			return true;
		}
		throw new AuthenticationException(ErrorCode.UNAUTHORIZED);
	}

}
