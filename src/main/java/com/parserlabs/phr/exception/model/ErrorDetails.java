package com.parserlabs.phr.exception.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class ErrorDetails {

	@ApiModelProperty(example = "email is invalid",dataType="String",name="message",value="Details of the Api error code",required = true)
	private String message;
	@ApiModelProperty(example = "MIS-1010",dataType="String",name="code",value="Error Code",required = true)
	private String code;
	private ErrorAttribute attribute;

}
