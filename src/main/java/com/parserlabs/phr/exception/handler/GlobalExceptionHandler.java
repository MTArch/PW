package com.parserlabs.phr.exception.handler;

import static com.parserlabs.phr.exception.model.ErrorCode.SYSTEM_EXCEPTION;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.parserlabs.phr.exception.AuthenticationException;
import com.parserlabs.phr.exception.BusinessException;
import com.parserlabs.phr.exception.HealthIDSystemException;
import com.parserlabs.phr.exception.model.ApiError;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalExceptionHandler {

	private static final String EXCEPTION_MSG_FORMAT = "{} Execption occured while procssing the request. Exception : ";
	private ExceptionHelper helper;

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException authExp, WebRequest request) {
		log.warn(EXCEPTION_MSG_FORMAT + "{}", "Authentication", authExp.getMessage());
		return new ResponseEntity<>(helper.apiError(authExp, request.getLocale()), UNAUTHORIZED);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiError> handleBusinessException(BusinessException busExp, WebRequest request) {
		log.error(EXCEPTION_MSG_FORMAT + "{}", "Business", busExp.getMessage());
		return ResponseEntity.unprocessableEntity().body(helper.apiError(busExp, request.getLocale()));
	}

	@ExceptionHandler(HealthIDSystemException.class)
	public ResponseEntity<ApiError> handleHIDProxyException(HealthIDSystemException busExp, WebRequest request) {
		return ResponseEntity.unprocessableEntity().body(busExp.getApiError());
	}

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleSystemException(Exception sysExp, WebRequest request) {
		log.error(EXCEPTION_MSG_FORMAT, "System", sysExp);
		return new ResponseEntity<>(helper.apiError(SYSTEM_EXCEPTION, request.getLocale()), INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException valExp,
			WebRequest request) {
		return ResponseEntity.badRequest().body(helper.apiError(valExp, request.getLocale()));
	}
}
