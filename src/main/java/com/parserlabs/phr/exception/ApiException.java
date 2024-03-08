package com.parserlabs.phr.exception;

import com.parserlabs.phr.exception.model.ErrorCode;

import lombok.Getter;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = -8262500809779681557L;
	private static final String API_EXP_FORMAT = "%s: Code: [%s]-[%s], Message: %s ";

	private boolean enableTrace = false;

	@Getter
	private final ErrorCode code;

	public ApiException(ErrorCode code) {
		this.code = code;
	}

	public ApiException(ErrorCode code, String message) {
		super(message);
		this.code = code;
	}

	public ApiException(ErrorCode code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
		this.enableTrace = true;
	}

	public ApiException(ErrorCode code, Throwable cause) {
		super(cause);
		this.code = code;
		this.enableTrace = true;
	}
	
	@Override
	public String toString() {
		String trace = String.format(API_EXP_FORMAT, this.getClass().getSimpleName(), getCode().name(),
				getCode().code(), getMessage());
		if (enableTrace) {
//			trace = trace.concat(", Trace : ").concat(ExceptionUtils..getStackTrace(getCause()));
		}
		return trace;
	}

}
