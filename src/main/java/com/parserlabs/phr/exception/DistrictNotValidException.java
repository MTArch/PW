package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.DISTRICT_NOT_VALID_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorCode;

public class DistrictNotValidException extends BusinessException {
	
	private static final long serialVersionUID = -8498255481262061602L;
	private static final ErrorCode CODE = DISTRICT_NOT_VALID_EXCEPTION;
	private static final String DEFAULT_MSG = "Please enter valid District";

	public DistrictNotValidException() {
		super(CODE, DEFAULT_MSG);
	}

	public DistrictNotValidException(String message) {
		super(CODE, message);
	}

	public DistrictNotValidException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public DistrictNotValidException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

}
