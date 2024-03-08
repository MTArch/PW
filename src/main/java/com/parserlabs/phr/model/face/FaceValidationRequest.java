package com.parserlabs.phr.model.face;

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
public class FaceValidationRequest {

	@ApiModelProperty(name = "profilePhoto", value = "Profile photo of the user.", example = "<BASE64 ENCODED STRING>")
	private String profilePhoto;
	
}
