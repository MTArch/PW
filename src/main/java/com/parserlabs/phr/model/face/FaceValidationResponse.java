package com.parserlabs.phr.model.face;

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
public class FaceValidationResponse {

	@ApiModelProperty(example = "true",dataType="boolean",name="faceFound",value="bool")
	private boolean faceFound;
	

	@ApiModelProperty(example = "1",dataType="Integer",name="noOfFaces",value="Integer")
	private int noOfFaces;
	
}
