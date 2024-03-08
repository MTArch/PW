package com.parserlabs.phr.exception.model;

import java.util.List;

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
public class ApiError {

	@ApiModelProperty(example = "MIS-422",dataType="String",name="code",value="Error Code",required = true)
	private String code;
	
	@ApiModelProperty(example = "Unable to process the current request due to some wrong data entered",dataType="String",name="code",value="Error Code",required = true)
	private String message;
	
	@ApiModelProperty(example = "[{\"attribute\": {\"key\": \"email\",\"value\": \"email is invalid\"},\"code\": \"MIS-1010\",\"message\": \"Invalid Data\"}]",dataType="java.util.List<ErrorDetails>",name="details",value="Dril down error details of the API",required = true)
	private List<ErrorDetails> details;
}
