/**
 * 
 */
package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.DeveloperMessage.INVALID_EMAIL_FORMAT;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.EmailValidator;

/**
 * @author Rajesh
 *
 */

@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, PARAMETER })
@Constraint(validatedBy = EmailValidator.class)
public @interface Email {

	String message() default INVALID_EMAIL_FORMAT;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean optional() default false;

}
