package com.parserlabs.phr.model.login.phr;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.Encryption;
import com.parserlabs.phr.annotation.MobileEmailValidate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@ApiModel
public class LoginViaMobileEmailRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@ApiModelProperty(name = "mobile", value = "Mobile/Email registered with PHR Address", required = true, example = "yJ2hY5bc2g3P2pQyca/ER6VYQ8TGMj/VN42h9xkh/3jAwJQtZEspnhrtEKqwFXt1+8budi64CPlUEzbkwUsCotIOMm8idfSX+SQyb8VlqLxxIkAzGvmXjWrbQUNEUWnnJjzkIjweNmj8GJ2u0uRdrAGpBc1vMoMz5XD2SGfFttvmziTtucq5w2dOoAPOni4Bl7sfii3Qyo8Szl1/tXNnZbDZi8HH9Cpajno4pFiu6mQDVTkkyDHTqyo7Bv3IFpdNYiRDAZ1yh1cBOfufMy1gSZQetCwETFxdsOgw7JvKL/gEN+RAFKZF2oUriCsAkYYbxW1cfrqa/YRXUw0ho+n4Jw==")
	@Encryption(required = true)
	@MobileEmailValidate(required = true)
	private String input;

}