package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.INVALID_TOKEN__EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorCode;

public class InvalidTokenException  extends BusinessException{
	
	private static final long serialVersionUID = -3602559069918045534L;
	private static final ErrorCode CODE = INVALID_TOKEN__EXCEPTION;

	private static final String DEFAULT_MSG = "Please enter valid token.";

	public InvalidTokenException() {
		super(CODE, DEFAULT_MSG);
	}

	public InvalidTokenException(String message) {
		super(CODE, message);
	}

	public InvalidTokenException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public InvalidTokenException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

}
