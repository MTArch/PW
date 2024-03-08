package com.parserlabs.phr.annotation.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.parserlabs.phr.annotation.OtpWeb;

public class OtpValidatorWeb implements ConstraintValidator<OtpWeb, String> {

	String parttern = "[0-9]{6}";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return Pattern.compile(parttern).matcher(value).matches();
	}
}
