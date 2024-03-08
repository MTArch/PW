package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.DeveloperMessage.PASSWORD_MISSMATCH;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.PasswordCustomValidator;

@Documented
@Constraint(validatedBy = PasswordCustomValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
 
    String message() default PASSWORD_MISSMATCH;
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
    
    boolean required() default true;
        
}
