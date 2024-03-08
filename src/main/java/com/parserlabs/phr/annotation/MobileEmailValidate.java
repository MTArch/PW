/**
 * 
 */
package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.DeveloperMessage.MOBILE_NUMBER_EMAIL_MISSMATCH;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.MobileEmailValidator;

/**
 * @author Rajesh
 *
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, PARAMETER })
@Constraint(validatedBy = MobileEmailValidator.class)
public @interface MobileEmailValidate {

	String message() default MOBILE_NUMBER_EMAIL_MISSMATCH;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean required() default true;

}
