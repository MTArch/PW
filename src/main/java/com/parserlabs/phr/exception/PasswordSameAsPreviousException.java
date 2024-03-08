package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PASSWORD_SAME_AS_PREVIOUS;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class PasswordSameAsPreviousException extends BusinessException {


	private static final long serialVersionUID = -6163831069652419368L;

	private static final ErrorCode CODE = PASSWORD_SAME_AS_PREVIOUS;

	private static final String DEFAULT_MSG = "The current password must is different from your last password used.";

	public PasswordSameAsPreviousException() {
		super(CODE, DEFAULT_MSG);
	}

	public PasswordSameAsPreviousException(String message) {
		super(CODE, message);
	}

	public PasswordSameAsPreviousException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public PasswordSameAsPreviousException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public PasswordSameAsPreviousException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}