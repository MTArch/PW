package com.parserlabs.phr.annotation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.MonthOfBirth;
import com.parserlabs.phr.utils.PhrUtilits;

public class MonthOfBirthValidator implements ConstraintValidator<MonthOfBirth, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return StringUtils.isBlank(value) ? true
				: !StringUtils.isBlank(value) ? PhrUtilits.isValidMonthOfBirth(value) : false;
	}

}
