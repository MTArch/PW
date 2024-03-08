package com.parserlabs.phr.annotation.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.parserlabs.phr.annotation.PHRAddress;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PHRAddressValidator implements ConstraintValidator<PHRAddress, String> {

	String parttern = "(?=^.{8,18}$)([a-zA-Z0-9]+[.]{0,1}[a-zA-Z0-9]+[_]{0,1}[a-zA-Z0-9]|[a-zA-Z0-9]+[_]{0,1}[a-zA-Z0-9]+[.]{0,1}[a-zA-Z0-9])[a-zA-Z0-9]{0,}+$";

	@Value("${phr.abha.suffix}")
	private String suffix;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
       log.info("------------  suffix = {} , ABHA Address = {} ",suffix,value);
		if (value.chars().filter(c -> c == '@').count() > 1 || containsNOTABDM(value)) {
			return false;
		}
		if ((StringUtils.hasLength(value) && (value.contains("@")))) {
			value = value.split("@")[0];
		}
		return Pattern.compile(parttern).matcher(value).matches();
	}

	private boolean containsNOTABDM(String value) {
		return value.contains("@") && !(value.trim().endsWith(this.suffix));
	}
}
