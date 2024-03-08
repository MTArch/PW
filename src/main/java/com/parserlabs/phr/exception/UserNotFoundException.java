package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.USER_NOT_FOUND;

import com.parserlabs.phr.exception.model.ErrorCode;

public class UserNotFoundException extends BusinessException {

	private static final ErrorCode CODE = USER_NOT_FOUND;

	private static final String DEFAULT_MSG = "User does not exist for the phr address/mobile/email.";
	private static final long serialVersionUID = 487703573561058809L;

	public UserNotFoundException() {
		super(CODE, DEFAULT_MSG);
	}

	public UserNotFoundException(String message) {
		super(CODE, message);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

	public UserNotFoundException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
