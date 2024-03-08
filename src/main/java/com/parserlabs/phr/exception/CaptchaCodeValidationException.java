package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.CAPTCHA_CODE_VALIDATION_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorCode;

public class CaptchaCodeValidationException extends BusinessException {

	private static final long serialVersionUID = 1L;

	private static final ErrorCode CODE = CAPTCHA_CODE_VALIDATION_EXCEPTION;

	private static final String DEFAULT_MSG = "Invalid Captcha Code!!";

	public CaptchaCodeValidationException() {
		super(CODE, DEFAULT_MSG);
	}

	public CaptchaCodeValidationException(String message) {
		super(CODE, message);
	}

	public CaptchaCodeValidationException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public CaptchaCodeValidationException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

}
