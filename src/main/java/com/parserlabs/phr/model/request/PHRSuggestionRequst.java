package com.parserlabs.phr.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.Uuid;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PHRSuggestionRequst {

	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83", dataType = "String", name = "transactionId", value = "Based on UUID", required = true)
	@Uuid
	private String transactionId;
	
}
