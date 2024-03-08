package com.parserlabs.phr.annotation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.Gender;

/**
 * @author Rajesh kr
 * @apiNote Gender validation.
 * 
 */
public class GenderValidator implements ConstraintValidator<Gender, String> {

	private Boolean isRequired;

	@Override
	public void initialize(Gender gender) {
		this.isRequired = gender.required();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		boolean checkGender = true;

		if (isRequired) {
			checkGender = !StringUtils.isEmpty(value);
		}

		if (checkGender) {
			return com.parserlabs.phr.enums.Gender.isValidCode(value);
		}
		return false;
	}

}
