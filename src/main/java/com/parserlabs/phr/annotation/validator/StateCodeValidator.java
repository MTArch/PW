/**
 * 
 */
package com.parserlabs.phr.annotation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.StateCode;
import com.parserlabs.phr.utils.PhrUtilits;

/**
 * @author Rajesh
 *
 */
public class StateCodeValidator implements ConstraintValidator<StateCode, String> {

	private Boolean isRequired;
	boolean isCheck = true;

	@Override
	public void initialize(StateCode value) {
		this.isRequired = value.required();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		isCheck = StringUtils.isNotBlank(value);
		if (!isCheck && !isRequired)
			return true;

		if (isCheck) {
			return PhrUtilits.isValidStateCode(value);
		}
		return false;
	}

}
