package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.OTP_MISSMATCH;

import com.parserlabs.phr.exception.model.ErrorCode;

public class OtpMismatchedException extends BusinessException {

	private static final long serialVersionUID = 7398509110426358199L;

	private static final ErrorCode CODE = OTP_MISSMATCH;

	private static final String DEFAULT_MSG = "OTP verification failed. Please enter a valid OTP.";

	public OtpMismatchedException() {
		super(CODE, DEFAULT_MSG);
	}

	public OtpMismatchedException(String message) {
		super(CODE, message);
	}

	public OtpMismatchedException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public OtpMismatchedException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
}