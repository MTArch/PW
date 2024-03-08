package com.parserlabs.phr.annotation.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import com.parserlabs.phr.annotation.HealthId;

public class HealthIdValidator implements ConstraintValidator<HealthId, String> {

	String parttern = "([A-Za-z][A-Za-z0-9]{3,31})|([A-Za-z][A-Za-z0-9]*\\\\.[A-Za-z0-9]+)|([1-9][0-9]{13})";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.hasLength(value) && value.contains("@")) {
			value = value.split("@")[0];
		}
		return StringUtils.isEmpty(value) || Pattern.compile(parttern).matcher(value).matches();
	}
}
