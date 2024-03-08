package com.parserlabs.phr.model.search;

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
public class SearchPhrAuthRequest {

	@ApiModelProperty(example = "user@abdm", dataType = "String", name = "phrAddress", value = "Phr Address of the user", required = true)
	private String phrAddress;

}
