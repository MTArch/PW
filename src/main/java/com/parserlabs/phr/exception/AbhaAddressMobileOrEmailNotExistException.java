package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PHR_EMAIL_MOBILE_NOT_EXIST;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class AbhaAddressMobileOrEmailNotExistException extends BusinessException {
	
	private static final long serialVersionUID = 7398509110426358199L;

	private static final ErrorCode CODE = PHR_EMAIL_MOBILE_NOT_EXIST;

	private static final String DEFAULT_MSG = "The given ABHA Address does not have Mobile/Email.";

	public AbhaAddressMobileOrEmailNotExistException() {
		super(CODE, DEFAULT_MSG);
	}

	public AbhaAddressMobileOrEmailNotExistException(String message) {
		super(CODE, message);
	}

	public AbhaAddressMobileOrEmailNotExistException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public AbhaAddressMobileOrEmailNotExistException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public AbhaAddressMobileOrEmailNotExistException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}

}
