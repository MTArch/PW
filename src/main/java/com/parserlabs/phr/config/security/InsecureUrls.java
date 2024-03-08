

package com.parserlabs.phr.config.security;

import java.util.Arrays;
import java.util.List;

public interface InsecureUrls {
	public static final List<String> ONE_TIME_CAPTCHA_USE_ONLY = Arrays.asList(
			"/v1/phr/search/authMethods",
			"/v1/phr/search/isExist",
			"/v1/phr/search/exists/byhealthId/phrAddress/",
			"/v1/phr/registration/hid/searchPhr"
	);
	
	// List of URLs which are eligible for Brute-force check.
	// These are supposed to be URLs which can only be called with Captch.
	public static final List<String> CAPTCHA_BRUTE_FORCE_CHECK_URLS = Arrays.asList(
			"/v1/phr/registration/hid/confirm/credential",
			"/v1/phr/registration/hid/init/transaction",
			"/v1/phr/registration/hid/init/resendOtp",
			"/v1/phr/registration/hid/create-phr-address",
			"/v1/phr/registration/hid/confirm/credential",
			"/v1/phr/profile/update/phrAttribute",
			"/v1/phr/registration/hid/search/auth-methods",
			"/v1/phr/login/search/userByPhrAddress",
			"/v1/phr/login/search-healthIdNumber",
			"/v1/phr/login/init/transaction",
            "/v1/phr/registration/update/phr/photo/migration",
			"/v1/phr/registration/create/phr/migration",
			"/v1/phr/registration/verify/otp",
			"/v1/phr/registration/resend/otp",
			"/v1/phr/registration/generate/otp",
			"/v1/phr/registration/details",
			"/v1/phr/registration/create/phr",
			"/v1/phr/registration/create/phr/HidMobile",
			"/v1/phr/registration/phr/suggestion",
			
			"/v1/phr/search/authMethods",
			"/v1/phr/search/isExist",
			"/v1/phr/search/exists/byhealthId/phrAddress/",
           			
			"/v1/phr/login/mobileEmail/getUserToken",
			"/v1/phr/login/mobileEmail/init",
			"/v1/phr/login/mobileEmail/preVerification",
			"/v1/phr/login/phrAddress/init",
			"/v1/phr/login/phrAddress/verify",
			"/v1/phr/login/resend/otp",
			"/v1/phr/login/search/authMethods",
			
			"/v1/phr/public/certificate"
		
			);
	
