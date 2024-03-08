package com.parserlabs.phr.constants;

public interface Constants {

	interface DeveloperMessage {
		String ALPHABET_MISSMATCHED = "ALPHABET_MISSMATCHED";
		String FIELD_REQUIRED = "FIELD_REQUIRED";
		String INCORRECT_TYPE = "INCORRECT_TYPE";
		String MOBILE_NUMBER_MISSMATCH = "MOBILE_NUMBER_MISSMATCH";
		String OTP_MISSMATCH = "OTP_MISSMATCH";
		String PASSWORD_MISSMATCH = "PASSWORD_MISSMATCH";
		String PATTREN_MISMATCHED = "PATTREN_MISMATCHED";
		String SIZE_MISMATCHED = "SIZE_MISMATCHED";
		String UUID_MISSMATCH = "UUID_MISSMATCH";
		String DATE_MISSMATCH = "DATE_MISSMATCH";
		String HEALTH_ID = "HEALTH_ID";
		String AADHAAR_NUMBER_INVALID = "AADHAAR_NUMBER_INVALID";
		String AADHAAR_BIOTYPE_INVALID = "AADHAAR_BIOTYPE_INVALID";
		String INVALID_OIDC_REQUEST_CODE = "INVALID_OIDC_REQUEST_CODE";
		String INVALID_NAME_FORMAT = "INVALID_NAME_FORMAT";
		String INVALID_EMAIL_FORMAT = "INVALID_EMAIL_FORMAT";
		String INVALID_DOC_TYPE = "INVALID_DOC_TYPE";
		String GENDER_INVALID = "GENDER_INVALID";
		String DATA_INVALID = "DATA_INVALID";
		String MOBILE_NUMBER_EMAIL_MISSMATCH = "MOBILE_NUMBER_EMAIL_MISSMATCH";
		String INVALID_ENCRYPTION_MISSMATCH = "INVALID_ENCRYPTION_MISSMATCH";

	}

	interface DocumentMessages {

		String MESSAGE_Code_401 = "Unauthorized Access.";
		String MESSAGE_Code_400 = "Bad request, check request before retrying";
		String MESSAGE_Code_500 = "Downstream system(s) is down.\nUnhandled exceptions.";

	}

	public static final String REQUESTER_ID_HEADER = "requesterId";
	public static final String REQUESTER_TYPE_HEADER = "requesterType";
	public static final String PURPOSE_HEADER = "purpose";

	public static final String TRANSECTION_ID = "txnId";
	public static final String CLIENT_ID = "clientId";
	public static final String PHR_ADDRESS = "phrAddress";
	public static final String PHR_MOBILE = "phrMobile";
    public static final String SYSTEM = "system";

	public static final String MOBILE_PASSWORD_SALT = "PHR_MOBILE";
	public static final String MOBILE_EMAIL_SALT = "PHR_EMAIL";
	public static final String PHR_ID_SUFFIX = "SUFFIX";
	
	public static final String X_TOKEN_HEADER = "X-Token";
	
	public static final String BEARER = "Bearer ";
	
	public static final String NAME = "name";
	public static final String GENDER = "gender";
	public static final String ADDRESS = "address";
	

	
	public static final String CAPTCHA_TEXT_KEY = "captcha_text";
	public static final String CAPTCHA_ANSWER_KEY = "captcha_answer";
	
	public static final String HEALTH_ID_NUMBER="healthIdNumber";
	public static final String DAY_OF_BIRTH="dayOfBirth";
	public static final String MONTH_OF_BIRTH="monthOfBirth";
	public static final String YEAR_OF_BIRTH="yearOfBirth";
	public static final String FULL_NAME="fullName";
	public static final String MOBILE="mobile";
	public static final String EMAIL="email";
	public static final String ADDRESS_LINE="addressLine";
	public static final String STATE_NAME="stateName";
	public static final String DISTRICT_NAME="districtName";
	public static final String PINCODE="pincode";
	public static final String FACE_NOT_FOUND = "FACE_NOT_FOUND";

	


}
