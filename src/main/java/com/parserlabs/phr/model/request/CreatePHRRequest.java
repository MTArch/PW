package com.parserlabs.phr.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.NotBlank;
import com.parserlabs.phr.annotation.PHRAddress;
import com.parserlabs.phr.annotation.Uuid;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class CreatePHRRequest {

	@ApiModelProperty(example = "a825f76b-0696-40f3-864c-5a3a5b389a83", dataType = "String", name = "transactionId", value = "Based on UUID", required = true)
	@Uuid
	private String transactionId;

	@ApiModelProperty(example = "user@abdm", dataType = "String", name = "phrAddress", value = "Phr address/Id of the User", required = true)
	@NotBlank
//	@PHRAddress
	private String phrAddress;

	//@Encryption(required = false)
	//@Password(required = false)
	@ApiModelProperty(name = "password", dataType = "String", value = "User Password(Encrypted with public key).", example = "HNceo964MVndrs8Z2oMtzIsmmbzagveHbWkDsDKskTue+/YZhHHrMon19J03ggU457upzWMIX0nU3d38xjB3FxA2qWCVmvLZ98A9l0y3i33vq1ywu9cORGF4OEqV8l7H9h4tDnLGDHnbOh9ct85VfOohP4p73lqW6WQSMYcU+xkBfEsRj42pWL19EVsE1UULtQE8gYY1B0SeM63svUp1kQ4Pt5hdgKxibYBq+hRcck2PkEIhp2N7AkjH4Tf+AhXU9956WLwjKgAKMk7K4+Zv8JtxYCcblQitbpN4ImPH5edf4mO5R/L9RpdAVSllAQQfPIDlp5ZGOZ1GrSmhzOSP3g==")
	private String password;
	
	@ApiModelProperty(name = "isAlreadyExistedPHR", dataType = "boolean", example = "false", required = false)
	private boolean isAlreadyExistedPHR;

}