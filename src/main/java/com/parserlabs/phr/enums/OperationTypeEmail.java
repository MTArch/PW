/**
 * 
 */
package com.parserlabs.phr.enums;

/**
 * @author Rajesh
 *
 */
public enum OperationTypeEmail {
	REGISTRATION("REGISTRATION"), 
	REGISTRATION_OTP("REGISTRATION_OTP"),
	REGISTRATION_SUCCESS("REGISTRATION_SUCCESS"),
	
	LOGIN("LOGIN"), 
	LOGIN_OTP("LOGIN_OTP"),
	LOGIN_SUCCESS("LOGIN_SUCCESS"),
	
	UPDATE_OTP("UPDATE_OTP"),
	UPDATE_SUCCESS("UPDATE_SUCCESS"),
	
	LINK_HID("LINK_HID"), 
	LINK_HID_OTP("LINK_HID_OTP"),
	LINK_HID_SUCCESS("LINK_HID_SUCCESS"),
	
	DELINK_HID("DELINK_HID"), 
	DELINK_HID_OTP("DELINK_HID_OTP"),
	DELINK_HID_SUCCESS("DELINK_HID_SUCCESS"),
	
	SUCCESS("SUCCESS"), 
	FAILED("FAILED"),
	EXCEPTION("EXCEPTION"),
	ACTIVATION("ACTIVATION"),
	EXPIRE("EXPIRE"), 
	
	UPDATE("UPDATE"), 
	GENERATE("GENERATE"), 
	REGENERATE("REGENERATE");
	
	private String name;

	private OperationTypeEmail(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
