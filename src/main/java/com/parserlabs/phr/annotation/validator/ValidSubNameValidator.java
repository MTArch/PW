package com.parserlabs.phr.annotation.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.ValidSubName;

public class ValidSubNameValidator implements ConstraintValidator<ValidSubName, String> {

	String namePattern = "^[A-Za-z\\s\\.\\']{1,}";
	

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return StringUtils.isEmpty(value) || Pattern.compile(namePattern).matcher(value).matches();
	}

}
