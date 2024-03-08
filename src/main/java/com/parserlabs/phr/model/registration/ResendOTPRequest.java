package com.parserlabs.phr.model.registration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.NotBlank;
import com.parserlabs.phr.annotation.Uuid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
@ToString
public class ResendOTPRequest {

	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83",dataType="String",name="transactionId",value="Based on UUID",required = true)
	@Uuid
	@NotBlank
	private String transactionId;

}