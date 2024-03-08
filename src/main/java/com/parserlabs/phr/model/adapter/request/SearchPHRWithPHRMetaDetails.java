package com.parserlabs.phr.model.adapter.request;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel
public class SearchPHRWithPHRMetaDetails {

	@ApiModelProperty(example = "kishan kumar singh", dataType = "String", name = "name")
	private String name;

	@ApiModelProperty(example = "M", dataType = "String", name = "gender", allowableValues = "M,F,O,U")
	private String gender;

	@ApiModelProperty(example = "1995", dataType = "String", name = "yearOfBirth")
	private String yearOfBirth;

	@ApiModelProperty(example = "05", dataType = "String", name = "monthOfBirth")
	private String monthOfBirth;

	@ApiModelProperty(example = "31", dataType = "String", name = "dayOfBirth")
	private String dayOfBirth;



}
