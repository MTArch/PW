package com.parserlabs.phr.annotation.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.parserlabs.phr.annotation.NotBlank;

/**
 * 
 * @author Rajesh
 *
 */
public class NotBlankValidator implements ConstraintValidator<NotBlank, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return (value instanceof String) ? Objects.nonNull(value) && value.toString().trim().length() > 0
				: (value instanceof byte[]) ? Objects.nonNull(value) : Objects.nonNull(value) ? true : false;
	}
}
