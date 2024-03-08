package com.parserlabs.phr.annotation;

import static com.parserlabs.phr.constants.Constants.FACE_NOT_FOUND;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.parserlabs.phr.annotation.validator.ImageSizeFaceDetectorValidator;


@Documented
@Constraint(validatedBy = ImageSizeFaceDetectorValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageSizeFaceDetectors {
 
    String message() default FACE_NOT_FOUND;
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
    
    boolean required() default false;
    boolean requiredFaceDetect() default false;
    

    
}
