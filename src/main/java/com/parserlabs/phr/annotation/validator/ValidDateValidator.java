package com.parserlabs.phr.annotation.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.ValidDate;

public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {

	private static final String DATE_FORMATTER = "dd-MM-yyyy";
	private Boolean isOptional;

	@Override
	public void initialize(ValidDate validDate) {
		this.isOptional = validDate.optional();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		
		return (!isOptional || (!StringUtils.isEmpty(value))) ? isValidFormat(DATE_FORMATTER, value): true;
	}

	private static boolean isValidFormat(String format, String value) {

		Date date = null;
		if (!StringUtils.isEmpty(value) && value.matches("[0-9]+") && value.length() == 4) {
			return true;
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			if (value != null) {
				date = sdf.parse(value);
				if (!value.equals(sdf.format(date))) {
					date = null;
				}
			}

		} catch (ParseException ex) {
		}
		return date != null;
	}
}
