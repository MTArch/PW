package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PHR_ACCOUNT_NOT_AVAILABLE;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class PhrIdNotAvailableException extends BusinessException {

	private static final long serialVersionUID = 7398509110426358199L;

	private static final ErrorCode CODE = PHR_ACCOUNT_NOT_AVAILABLE;

	private static final String DEFAULT_MSG = "No Account found with the requested Phr Address/Id.";

	public PhrIdNotAvailableException() {
		super(CODE, DEFAULT_MSG);
	}

	public PhrIdNotAvailableException(String message) {
		super(CODE, message);
	}

	public PhrIdNotAvailableException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public PhrIdNotAvailableException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public PhrIdNotAvailableException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}