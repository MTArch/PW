/**
 * 
 */
package com.parserlabs.phr.annotation.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.Email;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.utils.PhrUtilits;

/**
 * @author Rajesh
 *
 */
public class EmailValidator implements ConstraintValidator<Email, String> {

	private Boolean isOptional;

	@Override
	public void initialize(Email email) {
		this.isOptional = email.optional();

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		boolean checkEmail = true;
		if (isOptional) {
			checkEmail = StringUtils.isNotBlank(value);
		}
		
		if (checkEmail) {
			if (StringUtils.isBlank(value))
				return false;
			
			String decryptedValue = DecryptRSAUtil.decrypt(value);
			value = Objects.nonNull(decryptedValue) ? decryptedValue : value;
			return PhrUtilits.isValidEmailAddress(value);
		}
		return true;
	}

}
