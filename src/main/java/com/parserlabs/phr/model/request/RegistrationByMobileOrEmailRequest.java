package com.parserlabs.phr.model.request;

import com.parserlabs.phr.annotation.DayOfBirth;
import com.parserlabs.phr.annotation.Email;
import com.parserlabs.phr.annotation.Encryption;
import com.parserlabs.phr.annotation.Gender;
import com.parserlabs.phr.annotation.Mobile;
import com.parserlabs.phr.annotation.MonthOfBirth;
import com.parserlabs.phr.annotation.NotBlank;
import com.parserlabs.phr.annotation.PinCode;
import com.parserlabs.phr.annotation.StateCode;
import com.parserlabs.phr.annotation.Uuid;
import com.parserlabs.phr.annotation.ValidSubName;
import com.parserlabs.phr.annotation.YearOfBrith;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RegistrationByMobileOrEmailRequest {

	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83", dataType = "String", name = "transactionId", value = "Based on UUID", required = true)
	@Uuid
	private String transactionId;

	@ApiModelProperty(name = "email", value = "Email address of the user", example = "6BE4FfCtkOrDZU/Fc545GrhVGgC8JxiK8uU/DSQT7PNebX2Bigz0lUuC3R0F4RA+PoulQCfqqK4OKDe1vi0oAgiSbvAQl4DoCnd4ANmb5k+5YevhouiHCiBCp1zUZZLJ6A28Ux7NBWioNxFuWUYh2syF+nOoQ0kyQ1bUUMYg8tFtm0SLj0MRTowh3fksCFsbYBj6sgx7Hd+5M8xlUDhd/v5mHh4zoTodQNRc5L/2nlT/eBSQPof1iIgkMsgYZjLllS0v1LPSdeGsnsreEVtqkWn/tPyLwcsqkcMxDKrhHbI3odlIkYngBLGqLuBWLVE52pihdCbGrVwpG4S0bpQ0qg==", required = false)
	@Email(optional = true)
	@Encryption(required = false)
	private String email;

	@ApiModelProperty(name = "countryCode", value = "Country of the user", example = "+91 ", required = false)
	private String countryCode;

	@ApiModelProperty(name = "firstName", value = "First name of the user", example = "Ayushman", required = true)
	@NotBlank
	@Getter
	@ValidSubName
	private String firstName;

	@ApiModelProperty(name = "middleName", value = "Middle name of the user ", example = "Bharat")
	@Getter
	@ValidSubName
	String middleName;

	@ApiModelProperty(name = "lastName", value = "Last name of the user ", example = "Mission")
	@Getter
	@ValidSubName
	private String lastName;

	@ApiModelProperty(name = "dayOfBirth", value = "Day of birth of the user.", example = "15", required = false)
	@DayOfBirth
	private String dayOfBirth;

	@ApiModelProperty(name = "monthOfBirth", value = "Month of birth of the user.", example = "8", required = false)
	@MonthOfBirth
	private String monthOfBirth;

	@ApiModelProperty(name = "yearOfBirth", value = "Year of birth of the user.", example = "2018", required = true)
	@NotBlank
	@YearOfBrith
	private String yearOfBirth;

	@ApiModelProperty(name = "gender", value = "Gender", allowableValues = "M,F,O", example = "M", required = true)
	@Gender
	private String gender;

	@ApiModelProperty(name = "mobile", value = "Mobile number of the user.", required = true, example = "yJ2hY5bc2g3P2pQyca/ER6VYQ8TGMj/VN42h9xkh/3jAwJQtZEspnhrtEKqwFXt1+8budi64CPlUEzbkwUsCotIOMm8idfSX+SQyb8VlqLxxIkAzGvmXjWrbQUNEUWnnJjzkIjweNmj8GJ2u0uRdrAGpBc1vMoMz5XD2SGfFttvmziTtucq5w2dOoAPOni4Bl7sfii3Qyo8Szl1/tXNnZbDZi8HH9Cpajno4pFiu6mQDVTkkyDHTqyo7Bv3IFpdNYiRDAZ1yh1cBOfufMy1gSZQetCwETFxdsOgw7JvKL/gEN+RAFKZF2oUriCsAkYYbxW1cfrqa/YRXUw0ho+n4Jw==")
	@Mobile(optional = true)
	@Encryption(required = false)	
	private String mobile;

	@ApiModelProperty(name = "stateCode", value = "State Code of the user (LGD).", example = "7", required = true)
	@StateCode(required = false)
	private String stateCode;

	@ApiModelProperty(name = "districtCode", value = "District Code of the user (LGD).", example = "77")
	private String districtCode;

	@ApiModelProperty(name = "address", value = "Address of the user.", example = "9th Floor, Tower-l, Jeevan Bharati Building, Connaught Place, New Delhi - 110001")
	private String address;

	@ApiModelProperty(name = "pincode", value = "Area Pincode of the user.", example = "110001")
	@PinCode(required = false)
	private String pinCode;

}
