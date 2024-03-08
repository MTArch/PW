package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.VERIFY_OTP_LOCKOUT_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class OtpFloodLockOutException extends BusinessException {

	private static final long serialVersionUID = 7013613466871238863L;

	private static final ErrorCode CODE = VERIFY_OTP_LOCKOUT_EXCEPTION;

	private static final String DEFAULT_MSG = "You've reached the maximum generate/re-send/verify attempts. Exit your browser and try again.";

	public OtpFloodLockOutException() {
		super(CODE, DEFAULT_MSG);
	}

	public OtpFloodLockOutException(String message) {
		super(CODE, message);
	}

	public OtpFloodLockOutException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public OtpFloodLockOutException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

	public OtpFloodLockOutException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}
