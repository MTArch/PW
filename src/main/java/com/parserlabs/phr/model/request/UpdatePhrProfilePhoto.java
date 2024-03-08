package com.parserlabs.phr.model.request;

import com.parserlabs.phr.annotation.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePhrProfilePhoto {

	String phrAddress;
	String healthIdNumber;
	String profilePhoto;
	Boolean profilePhotoCompressed;
	
}
