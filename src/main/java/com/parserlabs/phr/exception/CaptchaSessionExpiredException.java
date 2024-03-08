package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.CAPTCHA_SESSSION_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorCode;

public class CaptchaSessionExpiredException extends BusinessException {

	private static final long serialVersionUID = 4824894828531468439L;

	private static final ErrorCode CODE = CAPTCHA_SESSSION_EXCEPTION;

	private static final String DEFAULT_MSG = "Sorry, Your session is expired. Please login to continue.";

	public CaptchaSessionExpiredException() {
		super(CODE, DEFAULT_MSG);
	}

	public CaptchaSessionExpiredException(String message) {
		super(CODE, message);
	}

	public CaptchaSessionExpiredException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public CaptchaSessionExpiredException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

}
