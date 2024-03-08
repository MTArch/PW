package com.parserlabs.phr.annotation.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.Otp;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;

public class OtpValidator implements ConstraintValidator<Otp, String> {

	String parttern = "[0-9]{6}";
	private boolean isRequired = true;

	@Override
	public void initialize(Otp otp) {
		this.isRequired = otp.required();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (!isRequired && StringUtils.isEmpty(value)) {
			return true;
		}
		String decryptedValue = DecryptRSAUtil.decrypt(value);
		value = !StringUtils.isEmpty(decryptedValue) ? decryptedValue : value;
		return Pattern.compile(parttern).matcher(value).matches();
	}
}