	public static final List<String> INSECURE_URLS = Arrays.asList(
			"/v1/phr/registration/hid/confirm/credential",
			"/v1/phr/registration/hid/init/transaction",
			"/v1/phr/registration/hid/init/resendOtp",
			"/v1/phr/registration/hid/create-phr-address",
			"/v1/phr/registration/hid/confirm/credential",
			"/v1/phr/registration/hid/search/auth-methods",
				
			"/v1/phr/registration/hid/searchPhr",
			"/v1/phr/registration/verify/otp",
			"/v1/phr/registration/resend/otp",
			"/v1/phr/registration/generate/otp",
			"/v1/phr/registration/details",
			"/v1/phr/registration/create/phr",
			"/v1/phr/registration/create/phr/HidMobile",
			"/v1/phr/registration/update/phr/photo/migration",
			"/v1/phr/registration/create/phr/migration",
			"/v1/phr/registration/phr/suggestion",

			

			"/v1/phr/search/authMethods",
			"/v1/phr/search/isExist",
			"/v1/phr/search/exists/byhealthId/phrAddress/",
			"/v1/phr/search/search/userByPhrAddress",
			
			"/v1/phr/auth/verify/token",
			"/v1/phr/auth/generate/access-token",
			
			"/v1/phr/login/search/userByPhrAddress",
			"/v1/phr/login/search-healthIdNumber",
			"/v1/phr/login/init/transaction",
			"/v1/phr/login/search/authMethods",
			"/v1/phr/login/mobileEmail/getUserToken",
			"/v1/phr/login/mobileEmail/init",
			"/v1/phr/login/mobileEmail/preVerification",
			"/v1/phr/login/phrAddress/init",
			"/v1/phr/login/phrAddress/verify",
			"/v1/phr/login/resend/otp",
			
			"/v1/phr/location/districts",
			"/v1/phr/location/states",
			
			"/v1/phr/profile/update/phrAttribute",
			"/v1/phr/profile/link/profileDetails",
			"/v1/phr/profile/update/passwordFromHid",
            "/v1/phr/profile/update/phrAttribute",
            "/v1/phr/profile/update/generateOtp",
			"/v1/phr/profile/link/phr/hidtoken",

			"/v1/phr/public/certificate",
			
			"/v1/phr/security/captcha/generate",
			"/v1/phr/search/search/userByPhrAddress",
			"/swagger-resources/configuration/ui", 
			"/swagger-resources", 
			"/swagger-resources/**", 
			"/swagger-ui.html",
			"/swagger-ui.html/**",
			"/swagger-ui/index.html",
			"/swagger-ui/index.html/**",
			"/swagger-ui/**",
			"/v3/**",
			"/webjars/**",
			"/index.html",
			"/*.html",
			"/",
			"/v1/phr/forget/mobileEmail/generateotp",
			"/v1/phr/forget/mobileEmail/verifyotp",
			"/v1/phr/forget/mobileEmail/update-password",
			"/actuator",
			"/actuator/health",
			"/actuator/info",
			"/actuator/prometheus"
			);
	
			public static final List<String> CAPTCHA_URLS = Arrays.asList(
					"/v1/phr/search/authMethods",
					"/v1/phr/search/isExist",
					"/v1/phr/search/exists/byhealthId/phrAddress/",
					"/v1/phr/registration/hid/searchPhr"					
			);
			


		  public static final List<String> GUEST_URLS = Arrays.asList(
				  "/v1/phr/public/certificate",
				  
				  "/v1/phr/search/isExist",
				  
				  "/v1/phr/registration/hid/create-phr-address",
				  "/v1/phr/registration/create/phr/HidMobile",
				  "/v1/phr/registration/phr/suggestion",
				  "/v1/phr/registration/create/phr/migration",
				  "/v1/phr/registration/update/phr/photo/migration",
				  
				  "/v1/phr/profile/update/phrAttribute",
				  "/v1/phr/profile/link/profileDetails",

				  "/v1/phr/search/search/userByPhrAddress",
				  "/v1/phr/security/captcha/generate"
			//	  "/actuator",
			//	  "/actuator/health"
			//	  "/actuator/info",
			//	  "/actuator/prometheus"


		      );
		  
		  public static final List<String> HEALTH_ID_URLS = Arrays.asList(
				    "/v1/phr/registration/hid/confirm/credential",
					"/v1/phr/registration/hid/init/transaction",
					"/v1/phr/registration/hid/init/resendOtp",
					"/v1/phr/registration/hid/create-phr-address",
					"/v1/phr/registration/hid/confirm/credential",
					"/v1/phr/registration/hid/search/auth-methods",
					"/v1/phr/registration/details",
					"/v1/phr/registration/create/phr/migration",
					"/v1/phr/registration/create/phr",
					"/v1/phr/registration/create/phr/HidMobile",
					
					"/v1/phr/login/mobileEmail/preVerification",
					"/v1/phr/login/search-healthIdNumber",
					"/v1/phr/login/init/transaction",
					
					"/v1/phr/location/districts",
					"/v1/phr/location/states",
					
					"/v1/phr/profile/edit",
					"/v1/phr/profile/link/hid",
					"/v1/phr/profile/link/phr/hidtoken",
					"/v1/phr/profile/link/profileDetails",
					"/v1/phr/profile/delete"
		      );
}
