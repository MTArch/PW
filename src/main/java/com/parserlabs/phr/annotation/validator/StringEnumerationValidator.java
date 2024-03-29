/**
 * 
 */
package com.parserlabs.phr.annotation.validator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.parserlabs.phr.annotation.StringEnumeration;

/**
 * @author Rajesh
 *
 */
public class StringEnumerationValidator implements ConstraintValidator<StringEnumeration, String> {

	private Set<String> AVAILABLE_ENUM_NAMES;

	public static Set<String> getNamesSet(Class<? extends Enum<?>> e) {
		Enum<?>[] enums = e.getEnumConstants();
		String[] names = new String[enums.length];
		for (int i = 0; i < enums.length; i++) {
			names[i] = enums[i].name();
		}
		Set<String> mySet = new HashSet<String>(Arrays.asList(names));
		return mySet;
	}

	@Override
	public void initialize(StringEnumeration stringEnumeration) {
		Class<? extends Enum<?>> enumSelected = stringEnumeration.enumClass();
		AVAILABLE_ENUM_NAMES = getNamesSet(enumSelected);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		} else {
			return AVAILABLE_ENUM_NAMES.contains(value);
		}
	}

}
