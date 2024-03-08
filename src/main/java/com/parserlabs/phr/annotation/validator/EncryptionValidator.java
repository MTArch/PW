package com.parserlabs.phr.annotation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.Encryption;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;

public class EncryptionValidator implements ConstraintValidator<Encryption, String> {
	private Boolean isRequired;

	@Override
	public void initialize(Encryption value) {
		this.isRequired = value.required();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		boolean isChecked = StringUtils.isNotBlank(value);

		if (!isRequired && !isChecked) {
			return true;
		}
		return !StringUtils.isEmpty(DecryptRSAUtil.decrypt(value)) ? true : false;

	}

}
