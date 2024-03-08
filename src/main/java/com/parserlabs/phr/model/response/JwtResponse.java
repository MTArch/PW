package com.parserlabs.phr.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Model class to represent JWT token as response.
 * 
 * @author Ashok Kumar<ashok@parserlabs.com>
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;

	@ApiModelProperty(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", dataType = "String", name = "token", value = "JWT Bearer Token")
	private String token;

	@ApiModelProperty(example = "7200", dataType = "Long", name = "expiresIn", value = "Access Token expiry time")
	long expiresIn;

	@ApiModelProperty(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", dataType = "String", name = "refreshToken", value = "JWT Bearer Token")
	private String refreshToken;

	@ApiModelProperty(example = "86400", dataType = "Long", name = "refreshExpiresIn", value = "Refresh Token expiry time")
	long refreshExpiresIn;

	@ApiModelProperty(dataType = "String", name = "phrAddress", value = "PHR Address", example = "user@abdm", required = true)
	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String phrAdress;

	@ApiModelProperty(dataType = "String", name = "firstName", value = "First Name", example = "Rahul", required = true)
	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String firstName;

	private String authTs;
	
	
	
	
}