package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.CAPTCHA_GENERATION_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorCode;

public class CaptchaGenerationException extends BusinessException {

	private static final long serialVersionUID = -4602425047971259594L;

	private static final ErrorCode CODE = CAPTCHA_GENERATION_EXCEPTION;

	private static final String DEFAULT_MSG = "Exception in the Security Captcha.";

	public CaptchaGenerationException() {
		super(CODE, DEFAULT_MSG);
	}

	public CaptchaGenerationException(String message) {
		super(CODE, message);
	}

	public CaptchaGenerationException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public CaptchaGenerationException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

}
