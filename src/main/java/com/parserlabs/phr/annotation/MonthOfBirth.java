package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.DeveloperMessage.PATTREN_MISMATCHED;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.MonthOfBirthValidator;

@Retention(RUNTIME)
@Constraint(validatedBy = MonthOfBirthValidator.class)
@Target({ TYPE, FIELD, PARAMETER })
public @interface MonthOfBirth {
	
	String message() default PATTREN_MISMATCHED;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
