package com.parserlabs.phr.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeletedUserInfoResponse {

	@ApiModelProperty(example = "user@abdm", dataType = "String", name = "phrAddress", value = "Phr address/Id of the User")
	private String phrAddress;

	@ApiModelProperty(name = "healthIdNumber", value = "HealthcareID Number.", example = "20-0878-1213-5055")
	private String healthIdNumber;

	@ApiModelProperty(name = "fullName", value = "Full name of the user", example = "Ayushman Bharat Mission")
	private String fullName;

	@ApiModelProperty(name = "kycStatus", value = "Kyc status of the user", example = "VERIFIED")
	private String kycStatus;

}
