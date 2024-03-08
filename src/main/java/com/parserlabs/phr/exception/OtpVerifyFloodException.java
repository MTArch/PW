package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.VERIFY_OTP_FLOOD_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class OtpVerifyFloodException extends BusinessException {

	private static final long serialVersionUID = 7013613466871238863L;

	private static final ErrorCode CODE = VERIFY_OTP_FLOOD_EXCEPTION;

	private static final String DEFAULT_MSG = "You've reached the maximum generate/re-send/verify attempts. Exit your browser and try again.";

	public OtpVerifyFloodException() {
		super(CODE, DEFAULT_MSG);
	}

	public OtpVerifyFloodException(String message) {
		super(CODE, message);
	}

	public OtpVerifyFloodException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public OtpVerifyFloodException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

	public OtpVerifyFloodException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}
