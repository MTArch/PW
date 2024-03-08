package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.DeveloperMessage.GENDER_INVALID;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.GenderValidator;

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = GenderValidator.class)
@Target({ TYPE, FIELD, PARAMETER })
public @interface Gender {
	String message() default GENDER_INVALID;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean required() default false;
	
}
