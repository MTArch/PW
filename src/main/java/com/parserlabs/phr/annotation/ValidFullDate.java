package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.DeveloperMessage.DATE_MISSMATCH;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.ValidFullDateValidator;


@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = ValidFullDateValidator.class)
public @interface ValidFullDate {

	String message() default DATE_MISSMATCH;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean optional() default true;

}

