package com.parserlabs.phr.enums;

/**
 * Salt key for the REDIS , To distinguish the lockout.
 * 
 * @author Rajesh
 *
 */
public enum RedisRequestSalt {
	GENERATE_MOBILE_OTP,
	VERIFY_MOBILE_OTP, 
	RESEND_MOBILE_OTP, 
	
	REG_EMAIL_OTP,
	REG_VERIFY_EMAIL_OTP, 
	REG_RESEND_EMAIL_OTP, 
	
	REG_VERIFY_EMAIL_MOBILE_OTP, 
	
	REG_DETAILS_PRE,

	RESEND_MOBILE_EMAIL_OTP,
	LOGIN_EMAIL_OTP, 
	LOGIN_EMAIL_PHR_OTP, 
	LOGIN_MOBILE_OTP, 
	LOGIN_MOBILE_PHR, 
	
	LOGIN_VERIFY_MOBILE_PHR_OTP,
	LOGIN_VERIFY_MOBILE_OTP,
	
	LOGIN_VERIFY_EMAIL_PHR_OTP,
	LOGIN_VERIFY_EMAIL_OTP,
	
	LOGIN_VERIFY_MOBILE_EMAIL_OTP,

	
	LOGIN_PWD, 
	LOGIN_PWD_VERIFY, 
	
	LOGIN_MOBILE_OTP_RESEND,
	LOGIN_MOBILE_PHR_OTP_RESEND,
	
	LOGIN_EMAIL_OTP_RESEND, 
	LOGIN_EMAIL_PHR__OTP_RESEND, 
	
	OTP_REG_30SEC_CHECK, OTP_REG_RESEND_30SEC_CHECK,
	
	PROFILE_EDIT_CHECK,
	PROFILE_EDIT_PWD_CHECK

}