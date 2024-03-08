package com.parserlabs.phr.model.profile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parserlabs.phr.annotation.Encryption;
import com.parserlabs.phr.annotation.Password;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangesPasswordRequest {

	@Encryption(required = true)
	@Password(required = true)
	@ApiModelProperty(name = "password", dataType = "String", value = "User Password(Encrypted with public key).", example = "HNceo964MVndrs8Z2oMtzIsmmbzagveHbWkDsDKskTue+/YZhHHrMon19J03ggU457upzWMIX0nU3d38xjB3FxA2qWCVmvLZ98A9l0y3i33vq1ywu9cORGF4OEqV8l7H9h4tDnLGDHnbOh9ct85VfOohP4p73lqW6WQSMYcU+xkBfEsRj42pWL19EVsE1UULtQE8gYY1B0SeM63svUp1kQ4Pt5hdgKxibYBq+hRcck2PkEIhp2N7AkjH4Tf+AhXU9956WLwjKgAKMk7K4+Zv8JtxYCcblQitbpN4ImPH5edf4mO5R/L9RpdAVSllAQQfPIDlp5ZGOZ1GrSmhzOSP3g==")
	private String password;
	
	@Encryption(required = true)
	@Password(required = true)
	@ApiModelProperty(name = "oldPassword", dataType = "String", value = "User Password(Encrypted with public key).", example = "HNceo964MVndrs8Z2oMtzIsmmbzagveHbWkDsDKskTue+/YZhHHrMon19J03ggU457upzWMIX0nU3d38xjB3FxA2qWCVmvLZ98A9l0y3i33vq1ywu9cORGF4OEqV8l7H9h4tDnLGDHnbOh9ct85VfOohP4p73lqW6WQSMYcU+xkBfEsRj42pWL19EVsE1UULtQE8gYY1B0SeM63svUp1kQ4Pt5hdgKxibYBq+hRcck2PkEIhp2N7AkjH4Tf+AhXU9956WLwjKgAKMk7K4+Zv8JtxYCcblQitbpN4ImPH5edf4mO5R/L9RpdAVSllAQQfPIDlp5ZGOZ1GrSmhzOSP3g==")
	private String oldPassword;
}
