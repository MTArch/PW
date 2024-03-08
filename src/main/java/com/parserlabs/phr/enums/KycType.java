package com.parserlabs.phr.enums;

import java.util.Arrays;
import java.util.Optional;

public enum KycType {
	MOBILE("MOBILE"), AADHAAR("AADHAAR"), DRIVING_LICENCE("DRIVING_LICENCE"), PAN_CARD("PAN_CARD");

	private String code;

	private KycType(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

	public static String kycType(String code) {
		Optional<KycType> accuntTypeOptional = Arrays.asList(KycType.values()).stream().filter(ac -> ac.code == code)
				.findFirst();
		return accuntTypeOptional.isPresent() ? accuntTypeOptional.get().name() : KycType.AADHAAR.name();

	}

}
