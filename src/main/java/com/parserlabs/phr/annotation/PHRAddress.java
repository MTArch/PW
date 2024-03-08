package com.parserlabs.phr.annotation;


import static com.parserlabs.phr.constants.Constants.DeveloperMessage.HEALTH_ID;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.PHRAddressValidator;


@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = PHRAddressValidator.class)
@Target({ TYPE, FIELD, PARAMETER })
public @interface PHRAddress {
	

	String message() default HEALTH_ID;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};


}
