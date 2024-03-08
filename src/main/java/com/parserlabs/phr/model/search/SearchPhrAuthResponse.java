package com.parserlabs.phr.model.search;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.enums.LoginMethodsEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rajesh
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@ApiModel
public class SearchPhrAuthResponse {

	@ApiModelProperty(example = "user@abdm", dataType = "String", name = "phrAddress", value = "Phr Address of the user", required = true)
	private String phrAddress;

	@ApiModelProperty(example = "[PASSWORD, MOBILE_OTP, EMAIL_OTP]", dataType = "java.util.Set", name = "authMethods", value = "Based on Login Methods", allowableValues = "AADHAAR_OTP,MOBILE_OTP,PASSWORD,EMAIL_OTP")
	@Builder.Default
	private Set<LoginMethodsEnum> authMethods = new HashSet<LoginMethodsEnum>();
	
	@ApiModelProperty(example = "kishan kumar singh",dataType="String",name="name")
	private String name;
	
	@ApiModelProperty(example = "Active",dataType="String",name="status")
	private String status;
	
	@ApiModelProperty(example = "43-4221-5105-6749",dataType="String",name="healthIdNumber")
	private String healthIdNumber;
}
