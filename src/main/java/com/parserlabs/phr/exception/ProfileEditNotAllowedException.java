package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PHR_ACCOUNT_EDIT_NOT_ALLOWED;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class ProfileEditNotAllowedException extends BusinessException {


	private static final long serialVersionUID = -6163831069652419368L;

	private static final ErrorCode CODE = PHR_ACCOUNT_EDIT_NOT_ALLOWED;

	private static final String DEFAULT_MSG = "Profile Edit is not allowed.";

	public ProfileEditNotAllowedException() {
		super(CODE, DEFAULT_MSG);
	}

	public ProfileEditNotAllowedException(String message) {
		super(CODE, message);
	}

	public ProfileEditNotAllowedException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public ProfileEditNotAllowedException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public ProfileEditNotAllowedException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}