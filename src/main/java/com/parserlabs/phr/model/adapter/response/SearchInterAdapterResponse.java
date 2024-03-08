package com.parserlabs.phr.model.adapter.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchInterAdapterResponse {
	
	private String healthIdNumber;
	private Set<String> authMethods;
	private Set<String> blockedAuthMethods;
	private String status;

}
