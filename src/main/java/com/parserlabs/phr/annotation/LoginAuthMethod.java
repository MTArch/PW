package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.DeveloperMessage.DATA_INVALID;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.LoginAuthMethodValidator;

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = LoginAuthMethodValidator.class)
@Target({ TYPE, FIELD, PARAMETER })
public @interface LoginAuthMethod {
	String message() default DATA_INVALID;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean required() default false;
	
}
