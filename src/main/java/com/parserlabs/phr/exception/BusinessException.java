package com.parserlabs.phr.exception;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

import lombok.Getter;

@Getter
public class BusinessException extends ApiException {

	private static final long serialVersionUID = 4151569448693017560L;

	private ErrorAttribute attribute;
	private Object[] messageArgs;
	
	

	public BusinessException(ErrorCode code) {
		super(code);
	}

	public BusinessException(ErrorCode code, ErrorAttribute attribute) {
		super(code);
		this.attribute = attribute;
	}

	public BusinessException(ErrorCode code, ErrorAttribute attribute, Throwable cause) {
		super(code, cause);
		this.attribute = attribute;
	}

	public BusinessException(ErrorCode code, String message) {
		super(code, message);
	}

	public BusinessException(ErrorCode code, String message, ErrorAttribute attribute) {
		super(code, message);
		this.attribute = attribute;
	}

	public BusinessException(ErrorCode code, String message, ErrorAttribute attribute, Throwable cause) {
		super(code, message, cause);
		this.attribute = attribute;
	}
	
	public BusinessException(ErrorCode code, String message, ErrorAttribute attribute, String... messageArgs) {
		super(code, message);
		this.attribute = attribute;
		this.messageArgs = messageArgs;
	}

	public BusinessException(ErrorCode code, String message, ErrorAttribute attribute, Throwable cause, String... messageArgs) {
		super(code, message, cause);
		this.attribute = attribute;
		this.messageArgs = messageArgs;
	}

	public BusinessException(ErrorCode code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public BusinessException(ErrorCode code, Throwable cause) {
		super(code, cause);
	}
}
