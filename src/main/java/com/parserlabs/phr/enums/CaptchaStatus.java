package com.parserlabs.phr.enums;

public enum CaptchaStatus {
	GENERATE("GENERATE"), ACTIVE("ACTIVE"), EXPIRED("EXPIRED"), COMPLETED("COMPLETED"), UPDATE("UPDATE");

	public static boolean isValid(String status) {
		CaptchaStatus[] values = CaptchaStatus.values();
		for (CaptchaStatus captchaStatus : values) {
			if (captchaStatus.toString().equals(status)) {
				return true;
			}
		}
		return false;
	}

	private String name;

	private CaptchaStatus(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
