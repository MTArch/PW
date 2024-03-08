package com.parserlabs.phr.proxy.model;

import com.parserlabs.phr.annotation.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PhrDeLinkedRequestPayLoad {
	
	
	@NotBlank
	private String phrAddress;

	@NotBlank
	private String healthIdNumber;


}
