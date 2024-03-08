package com.parserlabs.phr.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.parserlabs.phr.enums.LoginMethodsEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiIgnore
public class UserDTO {

	private String phrAddress;
	private String healthIdNumber;
	private String fullName;
	private String firstName;
	private String lastName;
	private String middleName;
	private String gender;
	private String email;
	private boolean emailVerified;
	private String mobile;
	private boolean mobileVerified;
	private String countryCode;
	private String dayOfBirth;
	private String monthOfBirth;
	private String yearOfBirth;
	private String dateOfBirth;
	private String kycStatus;
	private String kycDocumentType;
	private String profilePhoto;
	private String status;
	private String updatedBy;
	private String token;
	private AddressDTO address;
	private String password;
	@Builder.Default
	private Set<LoginMethodsEnum> authMode = new HashSet<LoginMethodsEnum>();
	private String authTransactionId;
}
