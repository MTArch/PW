package com.parserlabs.phr.annotation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.YearOfBrith;
import com.parserlabs.phr.utils.PhrUtilits;

public class YearOfBrithValidator implements ConstraintValidator<YearOfBrith, String>  {
	
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		return StringUtils.isEmpty(value)|| PhrUtilits.isValidYearOfBirth(value);
	}

}
