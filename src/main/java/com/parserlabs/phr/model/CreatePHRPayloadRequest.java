package com.parserlabs.phr.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePHRPayloadRequest {

	private String abhaAddress;

	private String abhaNumber;

	private boolean notify;

	private String password;

	private String email;

	private boolean mobileVerified;

	private boolean emailVerified;

	private String countryCode;

	private String firstName;

	private String fullName;

	String middleName;

	private String lastName;

	private String dayOfBirth;

	private String monthOfBirth;

	private String yearOfBirth;

	private String gender;

	private String mobile;

	private String stateCode;

	private String districtCode;

	private String addressLine;

	private String pinCode;

	private String status;

	private String kycStatus;

	private String profilePhoto;

	private List<String> authMethods;
	
	private boolean updateFlag = false;

	}
