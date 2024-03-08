package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.DeveloperMessage.UUID_MISSMATCH;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.UuidValidator;

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = UuidValidator.class)
@Target({ TYPE, FIELD, PARAMETER })
public @interface Uuid{
	String message() default UUID_MISSMATCH;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
