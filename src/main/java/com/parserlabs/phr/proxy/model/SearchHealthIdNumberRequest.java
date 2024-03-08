package com.parserlabs.phr.proxy.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SearchHealthIdNumberRequest implements Serializable{
	
	private static final long serialVersionUID = 3247033010896164569L;

	@ApiModelProperty(example = "deepakndhm",dataType="String",name="healthId",required = true)
	private String healthId;
	
	@ApiModelProperty(example = "1994",dataType="String",name="yearOfBirth",required = true)
	private String yearOfBirth;
	

}
