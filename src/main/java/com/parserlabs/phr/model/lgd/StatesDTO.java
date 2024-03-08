package com.parserlabs.phr.model.lgd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatesDTO implements Comparable<StatesDTO> {

	@ApiModelProperty(example = "27", dataType = "String", name = "code")
	private String code;

	@ApiModelProperty(example = "MAHARASHTRA", dataType = "String", name = "name")
	private String name;

	@Builder.Default
	private List<DistrictDTO> districts = new ArrayList<DistrictDTO>();

	public void setDistricts(List<DistrictDTO> districts) {
		Collections.sort(districts);
		this.districts = districts;
	}

	@Override
	public int compareTo(StatesDTO objectToCompare) {
		return this.getName().compareTo(objectToCompare.getName());
	}

	public static StatesDTO of(String code, String name) {
		return StatesDTO.builder().code(code).name(name).build();
	}

}
