/**
 * 
 */
package com.parserlabs.phr.utils;

import static com.parserlabs.phr.constants.Constants.PHR_ID_SUFFIX;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.exception.InvalidMediaType;

import lombok.experimental.UtilityClass;

/**
 * @author Rajesh
 *
 */
@UtilityClass
public class GeneralUtils {
	private static int offset;

	private static String phrIdSuffix = System.getProperty(PHR_ID_SUFFIX, "@abdm");

	public static String getRandomCode(long nrOfDigits) {
		if (nrOfDigits < 1) {
			throw new RuntimeException("Number of digits must be bigger than 0");
		}

		return RandomStringUtils.randomNumeric((int) nrOfDigits);
	}

	public static Long getCurrentTime() {
		return System.currentTimeMillis() + (offset * 1000);
	}

	public static String sanetizePhrAddress(String healthIdStr) {
		
		
		if (!StringUtils.isEmpty(healthIdStr)) {
			healthIdStr = healthIdStr.toLowerCase();
			if (!StringUtils.isEmpty(healthIdStr) && !healthIdStr.contains("@")) {
				healthIdStr = healthIdStr + phrIdSuffix;
			}
			else if(!StringUtils.isEmpty(healthIdStr) && healthIdStr.contains("@ndhm")) {
				healthIdStr = healthIdStr.replace("@ndhm",phrIdSuffix);	
			}
			return healthIdStr.toLowerCase();
		}
		return healthIdStr;
	}

	public static String deSanetizePhrAddress(String healthIdStr) {
		if (!StringUtils.isEmpty(healthIdStr)) {
			if (!StringUtils.isEmpty(healthIdStr) && healthIdStr.contains("@")) {
				healthIdStr = healthIdStr.replace(phrIdSuffix, "");
			}
			return healthIdStr;
		}
		return healthIdStr;
	}

