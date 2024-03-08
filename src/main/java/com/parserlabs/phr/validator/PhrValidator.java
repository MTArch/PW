package com.parserlabs.phr.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class PhrValidator {
	String parttern = "(?=^.{8,18}$)([a-zA-Z0-9]+[.]{0,1}[a-zA-Z0-9]+[_]{0,1}[a-zA-Z0-9]|[a-zA-Z0-9]+[_]{0,1}[a-zA-Z0-9]+[.]{0,1}[a-zA-Z0-9])[a-zA-Z0-9]{0,}+$";

	@Value("${phr.abha.suffix}")
	private String suffix;

	public boolean isValid(String value) {
       log.info("------------ PhrValidator  suffix = {} , ABHA Address = {} ",suffix,value);
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
