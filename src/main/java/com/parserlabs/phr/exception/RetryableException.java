package com.parserlabs.phr.exception;

public class RetryableException extends RuntimeException {

	private static final long serialVersionUID = 1245761121392327809L;

	private static final String DEFAULT_MSG = "Exception occoured while calling a thrid party service. Retrying after some time";

	public RetryableException() {
		super(DEFAULT_MSG);
	}

	public RetryableException(String message) {
		super(message);
	}

}
