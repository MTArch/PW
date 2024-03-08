package com.parserlabs.phr.utils;

public interface CommonFiles {

	static interface REGEX_PATTERN {
		public static final String OWSAP_EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		public static final String HEX_REGEX = "^[a-fA-F0-9]+$";
		public static final String MASKING_REGEX_NUMBER = "\\d(?=(?:\\D*\\d){size})";
		public static final String MASKING_REGEX_ALPH_NUMERIC = "(?<=.{size}).";
		public static final String MASKING_REGEX = "[A-Za-z0-9._%+-](?=(?:\\D*[A-Za-z0-9._%+-]){size})";

		public static final String MOBILE_NUMBER_REGEX = "(\\+91)?[1-9][0-9]{9}";

		public static final String STATE_CODE_LGD_REGEX = "^(?!25|26)([1-9]|[12][0-9]|3[0-8])$";
		public static final String PINCODE_CODE_REGEX ="^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
		
		public static final String DAY_OF_BIRTH_REGEX = "^(0?[1-9]|[12][0-9]|3[01])$";
		public static final String MONTH_OF_BIRTH_REGEX =  "^([1-9]|1[012])$";
		public static final String YEAR_OF_BIRTH_REGEX = "^(19|20)[0-9][0-9]$";
		
	}

}
