package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.RESOURCE_NOT_AUTHORIZED;

import com.parserlabs.phr.exception.model.ErrorCode;

public class ResourceNotAuthorizedException extends AuthenticationException {

	private static final long serialVersionUID = 1458621256250882707L;

	private static final ErrorCode CODE = RESOURCE_NOT_AUTHORIZED;

	public ResourceNotAuthorizedException(String message) {
		super(CODE, message);
	}

}
