package com.parserlabs.phr.model.response;

import java.io.Serializable;
import java.util.Set;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionWithPHRResponse implements Serializable {

	private static final long serialVersionUID = -6702371726535773498L;

	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83", dataType = "String", name = "transactionId", value = "Based on UUID", required = true)
	private String transactionId;
	
	@ApiModelProperty(example = "[user@abdm, user2@abdm]", dataType = "java.util.Set", name = "mappedPhrAddress", value = "List of the Phr Address mapped with the Mobile number", required = true)
	private Set<String> mappedPhrAddress;

}