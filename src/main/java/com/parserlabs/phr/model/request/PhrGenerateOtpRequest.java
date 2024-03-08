package com.parserlabs.phr.model.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.LoginAuthMethod;
import com.parserlabs.phr.enums.LoginMethodsEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@ApiModel
public class PhrGenerateOtpRequest implements Serializable {

	private static final long serialVersionUID = 770251693656640003L;
	
	@ApiModelProperty(example = "PASSWORD", dataType = "String", name = "authMethods", required = true, value = "Based on Login Authentication Methods", allowableValues = "MOBILE_OTP,EMAIL_OTP,PASSWORD, AADHAAR_OTP")
	@LoginAuthMethod
	private LoginMethodsEnum authMethod;

}
