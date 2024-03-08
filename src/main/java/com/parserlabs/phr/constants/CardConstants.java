/**
 * 
 */
package com.parserlabs.phr.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Rajesh
 *
 */
@Component
public class CardConstants {
	public static String qrRedirectLink;

	@Value("${qr.redirect.link}")
	public void setQrRedirectLink(String qrRedirectLink) {
		CardConstants.qrRedirectLink = qrRedirectLink;
	}

}
