/**
 * 
 */
package com.parserlabs.phr.model.captcha;

import java.io.Serializable;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

/**
 * @author Rajesh
 *
 */
@Data
@Builder
public class CaptchaAuthRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8200912230718653689L;

	private String clientId;
	private String clientSecert;
	private UUID captchaId;
	private String userAction;

}
