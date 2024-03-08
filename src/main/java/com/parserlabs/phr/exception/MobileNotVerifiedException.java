package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.MOBILE_NUMBER_NOT_VERIFIED;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class MobileNotVerifiedException extends BusinessException {
	
	private static final long serialVersionUID = 2856084862956904299L;

	private static final ErrorCode CODE = MOBILE_NUMBER_NOT_VERIFIED;

	private static final String DEFAULT_MSG = "Please verify mobile number.";

	public MobileNotVerifiedException() {
		super(CODE, DEFAULT_MSG);
	}

	public MobileNotVerifiedException(String message) {
		super(CODE, message);
	}

	public MobileNotVerifiedException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public MobileNotVerifiedException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public MobileNotVerifiedException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}
