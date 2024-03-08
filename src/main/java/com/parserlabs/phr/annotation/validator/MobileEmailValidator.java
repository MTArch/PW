/**
 * 
 */
package com.parserlabs.phr.annotation.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.MobileEmailValidate;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.utils.PhrUtilits;

/**
 * @author Rajesh
 *
 */
public class MobileEmailValidator implements ConstraintValidator<MobileEmailValidate, String> {

	private Boolean isRequired;

	boolean isCheck = true;

	@Override
	public void initialize(MobileEmailValidate value) {
		this.isRequired = value.required();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		isCheck = StringUtils.isNotBlank(value);
		if (!isCheck && !isRequired)
			return true;

		if (isCheck) {
			String decryptedValue = DecryptRSAUtil.decrypt(value);
			value = Objects.nonNull(decryptedValue) ? decryptedValue: value;
			return PhrUtilits.isValidMobile(value) || PhrUtilits.isValidEmailAddress(value);
		}
		return false;
	}

}
