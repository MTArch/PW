package com.parserlabs.phr.model.request;

import com.parserlabs.phr.model.AddressDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {

	private String firstName;
	private String lastName;
	private String middleName;
	private String gender;
	private String email;
	private String mobile;
	private String countryCode;
	private Integer dayOfBirth;
	private Integer monthOfBirth;
	private Integer yearOfBirth;
	private byte[] profilePhoto;
	private AddressDTO address;

}
