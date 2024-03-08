package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.UPDATE_OLD_NEW_VALUE_SAME;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class OldAndNewValueSameException extends BusinessException {
	
	private static final long serialVersionUID = 2856084862956904299L;

	private static final ErrorCode CODE = UPDATE_OLD_NEW_VALUE_SAME;

	private static final String DEFAULT_MSG = "The current value must is different from your last value used.";

	public OldAndNewValueSameException() {
		super(CODE, DEFAULT_MSG);
	}

	public OldAndNewValueSameException(String message) {
		super(CODE, message);
	}

	public OldAndNewValueSameException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public OldAndNewValueSameException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public OldAndNewValueSameException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}
}
