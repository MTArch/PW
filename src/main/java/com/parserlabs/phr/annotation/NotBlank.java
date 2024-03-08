package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.DeveloperMessage.FIELD_REQUIRED;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.NotBlankValidator;

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = NotBlankValidator.class)
@Target({ TYPE, FIELD, PARAMETER })
public @interface NotBlank {

	Class<?>[] groups() default {};

	String message() default FIELD_REQUIRED;

	Class<? extends Payload>[] payload() default {};
}
