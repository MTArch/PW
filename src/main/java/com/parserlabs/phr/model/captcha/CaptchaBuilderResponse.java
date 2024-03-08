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
public class CaptchaBuilderResponse implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -3227006569688334446L;
	
	private String captcha;
	private UUID captchaId;

}
