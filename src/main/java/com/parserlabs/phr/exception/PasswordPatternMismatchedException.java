package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PASSWORD_MISSMATCH;

import com.parserlabs.phr.exception.model.ErrorCode;

public class PasswordPatternMismatchedException extends BusinessException {

	private static final long serialVersionUID = 7398509110426358199L;

	private static final ErrorCode CODE = PASSWORD_MISSMATCH;

	private static final String DEFAULT_MSG = "Password verification failed. Please enter a valid password.";

	public PasswordPatternMismatchedException() {
		super(CODE, DEFAULT_MSG);
	}

	public PasswordPatternMismatchedException(String message) {
		super(CODE, message);
	}

	public PasswordPatternMismatchedException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public PasswordPatternMismatchedException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
}