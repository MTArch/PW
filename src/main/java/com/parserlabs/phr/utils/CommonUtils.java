package com.parserlabs.phr.utils;

import static com.parserlabs.phr.enums.KycStatus.PENDING;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

import com.parserlabs.phr.enums.KycStatus;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtils {

	private static final String PHR_SEPARATOR = "@";

	public String populateFullName(String... names) {
		return Stream.of(names).filter(StringUtils::hasText).collect(Collectors.joining(" "));
	}

	public String populateDateOfBirth(String... dates) {
		return Stream.of(dates).filter(x -> Objects.nonNull(x)).collect(Collectors.joining("-"));
	}

	public String calculatePhrProvider(String phrAddress) {
		return phrAddress.contains(PHR_SEPARATOR) ? phrAddress.substring(phrAddress.indexOf(PHR_SEPARATOR) + 1) : null;
	}

	public static String stringTrimmer(String str) {
		return StringUtils.hasLength(str) ? str : str.trim();
	}
	
	
	public String calculateKycStatus(String kycTypeString) {
		KycStatus kycStatus = PENDING;

//		try {
//			KycType kycType = KycType.valueOf(kycTypeString);
//			kycStatus = switch (kycType) {
//			case MOBILE -> PENDING;
//			case AADHAAR, DRIVING_LICENCE, PAN_CARD -> VERIFIED;
//			default -> PENDING;
//			};
//		} catch (Exception e) {
//			log.debug("Error occurred while calculating the kyc status, Err Msg {}", e.getMessage());
//		}
		return kycStatus.name();
	}
}
