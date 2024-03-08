/**
 * 
 */
package com.parserlabs.phr.utils;

import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.DAY_OF_BIRTH_REGEX;
import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.MASKING_REGEX_ALPH_NUMERIC;
import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.MASKING_REGEX_NUMBER;
import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.MOBILE_NUMBER_REGEX;
import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.MONTH_OF_BIRTH_REGEX;
import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.OWSAP_EMAIL_REGEX;
import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.PINCODE_CODE_REGEX;
import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.STATE_CODE_LGD_REGEX;
import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.YEAR_OF_BIRTH_REGEX;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.config.security.SecurityClientContext;
import com.parserlabs.phr.constants.Constants;
import com.parserlabs.phr.exception.UserNotFoundException;
import com.parserlabs.phr.keyprocess.DecryptRSAUtil;
import com.parserlabs.phr.model.UserDTO;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh
 *
 */

@UtilityClass
@Slf4j
public class PhrUtilits {

	private static final ZoneId zone = ZoneId.of("Asia/Kolkata");

	public UserDTO user() {
		return clientContext().getUser();
	}

	public SecurityClientContext clientContext() {
		if (Objects.nonNull(SecurityContextHolder.getContext())
				&& Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())
				&& Objects.nonNull(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				&& SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal() instanceof SecurityClientContext) {
			SecurityClientContext clientContext = (SecurityClientContext) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

			return clientContext;
		}
		throw new UserNotFoundException();
	}

	public String clientId() {
		String clientId = PHRContextHolder.clientId();
		if (StringUtils.isEmpty(clientId)) {
			clientId = clientContext().getClientId();
			clientId = StringUtils.isEmpty(clientId) ? "" : clientId;
			PHRContextHolder.clientId(clientId);
		}
		return clientId;
	}

	/**
	 * Validate the otp time.
	 * 
	 * @param createdOtpTime
	 * @param expireTimeMin
	 * @return
	 */
	public Boolean checkExpiry(LocalDateTime createdOtpTime, long expireTimeMin) {

		Boolean isOTPExpire = false;
		long optRistricTime = expireTimeMin; // [in minutes]
		if (Objects.nonNull(createdOtpTime)) {
			long min = getTimeDiffInMin(createdOtpTime);
			log.info("created otp createdOtpTime {} and expireTimeMin {} ", createdOtpTime, expireTimeMin);

			log.info("created otp min {} and expireTimeMin {} ", min, expireTimeMin);
			isOTPExpire = optRistricTime < min;
		}
		return isOTPExpire;

	}

