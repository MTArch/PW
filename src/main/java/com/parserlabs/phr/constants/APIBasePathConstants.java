/**
 * 
 */
package com.parserlabs.phr.constants;

/**
 * @author Rajesh
 *
 */
public interface APIBasePathConstants {

	interface EndPoints {
		String AUTH_V1_ENDPOINT = "/v1/phr/auth";
		String MOBILE_OR_EMAIL_REGISTRATION_V1_ENDPOINT = "/v1/phr/registration";
		String AADHAAR_REGISTRATION_V1_ENDPOINT = "/v1/phr/registration/aadhaar";
		String LOCATION_V1_ENDPOINT = "/v1/phr/location";
		String SEARCH_V1_ENDPOINT = "/v1/phr/search";
		String PUBLIC_CERT_V1_ENDPOINT = "/v1/phr/public";
		String HID_REGISTRATION_V1_ENDPOINT = "/v1/phr/registration/hid";
		String MOBILE_OR_EMAIL_LOGIN_V1_ENDPOINT = "/v1/phr/login";
		String LOGIN_V1_ENDPOINT = "/v1/phr/login";
		String PROFILE_V1_ENDPOINT = "/v1/phr/profile";
		String SECURITY_CAPTCHA_V1 = "/v1/phr/security/captcha/";
		String FORGET_V1_ENDPOINT = "/v1/phr/forget";

	}
}
