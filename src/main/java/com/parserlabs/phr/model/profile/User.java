package com.parserlabs.phr.model.profile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.parserlabs.phr.enums.LoginMethodsEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class User {

	@ApiModelProperty(example = "user@abdm", dataType = "String", name = "phrAddress", value = "Phr address/Id of the User")
	private String phrAddress;

	@ApiModelProperty(name = "healthIdNumber", value = "HealthcareID Number.", example = "20-0878-1213-5055")
	private String healthIdNumber;

	@ApiModelProperty(name = "fullName", value = "Full name of the user", example = "Ayushman Bharat Mission")
	private String fullName;
	@ApiModelProperty(name = "firstName", value = "First name of the user", example = "Ayushman")
	private String firstName;

	@ApiModelProperty(name = "lastName", value = "Last name of the user ", example = "Mission")
	private String lastName;

	@ApiModelProperty(name = "middleName", value = "Middle name of the user ", example = "Bharat")
	private String middleName;

	@ApiModelProperty(name = "gender", value = "Gender", allowableValues = "M,F,O", example = "M")
	private String gender;

	@ApiModelProperty(name = "email", value = "Email address of the user", example = "user@nha.com", required = false)
	private String email;
	@ApiModelProperty(name = "emailVerified", dataType = "Boolean", value = "Email Address is verified or not", example = "true")
	private boolean emailVerified;

	@ApiModelProperty(dataType = "String", name = "value", value = "Mobile Number/Email Address(Encrypted with public key).", example = "9988776655")
	private String mobile;
	@ApiModelProperty(name = "mobileVerified", value = "Mobile Number is verified or not", example = "true")
	private boolean mobileVerified;

	@ApiModelProperty(name = "countryCode", value = "Country code of the user", example = "+91 ")
	private String countryCode;

	@ApiModelProperty(name = "dayOfBirth", value = "Day of birth of the user.", example = "15")
	private String dayOfBirth;
	@ApiModelProperty(name = "monthOfBirth", value = "Month of birth of the user.", example = "8")
	private String monthOfBirth;
	@ApiModelProperty(name = "yearOfBirth", value = "Year of birth of the user.", example = "2021")
	private String yearOfBirth;
	@ApiModelProperty(name = "datefBirth", value = "Date of birth of the user.", example = "15/08/2021")
	private String dateOfBirth;

	@ApiModelProperty(name = "kycStatus", value = "Kyc status of the user", example = "VERIFIED")
	private String kycStatus;

	private String kycDocumentType;

	@ApiModelProperty(name = "profilePhoto", value = "Profile photo of the user.", example = "<BASE64 ENCODED STRING>")
	private String profilePhoto;

	@ApiModelProperty(name = "address", value = "Complete address of the user ", example = "")
	private Address address;
	
	@ApiModelProperty(example = "[PASSWORD, MOBILE_OTP, EMAIL_OTP, AADHAAR_OTP]", dataType = "String", name = "authMethods", required = true, value = "Based on Login Authentication Methods", allowableValues = "MOBILE_OTP,EMAIL_OTP,PASSWORD, AADHAAR_OTP")
	@Builder.Default
	private Set<LoginMethodsEnum> authMode = new HashSet<LoginMethodsEnum>();
	
	private List<AbhaAddress> abhaAddresses=new ArrayList<>();
}
