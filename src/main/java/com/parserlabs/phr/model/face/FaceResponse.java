package com.parserlabs.phr.model.face;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class FaceResponse {

	private boolean faceFound;
	private int noOfFaces;
	
}
