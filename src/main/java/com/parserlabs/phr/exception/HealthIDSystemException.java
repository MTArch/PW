package com.parserlabs.phr.exception;

import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.exception.model.ErrorCode;

import lombok.Data;

@Data
public class HealthIDSystemException extends RuntimeException {



	/**
	 * 
	 */
	private static final long serialVersionUID = 8326900018522212322L;
    
	private ApiError apiError;
	
	public HealthIDSystemException(ApiError code) {
		// TODO Auto-generated constructor stub
		this.apiError = code;
	}

}
