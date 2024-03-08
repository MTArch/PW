package com.parserlabs.phr.enums;

import java.util.Arrays;
import java.util.Optional;

public enum LoginMethodsEnum {
	AADHAAR_OTP(1, "AADHAAR_OTP"), MOBILE_OTP(2, "MOBILE_OTP"), PASSWORD(3, "PASSWORD"), EMAIL_OTP(4, "EMAIL_OTP"),
	DEMOGRAPHICS(5, "DEMOGRAPHICS"),
	AADHAAR_BIO(6, "AADHAAR_BIO");

	private int code;
	private String method;

	LoginMethodsEnum(int code, String description) {
		this.code = code;
		this.method = description;
	}

	public int code() {
		return code;
	}

	public String method() {
		return method;
	}

	public static String status(int code) {
		Optional<LoginMethodsEnum> statusOptional = Arrays.asList(LoginMethodsEnum.values()).stream()
				.filter(se -> se.code() == code).findFirst();
		return statusOptional.isPresent() ? statusOptional.get().name() : "";
	}

	public static int docType(String description) {
		Optional<LoginMethodsEnum> statusOptional = Arrays.asList(LoginMethodsEnum.values()).stream()
				.filter(se -> se.name() == description).findFirst();
		return statusOptional.isPresent() ? statusOptional.get().code() : 0;
	}

	public static boolean isValidByLoginCode(String authMethod) {
		Optional<LoginMethodsEnum> statusOptional = Arrays.asList(LoginMethodsEnum.values()).stream()
				.filter(se -> se.name().equalsIgnoreCase(authMethod)).findFirst();
		return statusOptional.isPresent() ? true : false;
	}

}
