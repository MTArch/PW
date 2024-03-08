package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.INVALID_TRANSACTION_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorCode;

public class TransactionExpiredException extends BusinessException {

	private static final long serialVersionUID = -4602425047971259594L;

	private static final ErrorCode CODE = INVALID_TRANSACTION_EXCEPTION;

	private static final String DEFAULT_MSG = " Invalid Transaction (Invalid request or transaction is expired)";

	public TransactionExpiredException() {
		super(CODE, DEFAULT_MSG);
	}

	public TransactionExpiredException(String message) {
		super(CODE, message);
	}

	public TransactionExpiredException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public TransactionExpiredException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

}
