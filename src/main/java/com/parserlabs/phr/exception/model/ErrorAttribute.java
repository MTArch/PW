package com.parserlabs.phr.exception.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class ErrorAttribute {

	@ApiModelProperty(example = "email",dataType="String",name="key",value="Attribute of the request that data is not valid",required = true)
	private String key;
	
	@ApiModelProperty(example = "email is invalid",dataType="String",name="value",value="Details of the error",required = true)
	private String value;
}
