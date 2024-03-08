package com.parserlabs.phr.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShareCMRequestPlayLoad {
	
	private String healthId;
	private String healthIdNumber;
	private String fullName;
	private String firstName;
	private String lastName;
	private String middleName;
	private String gender;
	private String email;
	private String mobile;
	private String countryCode;
	private String dayOfBirth;
	private String monthOfBirth;
	private String yearOfBirth;
	private String dateOfBirth;
	private String status;	
	private String stateCode;
	private String districtCode;
	private String pincode;
	private String address;
	private String stateName;	
	private String districtName;


	

}
