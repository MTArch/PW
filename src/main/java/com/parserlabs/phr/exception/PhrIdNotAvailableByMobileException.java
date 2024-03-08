package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PHR_ACCOUNT_NOT_AVAILABLE_BY_MOBILE;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class PhrIdNotAvailableByMobileException extends BusinessException {

	private static final long serialVersionUID = 7398509110426358199L;

	private static final ErrorCode CODE = PHR_ACCOUNT_NOT_AVAILABLE_BY_MOBILE;

	private static final String DEFAULT_MSG = "No Account found with the requested mobile.";

	public PhrIdNotAvailableByMobileException() {
		super(CODE, DEFAULT_MSG);
	}

	public PhrIdNotAvailableByMobileException(String message) {
		super(CODE, message);
	}

	public PhrIdNotAvailableByMobileException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public PhrIdNotAvailableByMobileException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public PhrIdNotAvailableByMobileException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}