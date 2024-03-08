/**
 * 
 */
package com.parserlabs.phr.model.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.DayOfBirth;
import com.parserlabs.phr.annotation.Gender;
import com.parserlabs.phr.annotation.Mobile;
import com.parserlabs.phr.annotation.MonthOfBirth;
import com.parserlabs.phr.annotation.NotBlank;
import com.parserlabs.phr.annotation.PinCode;
import com.parserlabs.phr.annotation.StateCode;
import com.parserlabs.phr.annotation.ValidSubName;
import com.parserlabs.phr.annotation.YearOfBrith;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author ameynaik
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePHRFromMobileRequest {

	@ApiModelProperty(example = "deepakndhm",dataType="String",name="abhaAddress")
	private String abhaAddress;

	@ApiModelProperty(example = "43-4221-5105-6749",dataType="String",name="abhaNumber")
	private String abhaNumber;

	@ApiModelProperty(name = "To Notify User", value = "True", example = "true|false")
	private boolean notify;
	
	@ApiModelProperty(name = "password", dataType = "String", value = "User Password(Encrypted with public key).", example = "HNceo964MVndrs8Z2oMtzIsmmbzagveHbWkDsDKskTue+/YZhHHrMon19J03ggU457upzWMIX0nU3d38xjB3FxA2qWCVmvLZ98A9l0y3i33vq1ywu9cORGF4OEqV8l7H9h4tDnLGDHnbOh9ct85VfOohP4p73lqW6WQSMYcU+xkBfEsRj42pWL19EVsE1UULtQE8gYY1B0SeM63svUp1kQ4Pt5hdgKxibYBq+hRcck2PkEIhp2N7AkjH4Tf+AhXU9956WLwjKgAKMk7K4+Zv8JtxYCcblQitbpN4ImPH5edf4mO5R/L9RpdAVSllAQQfPIDlp5ZGOZ1GrSmhzOSP3g==")
	private String password;
	
	@ApiModelProperty(name = "email", value = "Email address of the user", example = "xyz@gmail.com", required = false)
	private String email;

	@ApiModelProperty(name = "mobileVerified", value = "mobileVerified", example = "true|false")
	private boolean mobileVerified;
	
	@ApiModelProperty(name = "mobileVerified", value = "mobileVerified", example = "true|false")
	private boolean emailVerified;
	
	
	@ApiModelProperty(name = "countryCode", value = "Country of the user", example = "+91 ", required = false)
	private String countryCode;

	@ApiModelProperty(name = "firstName", value = "First name of the user", example = "Ayushman", required = true)
	@NotBlank
	@Getter
	@ValidSubName
	private String firstName;

	@ApiModelProperty(name = "fullName", value = "full name of the user", example = "Ayushman Khurana", required = true)
	private String fullName;
	
	@ApiModelProperty(name = "middleName", value = "Middle name of the user ", example = "Bharat")
	@Getter
	@ValidSubName
	String middleName;

	@ApiModelProperty(name = "lastName", value = "Last name of the user ", example = "Mission")
	@Getter
	@ValidSubName
	private String lastName;

	@ApiModelProperty(name = "dayOfBirth", value = "Day of birth of the user.", example = "15", required = true)
	@NotBlank
	@DayOfBirth
	private String dayOfBirth;

	@ApiModelProperty(name = "monthOfBirth", value = "Month of birth of the user.", example = "8", required = true)
	@NotBlank
	@MonthOfBirth
	private String monthOfBirth;

	@ApiModelProperty(name = "yearOfBirth", value = "Year of birth of the user.", example = "2018", required = true)
	@NotBlank
	@YearOfBrith
	private String yearOfBirth;

	@ApiModelProperty(name = "gender", value = "Gender", allowableValues = "M,F,O", example = "M", required = true)
	@Gender
	private String gender;

	@ApiModelProperty(name = "mobile", value = "Mobile number of the user.", required = true, example = "9405925999")
	@Mobile(optional = true)
	private String mobile;

	@ApiModelProperty(name = "stateCode", value = "State Code of the user (LGD).", example = "7", required = true)
	@StateCode(required = true)
	private String stateCode;

	@ApiModelProperty(name = "districtCode", value = "District Code of the user (LGD).", example = "77")
	private String districtCode;

	@ApiModelProperty(name = "address", value = "Address of the user.", example = "9th Floor, Tower-l, Jeevan Bharati Building, Connaught Place, New Delhi - 110001")
	private String addressLine;

	@ApiModelProperty(name = "pincode", value = "Area Pincode of the user.", example = "110001")
	@PinCode(required = true)
	private String pinCode;

	@ApiModelProperty(name = "status", value = "ACTIVE|DEACTIVE", example = "ACTIVE")
	private String status;

	@ApiModelProperty(name = "kycStatus", value = "VERIFIED|PENDING", example = "VERIFIED")
	private String kycStatus;

	@ApiModelProperty(example = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkJCQkJCQoLCwoODw0PDhQSERESFB4WFxYXFh4uHSEdHSEdLikxKCUoMSlJOTMzOUlUR0NHVGZbW2aBeoGoqOIBCQkJCQkJCgsLCg4PDQ8OFBIRERIUHhYXFhcWHi4dIR0dIR0uKTEoJSgxKUk5MzM5SVRHQ0dUZltbZoF6gaio4v/CABEIBLAHgAMBIgACEQEDEQH/xAAbAAACAwEBAQAAAAAAAAAAAAACAwABBAUGB//aAAgBAQAAAADwawLpMspcK7qrlE5F0Vtul2bVywMUNeBHUkW/bmxvYELGuNjh2VDvixxo5ViljKjDRMoahCULjs2JCShjhjh2OGxo0Y2MoXHOLszsKLhw7tD99mpZQxj8xceofmLEKFwXLTIyHwY1Ls+iEotjHY0M0pjRYxtGj4VFKLPohQlFQyy4Qipc0XG9pS+CP/2Q==",
			dataType="String",name="profilePhoto",value="Encoded with Base 64")
	private String profilePhoto;
	
	@ApiModelProperty(example = "[PASSWORD, MOBILE_OTP, EMAIL_OTP]",name="authMethods",value="{PASSWORD, MOBILE_OTP, EMAIL_OTP}")
	private List<String> authMethods;

	@ApiModelProperty(hidden=true) 
	@Builder.Default
	private boolean updateFlag = false;
	
    private String createdBy;
	
	private String updatedBy;
	
}
