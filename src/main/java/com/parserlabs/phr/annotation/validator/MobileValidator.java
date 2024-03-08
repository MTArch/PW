
package com.parserlabs.phr.annotation.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.parserlabs.phr.annotation.Mobile;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.utils.PhrUtilits;

/**
 *
 * @author suraj.singh
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

	private Boolean isOptional;

	boolean mobileCheck = true;

	@Override
	public void initialize(Mobile mobile) {
		this.isOptional = mobile.optional();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext cvc) {
		if (isOptional) {
			mobileCheck = !StringUtils.isEmpty(value);
		}
		if (mobileCheck) {
			if (StringUtils.isBlank(value))
				return false;

			String decryptedValue = DecryptRSAUtil.decrypt(value);
			value = Objects.nonNull(decryptedValue) ? decryptedValue : value;
			return PhrUtilits.isValidMobile(value);
		}
		return true;

	}

}
