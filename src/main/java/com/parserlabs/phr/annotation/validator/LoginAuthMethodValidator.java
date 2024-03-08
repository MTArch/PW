package com.parserlabs.phr.annotation.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.parserlabs.phr.annotation.LoginAuthMethod;
import com.parserlabs.phr.enums.LoginMethodsEnum;

/**
 * @author Rajesh kr
 * @apiNote Login method validation.
 * 
 */
public class LoginAuthMethodValidator implements ConstraintValidator<LoginAuthMethod, LoginMethodsEnum> {

	private Boolean isRequired;

	@Override
	public void initialize(LoginAuthMethod loginAuthMethod) {
		this.isRequired = loginAuthMethod.required();
	}

	@Override
	public boolean isValid(LoginMethodsEnum value, ConstraintValidatorContext context) {

		boolean checkLoginMethod = true;

		if (isRequired) {
			checkLoginMethod = Objects.nonNull(value);
		}

		if (checkLoginMethod) {
			return LoginMethodsEnum.isValidByLoginCode(value.name());
		}
		return false;
	}

}
