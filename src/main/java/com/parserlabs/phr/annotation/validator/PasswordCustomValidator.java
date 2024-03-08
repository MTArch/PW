package com.parserlabs.phr.annotation.validator;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.passay.AlphabeticalSequenceRule;
import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.QwertySequenceRule;
import org.passay.RuleResult;
import org.passay.SpecialCharacterRule;
import org.passay.UppercaseCharacterRule;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Value;

import com.parserlabs.phr.annotation.Password;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;

public class PasswordCustomValidator implements ConstraintValidator<Password, String> {

	private boolean isRequired = true;

	@Value("${password.maxLength:30}")
	private int maxLength;

	@Value("${password.minLength:8}")
	private int minLength;

	@Value("${password.minUpperCaseLength:1}")
	private int minUpperCaseLength;

	@Override
	public void initialize(Password conf) {
		isRequired = conf.required();
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(minLength, maxLength),
				new UppercaseCharacterRule(minUpperCaseLength), new DigitCharacterRule(1), new SpecialCharacterRule(1),
				// new NumericalSequenceRule(3,false),
				new AlphabeticalSequenceRule(3, false), new QwertySequenceRule(3, false), new WhitespaceRule()));

		if (StringUtils.isEmpty(password) && !isRequired) {
			return true;
		}

		String decryptedPassword = DecryptRSAUtil.decrypt(password);
		password = !StringUtils.isEmpty(decryptedPassword) ? decryptedPassword : password;
		RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}
		return false;
	}
}