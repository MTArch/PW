package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.HEALTHIDNUMBER_NOT_FOUND;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class HealthIdNumberNotFoundException extends BusinessException {

	private static final long serialVersionUID = -5220502470565550149L;

	private static final ErrorCode CODE = HEALTHIDNUMBER_NOT_FOUND;

	private static final String DEFAULT_MSG = "No Account found with the requested HealthIdNumber.";

	public HealthIdNumberNotFoundException() {
		super(CODE, DEFAULT_MSG);
	}

	public HealthIdNumberNotFoundException(String message) {
		super(CODE, message);
	}

	public HealthIdNumberNotFoundException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public HealthIdNumberNotFoundException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public HealthIdNumberNotFoundException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}