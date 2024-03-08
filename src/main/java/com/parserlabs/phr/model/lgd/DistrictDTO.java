package com.parserlabs.phr.model.lgd;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistrictDTO implements Comparable<DistrictDTO> {

	@ApiModelProperty(example = "490",dataType="String",name="code")
	private String code;
	
	@ApiModelProperty(example = "PUNE",dataType="String",name="name")
	private String name;

	@Override
	public int compareTo(DistrictDTO objectToCompare) {
		return this.getName().compareTo(objectToCompare.getName());
	}

	public static DistrictDTO of(String code, String name) {
		return DistrictDTO.builder().code(code).name(name).build();
	}

	public static boolean isValidKey(String key, String code, String name) {
		return key.equalsIgnoreCase(code) ? true : key.equalsIgnoreCase(name) ? true : false;
		
	}

}