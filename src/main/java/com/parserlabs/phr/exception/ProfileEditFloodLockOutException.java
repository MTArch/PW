package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PROFILE_EDIT_LOCKOUT_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class ProfileEditFloodLockOutException extends BusinessException {

	private static final long serialVersionUID = 7013613466871238863L;

	private static final ErrorCode CODE = PROFILE_EDIT_LOCKOUT_EXCEPTION;

	private static final String DEFAULT_MSG = "Profile edit locked due to the maximum profile edit attempts exceeded";

	public ProfileEditFloodLockOutException() {
		super(CODE, DEFAULT_MSG);
	}

	public ProfileEditFloodLockOutException(String message) {
		super(CODE, message);
	}

	public ProfileEditFloodLockOutException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public ProfileEditFloodLockOutException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

	public ProfileEditFloodLockOutException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}
