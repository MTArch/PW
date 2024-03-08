package com.parserlabs.phr.model.request;

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
public class SearchByHealthIdNumberRequest implements Serializable {

	private static final long serialVersionUID = -5317398175560570638L;
	
	@ApiModelProperty(example = "11-1111-1111-1111",dataType="String",name="healthIdNumber",required = true)
	private String healthIdNumber;
	
	@ApiModelProperty(example = "1994",dataType="String",name="yearOfBirth")
	private String yearOfBirth;
	
}
