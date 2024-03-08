package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.MOBILE_NUMBER_NULL;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class MobileNumberNullException extends BusinessException {
	
	private static final long serialVersionUID = 2856084862956904299L;

	private static final ErrorCode CODE = MOBILE_NUMBER_NULL;

	private static final String DEFAULT_MSG = "Mobile number is blank.";

	public MobileNumberNullException() {
		super(CODE, DEFAULT_MSG);
	}

	public MobileNumberNullException(String message) {
		super(CODE, message);
	}

	public MobileNumberNullException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public MobileNumberNullException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public MobileNumberNullException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}

	
}
