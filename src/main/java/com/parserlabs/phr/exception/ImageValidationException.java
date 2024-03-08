package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.IMAGE_VALIDATION_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorCode;

public class ImageValidationException extends BusinessException {

	private static final ErrorCode CODE = IMAGE_VALIDATION_EXCEPTION;

	private static final String DEFAULT_MSG = "Image size is invalid.";
	private static final long serialVersionUID = 487703573561058809L;

	public ImageValidationException() {
		super(CODE, DEFAULT_MSG);
	}

	public ImageValidationException(String message) {
		super(CODE, message);
	}

	public ImageValidationException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

	public ImageValidationException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
