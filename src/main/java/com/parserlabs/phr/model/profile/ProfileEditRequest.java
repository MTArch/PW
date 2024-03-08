package com.parserlabs.phr.model.profile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.parserlabs.phr.annotation.DayOfBirth;
import com.parserlabs.phr.annotation.Gender;
import com.parserlabs.phr.annotation.ImageSizeFaceDetectors;
import com.parserlabs.phr.annotation.MonthOfBirth;
import com.parserlabs.phr.annotation.PinCode;
import com.parserlabs.phr.annotation.StateCode;
import com.parserlabs.phr.annotation.ValidName;
import com.parserlabs.phr.annotation.ValidSubName;
import com.parserlabs.phr.annotation.YearOfBrith;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Rajesh
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel

public class ProfileEditRequest {

	@ApiModelProperty(name = "firstName", value = "First name of the user", example = "Ayushman")
	@Getter
	@ValidName
	private String firstName;

	@ApiModelProperty(name = "lastName", value = "Last name of the user ", example = "Mission")
	@Getter
	@ValidSubName
	private String lastName;

	@ApiModelProperty(name = "middleName", value = "Middle name of the user ", example = "Bharat")
	@Getter
	@ValidSubName
	private String middleName;

	@ApiModelProperty(name = "gender", value = "Gender", allowableValues = "M,F,O", example = "M")
	@Gender(required = false)
	private String gender;

	@ApiModelProperty(name = "dayOfBirth", value = "Day of birth of the user.", example = "15")
	@DayOfBirth
	private String dayOfBirth;
	@ApiModelProperty(name = "monthOfBirth", value = "Month of birth of the user.", example = "8")
	@MonthOfBirth
	private String monthOfBirth;
	@ApiModelProperty(name = "yearOfBirth", value = "Year of birth of the user.", example = "2021")
	@YearOfBrith
	private String yearOfBirth;

	@ImageSizeFaceDetectors(required = false, requiredFaceDetect = false)
	@ApiModelProperty(name = "profilePhoto", value = "Profile photo of the user.", example = "<BASE-64 FORMAT ENCODED STRING>")
	private byte[] profilePhoto;

	@ApiModelProperty(name = "stateCode", value = "State Code of the user (LGD).", example = "7")
	@StateCode(required = false)
	private String stateCode;

	@ApiModelProperty(name = "districtCode", value = "District Code of the user (LGD).", example = "71")
	private String districtCode;

	@ApiModelProperty(name = "addressLine", value = "Address of the user.", example = "9th Floor, Tower-l, Jeevan Bharati Building, Connaught Place, New Delhi - 110001")
	private String addressLine;
	
	@ApiModelProperty(name = "pincode", value = "Area Pincode of the user.", example = "110001")
	@PinCode(required = false)
	private String pinCode;

}
