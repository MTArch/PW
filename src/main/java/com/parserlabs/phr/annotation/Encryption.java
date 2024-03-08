package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.DeveloperMessage.INVALID_ENCRYPTION_MISSMATCH;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.EncryptionValidator;

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = EncryptionValidator.class)
@Target({ TYPE, FIELD, PARAMETER })
public @interface Encryption {

	String message() default INVALID_ENCRYPTION_MISSMATCH;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean required() default true;

}
