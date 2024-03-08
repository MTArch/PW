package com.parserlabs.phr.enums;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public enum AuthMethods {
	AADHAAR_OTP("AADHAAR_OTP"), MOBILE_OTP("MOBILE_OTP"), EMAIL_OTP("EMAIL_OTP"), PASSWORD("PASSWORD"),
	DEMOGRAPHICS("DEMOGRAPHICS"), AADHAAR_BIO("AADHAAR_BIO");

	public static List<String> getAllSupportedAuth() {
		List<String> names = new ArrayList<>();
		for (AuthMethods auth : values()) {
			names.add(auth.name);
		}
		return names;
	}

	public static boolean isValid(String authType) {
		AuthMethods[] values = AuthMethods.values();
		for (AuthMethods auth : values) {
			if (auth.toString().equals(authType)) {
				return true;
			}
		}
		return false;
	}

	@ApiModelProperty(example = "AADHAAR_OTP", dataType = "String", name = "name", value = "Based on authMethods", allowableValues = "AADHAAR_OTP,MOBILE_OTP,PASSWORD,DEMOGRAPHICS,AADHAAR_BIO")
	private String name;

	private AuthMethods(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}