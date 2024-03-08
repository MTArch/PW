package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.OTP_FLOOD_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorCode;

public class OtpFloodException extends BusinessException {

	private static final long serialVersionUID = -3602559069918045534L;
	private static final ErrorCode CODE = OTP_FLOOD_EXCEPTION;

	private static final String DEFAULT_MSG = "Please wait 30 seconds before sending another OTP request.";

	public OtpFloodException() {
		super(CODE, DEFAULT_MSG);
	}

	public OtpFloodException(String message) {
		super(CODE, message);
	}

	public OtpFloodException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public OtpFloodException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
}
