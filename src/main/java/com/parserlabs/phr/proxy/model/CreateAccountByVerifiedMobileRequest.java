package com.parserlabs.phr.proxy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class CreateAccountByVerifiedMobileRequest {

	private String txnId;
	private String token;
	private String name;
	private String gender;
	private String yearOfBirth;
	private String monthOfBirth;
	private String dayOfBirth;
	private String firstName;
	private String lastName;
	private String middleName;
	private String healthId;
	private String password;
	private String profilePhoto;
	private String stateCode;
	private String districtCode;
	private String address;

}