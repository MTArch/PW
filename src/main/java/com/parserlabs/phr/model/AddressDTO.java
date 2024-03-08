package com.parserlabs.phr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public class AddressDTO {

	private String countryName;
	private String countryCode;
	private String stateName;
	private String stateCode;
	private String districtName;
	private String districtCode;
	private String subDistrictName;
	private String subDistrictCode;
	private String townName;
	private String townCode;
	private String villageName;
	private String villageCode;
	private String wardName;
	private String wardCode;
	private String addressType;
	private String addressLine;
	private String pincode;
}
