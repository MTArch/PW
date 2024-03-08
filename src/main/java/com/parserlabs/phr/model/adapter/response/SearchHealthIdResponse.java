package com.parserlabs.phr.model.adapter.response;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchHealthIdResponse  {

	@ApiModelProperty(example = "deepakndhm",dataType="String",name="healthId")
	private String healthId;
	
	@ApiModelProperty(example = "43-4221-5105-6749",dataType="String",name="healthIdNumber")
	private String healthIdNumber;
	private Map<String, String> tags;
	private Set<String> blockedAuthMethods;
	private String status;
	private String verificationStatus;

}