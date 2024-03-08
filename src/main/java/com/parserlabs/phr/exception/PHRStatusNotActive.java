package com.parserlabs.phr.exception;

import static com.parserlabs.phr.exception.model.ErrorCode.PHR_STATUS_NOT_ACTIVE;

import org.springframework.boot.web.reactive.error.ErrorAttributes;

import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class PHRStatusNotActive extends BusinessException {

	

	private static final long serialVersionUID = -1253916939840486017L;

	private static final ErrorCode CODE = PHR_STATUS_NOT_ACTIVE;

	private static final String DEFAULT_MSG = "Abha Address Status is not active";

	
	public PHRStatusNotActive( ) {
		super(CODE, DEFAULT_MSG);
	}



	public PHRStatusNotActive(ErrorAttribute attribute) {
		super(CODE, DEFAULT_MSG,attribute);
	}}
