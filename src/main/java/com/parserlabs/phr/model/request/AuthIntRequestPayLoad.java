package com.parserlabs.phr.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthIntRequestPayLoad {

	@ApiModelProperty(example = "AADHAAR_OTP or MOBILE_OTP",dataType="String",name="authMethod",required = true)
	@NotBlank
	private String authMethod;
	@ApiModelProperty(example = "11-1111-1111-1111",dataType="String",name="healhtIdNumber",required = true)
	@NotBlank
	private String healhtIdNumber;

}
