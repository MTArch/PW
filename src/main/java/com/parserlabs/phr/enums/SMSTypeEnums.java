package com.parserlabs.phr.enums;

public enum SMSTypeEnums {
	// NOTE ADD MORE AS REQUIRED
	LOGIN_OTP("LOGIN_OTP"), UPDATE_OTP("UPDATE_OTP"), REGISTER_SUCCESS_EMAIL_MOBILE("REGISTER_SUCCESS_EMAIL_MOBILE"), REGISTER_SUCCESS_ABHA_NUMBER("REGISTER_SUCCESS_ABHA_NUMBER");

	private String name;

	private SMSTypeEnums(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
