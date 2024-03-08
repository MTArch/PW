package com.parserlabs.phr.model.adapter.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchAdapterRequest {
	
	private String healthId;

}
