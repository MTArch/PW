package com.parserlabs.phr.model.request;

import com.parserlabs.phr.annotation.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateForgetPassOtpRequest {

	
	@NotBlank
	@ApiModelProperty(dataType = "String", name = "phrAddress", value = "PHR Address", example = "user@abdm", required = true)
	private String abhaAddress;
	
	@ApiModelProperty(example = "MOBILE_OTP",name="authMode",value="{MOBILE_OTP, EMAIL_OTP}",required = true)
	@NotBlank
	private String authMode;
}
