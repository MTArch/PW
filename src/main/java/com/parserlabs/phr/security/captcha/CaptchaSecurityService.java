/**
 * 
 */
package com.parserlabs.phr.security.captcha;

import com.parserlabs.phr.model.captcha.CaptchaAuthRequest;
import com.parserlabs.phr.model.captcha.CaptchaBuilderResponse;

/**
 * @author Rajesh
 *
 */
public interface CaptchaSecurityService {

	public CaptchaBuilderResponse generateCaptcha(String request);

	public Boolean validateSessionCaptcha(CaptchaAuthRequest payload, int sessionTimeOut);
	
	public Boolean validateSessionCaptcha(String captchaData, int sessionTimeOut);

}
