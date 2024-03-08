package com.parserlabs.phr.annotation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;

import com.parserlabs.phr.annotation.ImageSizeFaceDetectors;
import com.parserlabs.phr.model.face.FaceResponse;
import com.parserlabs.phr.utils.OpenImajUtility;

public class ImageSizeFaceDetectorValidator implements ConstraintValidator<ImageSizeFaceDetectors, Object> {

	@Value("${haar.cascade.size:200}")
	private String haarCascadeSize;

	private boolean required;
	private boolean requiredFaceDetect;

	private int[] haarCascadeSizeValue = { 100, 130 };

	@Override
	public void initialize(ImageSizeFaceDetectors conf) {
		required = conf.required();
		requiredFaceDetect = conf.requiredFaceDetect();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Boolean ischeck = value != null && value.toString().trim().length() > 0;

		boolean isFaceFound = false;
		if (Boolean.TRUE.equals(ischeck)) {
			if (requiredFaceDetect) {
				FaceResponse fResp = OpenImajUtility.isFaceFoundProfile(value, haarCascadeSizeValue[0]);
				if (!fResp.isFaceFound()) {
					fResp = OpenImajUtility.isFaceFoundProfile(value, haarCascadeSizeValue[1]);
				}
				isFaceFound = fResp.isFaceFound();
			} else {
				isFaceFound = true;
			}

			return isFaceFound;

		} else {
			return !required && !ischeck;
		}
	}

}