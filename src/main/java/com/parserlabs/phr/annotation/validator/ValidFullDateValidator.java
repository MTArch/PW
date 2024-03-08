package com.parserlabs.phr.annotation.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.ValidFullDate;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ValidFullDateValidator implements ConstraintValidator<ValidFullDate, String> {
	
	private static final String DATE_FORMATTER = "dd-MM-yyyy";
	private Boolean isOptional;

	@Override
	public void initialize(ValidFullDate validFullDate) {
		this.isOptional = validFullDate.optional();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		
		return (!isOptional || (!StringUtils.isEmpty(value))) ? isValidFormat(DATE_FORMATTER, value): true;
	}

	private static boolean isValidFormat(String format, String value) {

		Date date = null;
		Date todaysDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			if (value != null) {
				date = sdf.parse(value);
				if (!value.equals(sdf.format(date))) {
					date = null;
				}
			}

		} catch (ParseException ex) {
			 log.error("Wrong date format ententered for reactivation", ex);
		}
		return date != null  &&  date.compareTo(todaysDate)>0 && value.length()==10;
	}

}






