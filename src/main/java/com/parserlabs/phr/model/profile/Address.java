package com.parserlabs.phr.model.profile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public class Address {

	@ApiModelProperty(name = "countryName", value = "Country Name of the user", example = "India")
	private String countryName;
	@ApiModelProperty(name = "countryCode", value = "Country code of the user", example = "+91 ")
	private String countryCode;
	
	@ApiModelProperty(name = "stateName", value = "State Name of the user (LGD).", example = "Delhi")
	private String stateName;
	@ApiModelProperty(name = "stateCode", value = "State Code of the user (LGD).", example = "7")
	private String stateCode;
	
	@ApiModelProperty(name = "districtName", value = "District Name of the user (LGD).", example = "Central")
	private String districtName;
	@ApiModelProperty(name = "districtCode", value = "District Code of the user (LGD).", example = "71")
	private String districtCode;
	
	@ApiModelProperty(name = "subDistrictName", value = "Sub District Name of the user (LGD).", example = "")
	private String subDistrictName;

	@ApiModelProperty(name = "subDistrictCode", value = "Sub District Code of the user (LGD).", example = " ")
	private String subDistrictCode;
	
	@ApiModelProperty(name = "townName", value = "town Name of the user (LGD).", example = " ")
	private String townName;
	
	@ApiModelProperty(name = "townCode", value = "town Code of the user (LGD).", example = " ")
	private String townCode;
	
	@ApiModelProperty(name = "villageName", value = "village Name of the user (LGD).", example = " ")
	private String villageName;
	@ApiModelProperty(name = "villageCode", value = "village Code of the user (LGD).", example = " ")
	private String villageCode;
	
	@ApiModelProperty(name = "wardName", value = "Ward Name of the user (LGD).", example = " ")
	private String wardName;
	@ApiModelProperty(name = "wardCode", value = "Ward Code of the user (LGD).", example = " ")
	private String wardCode;
	
	
	@ApiModelProperty(name = "addressLine", value = "Address of the user.", example = "9th Floor, Tower-l, Jeevan Bharati Building, Connaught Place, New Delhi - 110001")
	private String addressLine;

	@ApiModelProperty(name = "pincode", value = "pincode ", example = "110001")	
	private String pincode;
}
