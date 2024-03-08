package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.MOBILE_OTP_EXPIRE_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;


public class OtpExpireException extends BusinessException {

	
	private static final long serialVersionUID = 326267940442106322L;
	
	

	private static final ErrorCode CODE = MOBILE_OTP_EXPIRE_EXCEPTION;

	private static final String DEFAULT_MSG = "OTP is expired/Invalid.";

	public OtpExpireException() {
		super(CODE, DEFAULT_MSG);
	}

	public OtpExpireException(String message) {
		super(CODE, message);
	}

	public OtpExpireException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public OtpExpireException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

	public OtpExpireException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}

}
