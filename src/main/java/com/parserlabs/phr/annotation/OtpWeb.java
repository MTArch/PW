package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.DeveloperMessage.OTP_MISSMATCH;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.OtpValidatorWeb;


@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = OtpValidatorWeb.class)
@Target({ TYPE, FIELD, PARAMETER })
public @interface OtpWeb {

	String message() default OTP_MISSMATCH;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
