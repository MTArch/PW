package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PHR_ACCOUNT_NOT_AVAILABLE_BY_EMAIL;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class PhrIdNotAvailableByEmailException extends BusinessException {

	private static final long serialVersionUID = 7398509110426358199L;

	private static final ErrorCode CODE = PHR_ACCOUNT_NOT_AVAILABLE_BY_EMAIL;

	private static final String DEFAULT_MSG = "No Account found with the requested email Address.";

	public PhrIdNotAvailableByEmailException() {
		super(CODE, DEFAULT_MSG);
	}

	public PhrIdNotAvailableByEmailException(String message) {
		super(CODE, message);
	}

	public PhrIdNotAvailableByEmailException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public PhrIdNotAvailableByEmailException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public PhrIdNotAvailableByEmailException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}