package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PHR_LOGIN_AUTH_METHOD_NOT_AVAILABLE;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class LoginMethodNotAvailableException extends BusinessException {

	private static final long serialVersionUID = 7398509110426358199L;

	private static final ErrorCode CODE = PHR_LOGIN_AUTH_METHOD_NOT_AVAILABLE;

	private static final String DEFAULT_MSG = "Login Auth not found for the requested Phr Address/Id.";

	public LoginMethodNotAvailableException() {
		super(CODE, DEFAULT_MSG);
	}

	public LoginMethodNotAvailableException(String message) {
		super(CODE, message);
	}

	public LoginMethodNotAvailableException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public LoginMethodNotAvailableException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public LoginMethodNotAvailableException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}