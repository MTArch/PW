package com.parserlabs.phr.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;

	@ApiModelProperty(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", dataType = "String", name = "accessToken", value = "JWT Bearer Token")
	private String accessToken;

	@ApiModelProperty(example = "7200", dataType = "Long", name = "expiresIn", value = "Access Token expiry time")
	long expiresIn;

	public AccessTokenResponse(String accessToken) {
		super();
		this.accessToken = accessToken;
	}

}
