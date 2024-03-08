package com.parserlabs.phr.enums;

public enum AbhaCardTypeEnums {
	PDF("PDF"), PNG("PNG"), JPEG("JPEG"), SVG("SVG"), STRING("STRING");

	private String name;

	private AbhaCardTypeEnums(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
