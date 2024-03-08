package com.parserlabs.phr.exception;
import static com.parserlabs.phr.exception.model.ErrorCode.BAD_REQUEST;
import com.parserlabs.phr.exception.model.ErrorAttribute;
import com.parserlabs.phr.exception.model.ErrorCode;

public class PhrAddressNotValidException extends BusinessException{
	
	private static final long serialVersionUID = 7398509110426358199L;

	private static final ErrorCode CODE = BAD_REQUEST;

	private static final String DEFAULT_MSG = "Phr address is not valid.";

	public PhrAddressNotValidException() {
		super(CODE, DEFAULT_MSG);
	}

	public PhrAddressNotValidException(String message) {
		super(CODE, message);
	}

	public PhrAddressNotValidException(Throwable cause) {
		super(CODE, DEFAULT_MSG, cause);
	}

	public PhrAddressNotValidException(String message, Throwable cause) {
		super(CODE, message, cause);
	}
	
	public PhrAddressNotValidException(String message, ErrorAttribute attribute) {
		super(CODE, message, attribute);
	}

}
