package com.parserlabs.phr.model.adapter.response;

import java.util.Map;
import java.util.Set;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parserlabs.phr.enums.AuthMethods;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HidResponse {

	@ApiModelProperty(example = "43-4221-5105-6749", dataType = "String", name = "healthIdNumber")
	private String healthIdNumber;

	@ApiModelProperty(example = "9545812125", dataType = "String", name = "mobile")
	private String mobile;

	@ApiModelProperty(example = "kishan", dataType = "String", name = "firstName")
	private String firstName;

	@ApiModelProperty(example = "kumar", dataType = "String", name = "middleName")
	private String middleName;

	@ApiModelProperty(example = "singh", dataType = "String", name = "lastName")
	private String lastName;

	@ApiModelProperty(example = "kishan kumar singh", dataType = "String", name = "name")
	private String name;

	@ApiModelProperty(example = "1994", dataType = "String", name = "yearOfBirth")
	private String yearOfBirth;

	@ApiModelProperty(example = "31", dataType = "String", name = "dayOfBirth")
	private String dayOfBirth;

	@ApiModelProperty(example = "05", dataType = "String", name = "monthOfBirth")
	private String monthOfBirth;

	@ApiModelProperty(example = "Male", dataType = "String", name = "gender")
	private String gender;

	@ApiModelProperty(example = "example@demo.com", dataType = "String", name = "email")
	private String email;

	@ApiModelProperty(example = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkJCQkJCQoLCwoODw0PDhQSERESFB4WFxYXFh4uHSEdHSEdLikxKCUoMSlJOTMzOUlUR0NHVGZbW2aBeoGoqOIBCQkJCQkJCgsLCg4PDQ8OFBIRERIUHhYXFhcWHi4dIR0dIR0uKTEoJSgxKUk5MzM5SVRHQ0dUZltbZoF6gaio4v/CABEIBLAHgAMBIgACEQEDEQH/xAAbAAACAwEBAQAAAAAAAAAAAAACAwABBAUGB//aAAgBAQAAAADwawLpMspcK7qrlE5F0Vtul2bVywMUNeBHUkW/bmxvYELGuNjh2VDvixxo5ViljKjDRMoahCULjs2JCShjhjh2OGxo0Y2MoXHOLszsKLhw7tD99mpZQxj8xceofmLEKFwXLTIyHwY1Ls+iEotjHY0M0pjRYxtGj4VFKLPohQlFQyy4Qipc0XG9pS+CP/2Q==", dataType = "String", name = "profilePhoto", value = "Encoded with Base 64")
	private String profilePhoto;

	@ApiModelProperty(example = "27", dataType = "String", name = "stateCode")
	private String stateCode;

	@ApiModelProperty(example = "401", dataType = "String", name = "districtCode")
	private String districtCode;

	@ApiModelProperty(example = "213", dataType = "String", name = "subDistrictCode")
	private String subDistrictCode;

	@ApiModelProperty(example = "412", dataType = "String", name = "villageCode")
	private String villageCode;

	@ApiModelProperty(example = "27", dataType = "String", name = "townCode")
	private String townCode;

	@ApiModelProperty(example = "08", dataType = "String", name = "wardCode")
	private String wardCode;

	@ApiModelProperty(example = "412306", dataType = "String", name = "pincode")
	private String pincode;

	@ApiModelProperty(example = "b-14 someshwar nagar", dataType = "String", name = "address")
	private String address;

	@ApiModelProperty(example = "sw1uD+gpv3fj6NHBNhtcII3GksVtkLT9bvcz0svYDyUt/x3jTtedXSYgw4b90GTwfLfs1eow056VsOw9HFS/wB8uH5Ysx+QzpL7PxmAY1WOHwOj04sPKN6Dw8XY8vcXovtvZc1dUB+TPAlGGPNu8iqMVPetukysjRxgbNdLLKMxn46rIRb8NieeyuDx1EHa90jJP9KwKGZdsLr08BysrmMJExzTO9FT93CzoNg50/nxzaQgmkBSbu9D8DxJm7XrLzWSUB05YCknHbokm4iXwyYBsrmfFDE/xCDfzYPhYyhtEmOi4J/GMp+lO+gAHQFQtxkIADhoSR8WXGcAbCUj7uTjFsBU/tc+RtvSotso4FXy8v+Ylzj28jbFTmmOWyAwYi9pThQjXnmRnq43dVdd5OXmxIII6SXs0JzoFvKwSk7VxhuLIRYzKqrkfcnWMrrmRgE8xZ6ZLft6O3IeiHb9WA8b/6/qO8Hdd17FKsSF6te59gSpoajS0FtQIgFn/c+NHzQYo5ZdsuRGM9v+bhHTInI=", dataType = "String", name = "kycPhoto", value = "Encoded with Base 64")
	private String kycPhoto;

	@ApiModelProperty(example = "MAHARASHTRA", dataType = "String", name = "stateName")
	private String stateName;

	@ApiModelProperty(example = "Pune", dataType = "String", name = "districtName")
	private String districtName;

	@ApiModelProperty(example = "Baramati", dataType = "String", name = "subdistrictName")
	private String subdistrictName;

	@ApiModelProperty(example = "Someshwar Nagar", dataType = "String", name = "villageName")
	private String villageName;

	@ApiModelProperty(example = "Baramati", dataType = "String", name = "townName")
	private String townName;

	@ApiModelProperty(example = "Sinhgad fort ward", dataType = "String", name = "wardName")
	private String wardName;

	@ApiModelProperty(example = "true", dataType = "boolean", name = "isNew")
	private boolean isNew;

	@ApiModelProperty(example = "AADHAAR_OTP", dataType = "String", name = "authMethods", value = "Based on authMethods", allowableValues = "AADHAAR_OTP,MOBILE_OTP,PASSWORD,DEMOGRAPHICS,AADHAAR_BIO")
	private Set<AuthMethods> authMethods;

	private Map<String, String> tags;

	@ApiModelProperty(example = "true", dataType = "boolean", name = "kycVerified")
	private boolean kycVerified;

	@ApiModelProperty(example = "Active", dataType = "String", name = "status")
	@JsonIgnore
	private String status;

	@ApiModelProperty(example = "true", dataType = "boolean", name = "isEmailVerified")
	private boolean isEmailVerified;

	@ApiModelProperty(example = "verified", dataType = "String", name = "verificationStatus")
	private String verificationStatus;

	@ApiModelProperty(example = "testing", dataType = "String", name = "verificationType")
	private String verificationType;

	@ApiModelProperty(example = "01111481doG3Y8HY2YRX/937+QzDt58C9Pu7BCMrMWm5M6V8zP8c2oUCDMLSvtELY2XosRNv", dataType = "String", name = "xmlUID",hidden = true)
	@JsonIgnore
	private String xmlUID;

	private Set<String> phrAddress;
	
	
	private int linkedPhrAddess;
	
	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83", dataType = "String", name = "transactionId", value = "Based on UUID", required = true)
	private String transactionId;

	// Marked email to verified or not
	public void populateEmail(String Email_verified, String email) {
		// Email is Verified or not flag
		if (!ObjectUtils.isEmpty(Email_verified)) {
			this.email = Email_verified;
			this.isEmailVerified = true;
		} else {
			this.email = email;
			this.isEmailVerified = false;
		}
	}
	
	

}