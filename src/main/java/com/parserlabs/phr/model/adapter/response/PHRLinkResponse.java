package com.parserlabs.phr.model.adapter.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PHRLinkResponse {

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

	@ApiModelProperty(example = "b-14 someshwar nagar", dataType = "String", name = "address")
	private String address;

	@ApiModelProperty(example = "MAHARASHTRA", dataType = "String", name = "stateName")
	private String stateName;

	@ApiModelProperty(example = "Pune", dataType = "String", name = "districtName")
	private String districtName;
	
	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83", dataType = "String", name = "transactionId", value = "Based on UUID", required = true)
	private String transactionId;
	

}