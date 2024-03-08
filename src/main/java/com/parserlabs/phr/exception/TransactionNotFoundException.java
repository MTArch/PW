package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.TRANSACTION_NOT_FOUND_EXCEPTION;

import com.parserlabs.phr.exception.model.ErrorCode;

public class TransactionNotFoundException extends BusinessException {

	private static final long serialVersionUID = -4602425047971259594L;

	private static final ErrorCode CODE = TRANSACTION_NOT_FOUND_EXCEPTION;

	private static final String DEFAULT_MSG = " Transaction is not found for UUID";

	public TransactionNotFoundException() {
		super(CODE, DEFAULT_MSG);
	}

	public TransactionNotFoundException(String message) {
		super(CODE, message);
	}

	public TransactionNotFoundException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public TransactionNotFoundException(String message, Throwable cause) {
		super(CODE, message, cause);
	}

}
