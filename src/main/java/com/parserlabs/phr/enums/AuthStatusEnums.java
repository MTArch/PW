package com.parserlabs.phr.enums;

public enum AuthStatusEnums {
	ACTIVE("ACTIVE"), 
	INTERMIDIATE("INTERMIDIATE"), 
	DONE("DONE"),
	PRE_OTP("PRE_OTP"),
	OTP_VERIFIED("OTP_VERIFIED"), 
	PWD_VERIFIED("PWD_VERIFIED"), 
	UPDATE_OTP_VERIFIED("UPDATE_OTP_VERIFIED"),
	REGISTER_ACCOUNT_DETAILS("REGISTER_ACCOUNT_DETAILS");
;
	
	

	private String name;

	private AuthStatusEnums(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}