	/**
	 * Validate the otp time.
	 * 
	 * @param createdOtpTime
	 * @param expireTimeMin
	 * @return
	 */
	public Boolean checkExpiry(Date createdOtpTime, long expireTimeMin) {

		Boolean isOTPExpire = false;
		long optRistricTime = expireTimeMin;// [in minutes]
		if (Objects.nonNull(createdOtpTime)) {
			long currentTime = GeneralUtils.getCurrentTime();
			long createdTime = createdOtpTime.getTime();
			long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - createdTime);
			isOTPExpire = optRistricTime < minutes;
		}
		return isOTPExpire;

	}

	public String phrAddress() {
		String phrAddress = PHRContextHolder.phrAddress();
		if (StringUtils.isEmpty(phrAddress)) {
			UserDTO user = user();
			phrAddress = Objects.nonNull(user) ? user.getPhrAddress() : "";
			if (StringUtils.isEmpty(phrAddress)) {
				throw new UserNotFoundException();
			}
			PHRContextHolder.phrAddress(phrAddress);
		}
		return phrAddress;
	}

	private long getTimeDiffInMin(LocalDateTime createdTime) {
		LocalDateTime now = LocalDateTime.now();
		long minutes = ChronoUnit.MINUTES.between(createdTime, now);
		return minutes;
	}

	/**
	 * Validate the Email using OWSAP REGEX
	 * 
	 * @param email
	 * @return
	 */
	public boolean isValidEmailAddress(String email) {
		// OWSAP validation REGEX
		final String regex = OWSAP_EMAIL_REGEX;

		boolean result = false;
		try {
			if (StringUtils.isBlank(email)) {
				return false;
			}
			// initialize the Pattern object
			Pattern pattern = Pattern.compile(regex);
			// searching for occurrences of regex
			Matcher matcher = pattern.matcher(email);
			result = matcher.matches();
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	/**
	 * Validate the Mobile using OWSAP REGEX
	 * 
	 * @param email
	 * @return
	 */
	public boolean isValidMobile(String mobile) {
		// OWSAP validation REGEX
		final String regex = MOBILE_NUMBER_REGEX;

		boolean result = false;
		try {
			if (StringUtils.isBlank(mobile)) {
				return false;
			}
			// initialize the Pattern object
			Pattern pattern = Pattern.compile(regex);
			// searching for occurrences of regex
			Matcher matcher = pattern.matcher(mobile);
			result = matcher.matches();
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	/**
	 * Method to validate the state code (LGD)
	 * 
	 * @param stateCode
	 * @return true/false
	 */
	public boolean isValidStateCode(String stateCode) {
		// OWSAP validation REGEX
		final String regex = STATE_CODE_LGD_REGEX;

		boolean result = false;
		try {
			if (StringUtils.isBlank(stateCode)) {
				return false;
			}
			// initialize the Pattern object
			Pattern pattern = Pattern.compile(regex);
			// searching for occurrences of regex
			Matcher matcher = pattern.matcher(stateCode);
			result = matcher.matches();
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	/**
	 * Method to validate the pin code
	 * 
	 * @param pinCode
	 * @return true/false
	 */
	public boolean isValidPinCode(String pinCode) {
		// OWSAP validation REGEX
		final String regex = PINCODE_CODE_REGEX;

		boolean result = false;
		try {
			if (StringUtils.isBlank(pinCode)) {
				return false;
			}
			// initialize the Pattern object
			Pattern pattern = Pattern.compile(regex);
			// searching for occurrences of regex
			Matcher matcher = pattern.matcher(pinCode);
			result = matcher.matches();
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	/**
	 * Method to validate the day of birth.
	 * 
	 * @param dayOfBirth
	 * @return true/false
	 */
	public boolean isValidDayOfBirth(String dayOfBirth) {
		// OWSAP validation REGEX
		final String regex = DAY_OF_BIRTH_REGEX;

		boolean result = false;
		try {
			if (StringUtils.isBlank(dayOfBirth)) {
				return false;
			}
			// initialize the Pattern object
			Pattern pattern = Pattern.compile(regex);
			// searching for occurrences of regex
			Matcher matcher = pattern.matcher(dayOfBirth);
			result = matcher.matches();
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	/**
	 * Method to validate the month of birth.
	 * 
	 * @param monthOfBirth
	 * @return true/false
	 */
	public boolean isValidMonthOfBirth(String monthOfBirth) {
		// OWSAP validation REGEX
		final String regex = MONTH_OF_BIRTH_REGEX;

		boolean result = false;
		try {
			if (StringUtils.isBlank(monthOfBirth)) {
				return false;
			}
			// initialize the Pattern object
			Pattern pattern = Pattern.compile(regex);
			// searching for occurrences of regex
			Matcher matcher = pattern.matcher(monthOfBirth);
			result = matcher.matches();
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	/**
	 * Method to validate the day of birth.
	 * 
	 * @param yearOfBirth
	 * @return true/false
	 */
	public boolean isValidYearOfBirth(String yearOfBirth) {
		// OWSAP validation REGEX
		final String regex = YEAR_OF_BIRTH_REGEX;

		boolean result = false;
		try {
			if (StringUtils.isBlank(yearOfBirth)) {
				return false;
			}
			// initialize the Pattern object
			Pattern pattern = Pattern.compile(regex);
			// searching for occurrences of regex
			Matcher matcher = pattern.matcher(yearOfBirth);
			result = matcher.matches();
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	public long expiryTime(long interval, ChronoUnit unit) {
		LocalDateTime dateTime = LocalDateTime.now().plus(interval, unit);
		return dateTime.atZone(zone).toInstant().toEpochMilli();
	}

	/**
	 * Get the expire time string for the mail.
	 * 
	 * @param inMinutes
	 * @return
	 */
	public String getExpireTimeString(long expire_minutes) {

		long inMinutes = expire_minutes > 0 ? expire_minutes : 120;
		String expireTimeString;
		long hours = 0;
		long minutes = 0;
		if (inMinutes > 60) {
			hours = inMinutes / 60;
			minutes = inMinutes % 60;
			if (minutes > 0)
				expireTimeString = hours + " hours and " + minutes + " minutes";
			else
				expireTimeString = hours + " hours";

		} else {
			minutes = inMinutes;
			expireTimeString = minutes + " minutes";
		}
		return expireTimeString;

	}

	/**
	 * Encode the Plain passwords with Argon2 encoder for security.
	 * 
	 * @param plainText
	 * @return encode password string
	 */
	public String getEncodedPassword(String plainText) {
		String decryptedPwd = DecryptRSAUtil.decrypt(plainText);
		String password = StringUtils.isNotBlank(decryptedPwd) ? decryptedPwd : plainText;
		return StringUtils.isNoneBlank(password) ? Argon2Encoder.encode(password, Constants.MOBILE_PASSWORD_SALT) : "";
	}

	/**
	 * Masked the Data ID.
	 * 
	 * @param data
	 * @param masking_character
	 * @param masking_number    (number to character to masked from start)
	 * @return
	 */
	public String maskedNumberData(String data, String masking_character, int masking_number) {
		int masking_length = 0;
		String maskedData = null;

		if (data.length() > 0 && data.length() > masking_number) {
			masking_length = data.length() - masking_number;
			String ALTERED_MASKING_REGEX = MASKING_REGEX_NUMBER.replaceFirst("size", String.valueOf(masking_length));
			maskedData = data.replaceAll(ALTERED_MASKING_REGEX, masking_character);
		}

		return Objects.nonNull(maskedData) ? maskedData : data;
	}

	/**
	 * Masked the Data ID.
	 * 
	 * @param data
	 * @param masking_character
	 * @param masking_number    (number to character to masked from start)
	 * @return
	 */
	public String maskedData(String data, String masking_character) {
		int masking_length = 0;
		String maskedData = null;
		int masking_number = data.length() > 2 ? data.length() - 2 : 0;

		if (data.length() > 0 && data.length() > masking_number) {
			masking_length = data.length() - masking_number;
			String ALTERED_MASKING_REGEX = MASKING_REGEX_ALPH_NUMERIC.replaceFirst("size",
					String.valueOf(masking_length));
			maskedData = data.replaceAll(ALTERED_MASKING_REGEX, masking_character);
		}

		return Objects.nonNull(maskedData) ? maskedData : data;
	}

	// Security Audit add-on
	public static TransformerFactory getSecureTransformFactory() {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");

		} catch (TransformerConfigurationException e) {
			log.error("TransformerConfigurationException occured", e.getMessage());
		} catch (Exception e) {
			// log.error("Exception occured:: {}", e.getMessage());
		}

		return transformerFactory;
	}

	public String getCurrentTimeStamp() {
		SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
		return date.format(new Date());
	}
	

	


}
