package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.DATABASE_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorCode;

public class DatabaseException extends SystemException {

	private static final long serialVersionUID = -4437131025229714979L;
	private static final ErrorCode CODE = DATABASE_EXCEPTION;
	private static final String DEFAULT_MESSAGE = "Exception occured while calling this database exception.";

	public DatabaseException() {
		super(CODE, DEFAULT_MESSAGE);
	}

	public DatabaseException(Throwable cause) {
		super(CODE, DEFAULT_MESSAGE, cause);
	}

	public DatabaseException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

	public DatabaseException(String message) {
		super(CODE, message);
	}

}
