package com.parserlabs.phr.exception;

import com.parserlabs.phr.exception.model.ErrorCode;

public class SystemException extends ApiException {

	private static final ErrorCode CODE = ErrorCode.SYSTEM_EXCEPTION;
	private static final long serialVersionUID = 3592923536345132037L;

	public SystemException(String message) {
		super(CODE, message);
	}

	public SystemException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public SystemException(ErrorCode code, String message, Throwable cause) {
		super(code, message, cause);
	}
	
	public SystemException(ErrorCode code, String message) {
		super(code, message);
	}
}
