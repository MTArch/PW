package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PHR_ACCOUNT_NOT_MAPPED_TO_MOBILE;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class PhrIdNotMappedToMobileException extends BusinessException {

	private static final long serialVersionUID = 7398509110426358199L;

	private static final ErrorCode CODE = PHR_ACCOUNT_NOT_MAPPED_TO_MOBILE;

	private static final String DEFAULT_MSG = "PHR address is not mapped with the requested mobile.";

	public PhrIdNotMappedToMobileException() {
		super(CODE, DEFAULT_MSG);
	}

	public PhrIdNotMappedToMobileException(String message) {
		super(CODE, message);
	}

	public PhrIdNotMappedToMobileException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public PhrIdNotMappedToMobileException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public PhrIdNotMappedToMobileException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}