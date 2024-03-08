package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.EMAIL_VERIFICATION_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;


public class EmailVerificationException extends BusinessException {
	
	private static final long serialVersionUID = 2856084862956904299L;

	private static final ErrorCode CODE = EMAIL_VERIFICATION_EXCEPTION;

	private static final String DEFAULT_MSG = "Email OTP generation failed.";

	public EmailVerificationException() {
		super(CODE, DEFAULT_MSG);
	}

	public EmailVerificationException(String message) {
		super(CODE, message);
	}

	public EmailVerificationException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public EmailVerificationException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public EmailVerificationException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}
