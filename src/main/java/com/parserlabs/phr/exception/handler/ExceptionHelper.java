package com.parserlabs.phr.exception.handler;

import static com.parserlabs.phr.exception.model.ErrorCode.BAD_REQUEST;
import static com.parserlabs.phr.exception.model.ErrorCode.BUSINESS_EXCEPTION;
import static com.parserlabs.phr.exception.model.ErrorCode.UNAUTHORIZED;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.parserlabs.phr.exception.AuthenticationException;
import com.parserlabs.phr.exception.BusinessException;
import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;
import com.parserlabs.phr.exception.model.ErrorDetails;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class ExceptionHelper {

	private static final String ERROR_KEY_FORMAT = "error.code.";
	private static final String CODE_DEFUALT_CODE = "$$";

	private MessageSource messageSource;

	public ApiError apiError(AuthenticationException exception, Locale locale) {
		ErrorDetails errorDetails = ErrorDetails.builder().code(exception.getCode().code())
				.message(getMessage(exception.getCode(), locale)).build();

		return ApiError.builder().code(UNAUTHORIZED.code()).message(getMessage(UNAUTHORIZED, locale))
				.details(Arrays.asList(errorDetails)).build();
	}

	public ApiError apiError(BusinessException exception, Locale locale) {
		String message = getMessage(exception.getCode(), exception, locale);
		if (Objects.nonNull(exception.getAttribute()) && !StringUtils.isEmpty(exception.getAttribute().getKey())) {
			message = message.replaceAll("#" + exception.getAttribute().getKey(), exception.getAttribute().getValue());
		}
		ErrorDetails errorDetails = ErrorDetails.builder().code(exception.getCode().code()).message(message)
				.attribute(exception.getAttribute()).build();

		return ApiError.builder().code(BUSINESS_EXCEPTION.code()).message(getMessage(BUSINESS_EXCEPTION, locale))
				.details(Arrays.asList(errorDetails)).build();
	}

	public ApiError apiError(ErrorCode code, Locale locale) {
		return ApiError.builder().code(code.code()).message(getMessage(code, locale)).build();
	}

	public ApiError apiError(MethodArgumentNotValidException exception, Locale locale) {
		List<ErrorDetails> errorDetails = populateErrorDetails(exception, locale);
		return ApiError.builder().code(BAD_REQUEST.code()).message(getMessage(BAD_REQUEST, locale))
				.details(errorDetails).build();
	}

	private String getActualErrorCode(ErrorCode code) {
		return Objects.nonNull(code) ? code.code() : "";
	}

	private ErrorCode getErrorCode(String code) {
		try {
			return ErrorCode.valueOf(code);
		} catch (Exception e) {
			log.error("No Error code found for {}.", code);
		}
		return null;
	}

	private String getMessage(ErrorCode code, Locale locale) {
		String message = "";
		if (Objects.nonNull(code)) {
			try {
				message = messageSource.getMessage(getMessageKey(code.code()), null, locale);
			} catch (NoSuchMessageException exp) {
				log.error("No Message found for {}.", code.code());
			}
		}
		return message;
	}

	private String getMessage(ErrorCode code, BusinessException exception, Locale locale) {
		String message = "";
		if (Objects.nonNull(code)) {
			try {
				message = exception.getMessage();
				if (StringUtils.isNotBlank(message) && message.startsWith(CODE_DEFUALT_CODE)) {
					message = StringUtils.substringAfter(message, CODE_DEFUALT_CODE);
				} else {
					message = messageSource.getMessage(getMessageKey(code.code()), null, locale);
				}
			} catch (NoSuchMessageException exp) {
				log.error("No Message found for {}.", code.code());
			}
		}
		return StringUtils.isNotBlank(message) ? message : exception.getMessage();
	}

	private String getMessageKey(String code) {
		return ERROR_KEY_FORMAT + code;
	}

	private String getValue(FieldError fieldError) {
		Object value = fieldError.getRejectedValue();
		return Objects.nonNull(value) ? fieldError.getRejectedValue().toString() : null;
	}

	private ErrorDetails populateErrorDetail(FieldError fieldError, Locale locale) {
		ErrorCode errorCode = getErrorCode(fieldError.getDefaultMessage());
		return ErrorDetails.builder().code(getActualErrorCode(errorCode)).message(getMessage(errorCode, locale))
				.attribute(ErrorAttribute.builder().key(fieldError.getField()).value(getValue(fieldError)).build())
				.build();
	}

	private List<ErrorDetails> populateErrorDetails(MethodArgumentNotValidException exception, Locale locale) {
		return exception.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> populateErrorDetail(fieldError, locale)).collect(Collectors.toList());

	}
}
