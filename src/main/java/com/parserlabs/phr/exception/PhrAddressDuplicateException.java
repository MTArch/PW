package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PHR_ID_ALREADY_EXISTS;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class PhrAddressDuplicateException extends BusinessException {
	
	private static final long serialVersionUID = 2856084862956904299L;

	private static final ErrorCode CODE = PHR_ID_ALREADY_EXISTS;

	private static final String DEFAULT_MSG = "PHR address already exists";

	public PhrAddressDuplicateException() {
		super(CODE, DEFAULT_MSG);
	}

	public PhrAddressDuplicateException(String message) {
		super(CODE, message);
	}

	public PhrAddressDuplicateException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public PhrAddressDuplicateException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public PhrAddressDuplicateException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}
