package com.parserlabs.phr.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ApiModel
public class SuccessResponse {

	@ApiModelProperty(example = "true",dataType="Boolean",name="success",value="bool",required = true)
	private Boolean success;
}
