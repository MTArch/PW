/**
 * 
 */
package com.parserlabs.phr.model.login.mobileemail;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rajesh
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@ApiModel
public class LoginPreVerificationResponse {
	
	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83", dataType = "String", name = "transactionId", value = "Based on UUID", required = true)
	private String transactionId;
	
	@ApiModelProperty(name = "mobileEmail", value = "Email/Mobile number registered with PHR ADDRESS.", required = true, example = "9988776655")
	private String mobileEmail;
	
	@ApiModelProperty(example = "[user@abdm, user2@abdm]", dataType = "java.util.Set", name = "mappedPhrAddress", value = "List of the Phr Address mapped with the Mobile number", required = true)
	private Set<String> mappedPhrAddress;
	

}
