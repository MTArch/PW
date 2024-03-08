package com.parserlabs.phr.model.request;

import com.parserlabs.phr.annotation.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdatePhrAttributePayLoad {

	@ApiModelProperty(example = "xyz@{abdm,sbx}",dataType="String",name="phrAddress",required = true)
	@NotBlank
	private String phrAddress;
	
	@ApiModelProperty(example = "status",dataType="String",name="key",required = true)
	@NotBlank
	private String key;

	@ApiModelProperty(example = "PENDING",dataType="String",name="value",required = true)
	@NotBlank
	private String value;
	

	
}
