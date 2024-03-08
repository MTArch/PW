package com.parserlabs.phr.enums;

public enum CaptchaType {

	MATH("MATH"), TEXT("TEXT"), SOUND("SOUND");

	public static boolean isValid(String type) {
		CaptchaType[] captchaType = CaptchaType.values();
		for (CaptchaType bio : captchaType) {
			if (bio.toString().equals(type)) {
				return true;
			}
		}
		return false;
	}

	String type;

	CaptchaType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
