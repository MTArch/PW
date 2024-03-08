/**
 * 
 */
package com.parserlabs.phr.annotation;

import static  com.parserlabs.phr.constants.Constants.DeveloperMessage.DATA_INVALID;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.StringEnumerationValidator;
/**
 * @author Rajesh
 *
 */
@Documented
@Constraint(validatedBy = StringEnumerationValidator.class)
@Target({ ElementType.METHOD, FIELD, ElementType.ANNOTATION_TYPE, PARAMETER, ElementType.CONSTRUCTOR })
@Retention(RUNTIME)
public @interface StringEnumeration {
	 String message() default DATA_INVALID;
	  Class<?>[] groups() default {};
	  Class<? extends Payload>[] payload() default {};

	  Class<? extends Enum<?>> enumClass();

}
