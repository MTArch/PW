package com.parserlabs.phr.proxy.model;

import com.parserlabs.phr.annotation.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PhrLinkedOrDeLinkedRequestPayLoad {
	
	@NotBlank
	private String phrAddress;
	
	private boolean preferred;

}
