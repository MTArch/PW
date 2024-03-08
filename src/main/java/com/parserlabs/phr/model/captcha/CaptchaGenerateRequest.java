/**
 * 
 */
package com.parserlabs.phr.model.captcha;

import lombok.Builder;
import lombok.Data;

/**
 * @author Rajesh
 *
 */
@Data
@Builder
public class CaptchaGenerateRequest {

	private String clientId;
	private String clientSecert;
	private String type;
	private String authTs;
	private String hostIp;
	private String latitude;
	private String longitude;
}
