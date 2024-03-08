package com.parserlabs.phr.model.response;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponsePayLoad {
	
	private String healthIdNumber;
	private Set<String> authMethods;
	private Set<String> blockedAuthMethods;
	private String status;
	
	

}
