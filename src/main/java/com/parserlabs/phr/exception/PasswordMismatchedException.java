package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PASSWORD_VERIFICATION_MISSMATCH;

import com.parserlabs.phr.exception.model.ErrorCode;

public class PasswordMismatchedException extends BusinessException {

	private static final long serialVersionUID = 7398509110426358199L;

	private static final ErrorCode CODE = PASSWORD_VERIFICATION_MISSMATCH;

	private static final String DEFAULT_MSG = "Password verification failed. Please enter a valid password.";

	public PasswordMismatchedException() {
		super(CODE, DEFAULT_MSG);
	}

	public PasswordMismatchedException(String message) {
		super(CODE, message);
	}

	public PasswordMismatchedException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public PasswordMismatchedException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
}