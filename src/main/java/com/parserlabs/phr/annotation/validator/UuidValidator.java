package com.parserlabs.phr.annotation.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.Uuid;

public class UuidValidator implements ConstraintValidator<Uuid, String> {

	String parttern = "[0-9abcdef-]{36}";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return StringUtils.isEmpty(value) || Pattern.compile(parttern).matcher(value).matches();
	}

}
