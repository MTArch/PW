package com.parserlabs.phr.utils;

import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.HEX_REGEX;
import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.MASKING_REGEX;
import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.MASKING_REGEX_ALPH_NUMERIC;
import static com.parserlabs.phr.utils.CommonFiles.REGEX_PATTERN.MASKING_REGEX;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.context.SecurityContextHolder;

import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.config.security.SecurityClientContext;
import com.parserlabs.phr.exception.UserNotFoundException;
import com.parserlabs.phr.model.UserDTO;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class PHRIdUtils {

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
		String clientId = PHRContextHolder.clientID();
		if (ObjectUtils.isNotEmpty(clientId)) {
			clientId = clientContext().getClientId();
			clientId = ObjectUtils.isEmpty(clientId) ? "" : clientId;
			PHRContextHolder.clientId(clientId);
		}
		return clientId;
	}

	public String phrAdress() {
		String healthIdNumber = PHRContextHolder.healthIdNumber();
		if (ObjectUtils.isEmpty(healthIdNumber)) {
			UserDTO user = user();
			healthIdNumber = Objects.nonNull(user) ? user.getHealthIdNumber() : "";
			if (ObjectUtils.isEmpty(healthIdNumber)) {
				throw new UserNotFoundException();
			}
			PHRContextHolder.healthIdNumber(healthIdNumber);
		}
		return healthIdNumber;
	}

	public String xHipId() {
		String xHipId = PHRContextHolder.xHipId();
		if (ObjectUtils.isEmpty(xHipId)) {
			xHipId = clientContext().getXhipId();
			xHipId = ObjectUtils.isEmpty(xHipId) ? "" : xHipId;
			PHRContextHolder.xHipId(xHipId);
		}
		return xHipId;
	}

	public String clientIp() {
		String clientIp = PHRContextHolder.clientIp();
		if (ObjectUtils.isEmpty(clientIp)) {
			clientIp = clientContext().getClientIp();
			clientIp = ObjectUtils.isEmpty(clientIp) ? "" : clientIp;
			PHRContextHolder.clientIp();
		}
		return clientIp;
	}

	/**
	 * @param healthIdNumber
	 * @Descrition: This function is used for adding dash in health id number
	 * @return
	 */
	public static String actualHealthIdNumber(String healthIdNumber) {

		String parttern = "[0-9]{14}";
		if (!healthIdNumber.contains("-") && healthIdNumber.matches(parttern)) {
			StringBuffer temphealthIdNumber = new StringBuffer();
			String tempZeroToTwo = healthIdNumber.substring(0, 2);
			String tempTwoToSix = healthIdNumber.substring(2, 6);
			String tempSixToTen = healthIdNumber.substring(6, 10);
			String tempTenToFourteenth = healthIdNumber.substring(10, 14);
			healthIdNumber = temphealthIdNumber.append(tempZeroToTwo).append("-").append(tempTwoToSix).append("-")
					.append(tempSixToTen).append("-").append(tempTenToFourteenth).toString();

		}

		return healthIdNumber;
	}

	public String isNotEmpty(Object object) {
		return !Objects.isNull(object) ? object.toString() : "";

	}

	public String checksum(String input) {
		return DigestUtils.md5Hex(input);
	}

	public long expiryTime(long interval, ChronoUnit unit) {
		LocalDateTime dateTime = LocalDateTime.now().plus(interval, unit);
		return dateTime.atZone(zone).toInstant().toEpochMilli();
	}

	/**
	 * Generate the Ref txnid for email
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public String generateEmailVerificationTxnId() throws NoSuchAlgorithmException {
		// Initialize SecureRandom
		// This is a lengthy operation, to be done only upon
		// initialization of the application
		SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");

		// generate a random number
		String randomNum = Integer.valueOf(prng.nextInt()).toString();
		// get its digest
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		byte[] result = sha.digest(randomNum.getBytes());
		return hexEncode(result);
	}

	/**
	 * The byte[] returned by MessageDigest does not have a nice textual
	 * representation, so some form of encoding is usually performed. Another
	 * popular alternative is to use a "Base64" encoding.
	 * 
	 * @param byte[]
	 * @return
	 */
	private static String hexEncode(byte[] input) {
		StringBuilder result = new StringBuilder();
		char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		for (int idx = 0; idx < input.length; ++idx) {
			byte b = input[idx];
			result.append(digits[(b & 0xf0) >> 4]);
			result.append(digits[b & 0x0f]);
		}
		return result.toString();
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
	 * Validate the Confirmation Token.
	 * 
	 * @param email
	 * @return
	 */
	public boolean isValidConfirmationToken(String input) {
		// OWSAP validation Regex
		final String regex = HEX_REGEX;

		boolean isValid = false;
		try {
			// initialize the Pattern object
			Pattern pattern = Pattern.compile(regex);
			// searching for occurrences of Regex
			Matcher matcher = pattern.matcher(input);
			isValid = matcher.matches() && input.length() == 40;
		} catch (Exception ex) {
			isValid = false;
		}
		return isValid;
	}

	public static ByteArrayResource populateByteArray(byte[] resource) {
		return new ByteArrayResource(resource);
	}

	/**
	 * Validate the otp time.
	 * 
	 * @param createdOtpTime
	 * @param expireTimeMin
	 * @return
	 */
	public Boolean checkOtpExpiry(LocalDateTime createdOtpTime, long expireTimeMin) {

		Boolean isOTPExpire = false;
		long optRistricTime = expireTimeMin; // [in minutes]
		if (Objects.nonNull(createdOtpTime)) {
			long min = getTimeDiffInMin(createdOtpTime);
			log.info("created otp createdOtpTime {}", createdOtpTime);
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
	public Boolean checkOtpExpiry(Date createdOtpTime, long expireTimeMin) {

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

	private long getTimeDiffInMin(LocalDateTime createdTime) {
		LocalDateTime now = LocalDateTime.now();
		long minutes = ChronoUnit.MINUTES.between(createdTime, now);
		return minutes;
	}

	/**
	 * Masked the Health ID.
	 * 
	 * @param HP_ID
	 * @param masking_character
	 * @param masking_number    (number to character to masked from start)
	 * @return
	 */
	public String maskedHealthCareProfessionalID(String HP_ID, String masking_character, int masking_number, boolean backMasking) {
		int masking_length = 0;
		String maskedHPID = null;
		String REGEX_F = MASKING_REGEX_ALPH_NUMERIC;
		if (!backMasking) {
			REGEX_F = MASKING_REGEX;
		}

		if (HP_ID.length() > 0 && HP_ID.length() > masking_number) {
			masking_length = HP_ID.length() - masking_number;
			String ALTERED_MASKING_REGEX = REGEX_F.replaceFirst("size",
					String.valueOf(masking_length));
			maskedHPID = HP_ID.replaceAll(ALTERED_MASKING_REGEX, masking_character);
		}

		return Objects.nonNull(maskedHPID) ? maskedHPID : HP_ID;
	}

	public static String getSaltedValue(UserDTO user) {
		String phrAddress;
		if (user.getPhrAddress().contains("@")) {
			String[] getFirstString = user.getPhrAddress().split("@");
			phrAddress = getFirstString[0];
		} else {
			phrAddress = user.getPhrAddress().trim();
		}
		return phrAddress.toUpperCase().concat(":").concat(user.getYearOfBirth()).concat(":")
				.concat(String.valueOf(phrAddress.length()));
	}
}