	public static String getClientIP(HttpServletRequest request) {
		final String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader != null) {
			return xfHeader.split(",")[0];
		}
		return request.getRemoteAddr();
	}
	
	public static String stringTrimmer(String str) {
		return StringUtils.isEmpty(str) ? str : str.trim();
	}
	
	public String populatePHRAddress(String... values) {
		return sanetizePhrAddress(Stream.of(values).map(data -> data.strip()).filter(data -> !StringUtils.isEmpty(data))
				.collect(Collectors.joining("")));

	}

	public Set<String> populatePHRAddress(PhrTransactionEntity entity) {
		LinkedHashSet<String> phrAddress = new LinkedHashSet<String>();

		String dayOfBirth = !StringUtils.isEmpty(entity.getDayOfBirth()) ? entity.getDayOfBirth()
				: "";
		String monthOfBirth = !StringUtils.isEmpty(entity.getMonthOfBirth()) ? entity.getMonthOfBirth()
				: "";
		if (!StringUtils.isEmpty(entity.getFirstName()))
		{	
			phrAddress.add(populatePHRAddress(entity.getFirstName()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(),dayOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(),monthOfBirth));
		}
		
		if (!StringUtils.isEmpty(entity.getLastName()) && !StringUtils.isEmpty(entity.getFirstName())) {
			phrAddress.add(populatePHRAddress(entity.getFirstName(), entity.getLastName()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), ".", entity.getLastName()));
			
			// Abha Address combination of FirstName + LastName + DayOfBirth
			phrAddress.add(populatePHRAddress(entity.getFirstName(), entity.getLastName(),dayOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), ".", entity.getLastName(),dayOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), "_", entity.getLastName(),dayOfBirth));
		
			// Abha Address combination of FirstName + LastName + MonthOfBirth
			phrAddress.add(populatePHRAddress(entity.getFirstName(), entity.getLastName(),monthOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), ".", entity.getLastName(),monthOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), "_", entity.getLastName(),monthOfBirth));

			// Abha Address combination of FirstName + LastName + YearOfBirth
			phrAddress.add(populatePHRAddress(entity.getFirstName(), entity.getLastName(),entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), ".", entity.getLastName(),entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), "_", entity.getLastName(),entity.getYearOfBirth()));
	
			// Abha Address combination of FirstName + LastName + DayOfBirth + MonthOfBirth
			phrAddress.add(populatePHRAddress(entity.getFirstName(), entity.getLastName(),dayOfBirth,monthOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), ".", entity.getLastName(),dayOfBirth,monthOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), "_", entity.getLastName(),dayOfBirth,monthOfBirth));
			
			// Abha Address combination of FirstName + LastName + DayOfBirth + MonthOfBirth + YearOfBirth
			phrAddress.add(populatePHRAddress(entity.getFirstName(), entity.getLastName(),dayOfBirth,monthOfBirth,entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), ".", entity.getLastName(),dayOfBirth,monthOfBirth,entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), "_", entity.getLastName(),dayOfBirth,monthOfBirth,entity.getYearOfBirth()));
			
			// Abha Address combination of FirstName(Initial) + LastName + DayOfBirth
			String initialFName=Character.toString(entity.getFirstName().charAt(0));
			phrAddress.add(populatePHRAddress(initialFName, entity.getLastName(),dayOfBirth));
			phrAddress.add(populatePHRAddress(initialFName, ".", entity.getLastName(),dayOfBirth));
			phrAddress.add(populatePHRAddress(initialFName, "_", entity.getLastName(),dayOfBirth));
			
			
			// Abha Address combination of FirstName + LastName(Initial) + DayOfBirth
			String initialLName=Character.toString(entity.getLastName().charAt(0));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), initialLName,dayOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), ".", initialLName,dayOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), "_", initialLName,dayOfBirth));
			
			// Abha Address combination of FirstName(Initial) + LastName + monthOfBirth
			phrAddress.add(populatePHRAddress(initialFName, entity.getLastName(),monthOfBirth));
			phrAddress.add(populatePHRAddress(initialFName, ".", entity.getLastName(),monthOfBirth));
			phrAddress.add(populatePHRAddress(initialFName, "_", entity.getLastName(),monthOfBirth));
			
			// Abha Address combination of FirstName + LastName(Initial) + monthOfBirth
			phrAddress.add(populatePHRAddress(entity.getFirstName(), initialLName,monthOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), ".", initialLName,monthOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), "_", initialLName,monthOfBirth));
			
			
			// Abha Address combination of FirstName + LastName(Initial) + YearOfBirth
			phrAddress.add(populatePHRAddress(initialFName, entity.getLastName(),entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(initialFName, ".", entity.getLastName(),entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(initialFName, "_", entity.getLastName(),entity.getYearOfBirth()));
			
			
			// Abha Address combination of FirstName + LastName(Initial) + YearOfBirth
			phrAddress.add(populatePHRAddress(entity.getFirstName(), initialLName,entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), ".", initialLName,entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), "_", initialLName,entity.getYearOfBirth()));
			
			
			// Abha Address combination of FirstName(Initial) + LastName + DayOfBirth + monthOfBirth
			phrAddress.add(populatePHRAddress(initialFName, entity.getLastName(),dayOfBirth,monthOfBirth));
			phrAddress.add(populatePHRAddress(initialFName, ".", entity.getLastName(),dayOfBirth,monthOfBirth));
			phrAddress.add(populatePHRAddress(initialFName, "_", entity.getLastName(),dayOfBirth,monthOfBirth));
			
			
			// Abha Address combination of FirstName + LastName(Initial) + DayOfBirth + monthOfBirth
			phrAddress.add(populatePHRAddress(entity.getFirstName(), initialLName,dayOfBirth,monthOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), ".", initialLName,dayOfBirth,monthOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), "_", initialLName,dayOfBirth,monthOfBirth));
			
			// Abha Address combination of FirstName(Initial) + LastName + DayOfBirth + monthOfBirth + YearOfBirth
			phrAddress.add(populatePHRAddress(initialFName, entity.getLastName(),dayOfBirth,monthOfBirth,entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(initialFName, ".", entity.getLastName(),dayOfBirth,monthOfBirth,entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(initialFName, "_", entity.getLastName(),dayOfBirth,monthOfBirth,entity.getYearOfBirth()));
			
			// Abha Address combination of FirstName + LastName(Initial) + DayOfBirth + monthOfBirth + YearOfBirth
			phrAddress.add(populatePHRAddress(entity.getFirstName(), initialLName,dayOfBirth,monthOfBirth,entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), ".", initialLName,dayOfBirth,monthOfBirth,entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), "_", initialLName,dayOfBirth,monthOfBirth,entity.getYearOfBirth()));
			
				
	
		}
		
		if (!StringUtils.isEmpty(entity.getFirstName()))
		{	
			String lastName = !StringUtils.isEmpty(entity.getLastName()) ? entity.getLastName()
					: "";
			phrAddress.add(populatePHRAddress(entity.getFirstName(), lastName, entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), dayOfBirth, monthOfBirth));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), dayOfBirth, monthOfBirth,
					entity.getYearOfBirth()));
			phrAddress.add(populatePHRAddress(entity.getFirstName(), lastName, dayOfBirth,
					monthOfBirth, entity.getYearOfBirth()));
		
		}
		
		if (!StringUtils.isEmpty(entity.getLastName())) {
			phrAddress.add(populatePHRAddress(entity.getLastName(), dayOfBirth, monthOfBirth));
			phrAddress.add(populatePHRAddress(entity.getLastName(), dayOfBirth));
			phrAddress.add(populatePHRAddress(entity.getLastName(), monthOfBirth));
			phrAddress.add(populatePHRAddress(entity.getLastName(), entity.getYearOfBirth()));			
			
		}
		
		if (!StringUtils.isEmpty(entity.getEmail()) && entity.getEmail().contains("@")) {
			phrAddress.add(populatePHRAddress(entity.getEmail().substring(0, entity.getEmail().indexOf('@'))));
		}
				

		Function replace =  (value) -> ((String) value).replace(" ","");
		return (Set<String>) phrAddress.stream().map(replace).collect(Collectors.toSet());

	}

	public String populateDOB(String day, String month, String year) {
		month = StringUtils.isEmpty(month) ? null : String.format("%02d", Integer.parseInt(month));
		day = StringUtils.isEmpty(day) ? null : String.format("%02d", Integer.parseInt(day));

		if (Objects.isNull(month) || Objects.isNull(day)) {
			return year;
		}
		return year.concat("-").concat(month).concat("-").concat(day);
	}

	/**
	 * @param healthIdNumber
	 * @Descrition: This function is used for adding dash in health id number
	 * @return
	 */
	public static String actualHealthIdNumber(String healthIdNumber) {

		if (!StringUtils.isEmpty(healthIdNumber)) {
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
		}
		return healthIdNumber;
	}

	/**
	 * Get DOB or YOB string based on data passed. YOB is always required.
	 * 
	 * @param day   day of birth (optional)
	 * @param month month of birth (optional)
	 * @param year
	 * @return String in format of "DD/MM/YYYY" or "YYYY"
	 */
	public String getDobYobAsString(String day, String month, String year) {
		StringBuilder result = new StringBuilder();
		if (StringUtils.isNoneBlank(day) && StringUtils.isNoneBlank(month)) {
			result.append(day + "-" + month + "-");
		}
		result.append(year);
		return result.toString();
	}

	/**
	 * Get the Mediatype
	 * 
	 * @param mediaContent
	 * @return MediaType
	 */
	public HttpHeaders getHeaders(String mediaContent) {
		HttpHeaders headers = new HttpHeaders();
		if (mediaContent.equalsIgnoreCase("PDF")) {
			headers.add("Content-Disposition", "inline; filename=AbhaIdCard.pdf");
			headers.add("Content-Type", MediaType.APPLICATION_PDF_VALUE);
		} else if (mediaContent.equalsIgnoreCase("PNG")) {
			headers.add("Content-Disposition", "inline; filename=AbhaIdCard.png");
			headers.add("Content-Type", MediaType.IMAGE_PNG_VALUE);
		} else if (mediaContent.equalsIgnoreCase("SVG")) {
			headers.add("Content-Disposition", "inline; filename=AbhaIdCard.svg");
			headers.add("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
		} else {
			throw new InvalidMediaType();
		}
		return headers;

	}

}
