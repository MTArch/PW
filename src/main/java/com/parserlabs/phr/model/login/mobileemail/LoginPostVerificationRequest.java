package com.parserlabs.phr.model.login.mobileemail;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.NotBlank;
import com.parserlabs.phr.annotation.Uuid;

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
public class LoginPostVerificationRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83", dataType = "String", name = "txnId", value = "Based on UUID", required = true)
	@Uuid
	private String transactionId;

	@ApiModelProperty(dataType = "String", name = "phrAddress", value = "PHR address of the user", required = true, example = "user@abdm")
	@NotBlank
	private String phrAddress;
}
