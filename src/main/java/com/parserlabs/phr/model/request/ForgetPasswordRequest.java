package com.parserlabs.phr.model.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.NotBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForgetPasswordRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83", dataType = "String", name = "transactionId", value = "Based on UUID", required = true)
	@NotBlank
	private String transactionId;
	
	@ApiModelProperty(example = "abc@7358", dataType = "String", name = "newPassword", required = true)
	@NotBlank
	private String newPassword;
	
	@ApiModelProperty(example = "abc@7358", dataType = "String", name = "confirmPassword", required = true)
	@NotBlank
	private String confirmPassword;
}
