/**
 * 
 */
package com.parserlabs.phr.model.profile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.Uuid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rajesh
 *
 */

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class UpdateProfileRequest {
	
	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83",dataType="String",name="transactionId",value="Based on UUID",required = true)
	@Uuid
	private String transactionId;

}
