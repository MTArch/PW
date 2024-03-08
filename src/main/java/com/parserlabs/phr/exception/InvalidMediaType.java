package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.INVALID_MEDIA_TYPE;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;


public class InvalidMediaType extends BusinessException {

	
	private static final long serialVersionUID = 326267940442106322L;
	
	

	private static final ErrorCode CODE = INVALID_MEDIA_TYPE;

	private static final String DEFAULT_MSG = "Invalid Media Type.";

	public InvalidMediaType() {
		super(CODE, DEFAULT_MSG);
	}

	public InvalidMediaType(String message) {
		super(CODE, message);
	}

	public InvalidMediaType(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public InvalidMediaType(String message, Throwable cause) {
		super(CODE, message, cause);
	}

	public InvalidMediaType(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}

}
