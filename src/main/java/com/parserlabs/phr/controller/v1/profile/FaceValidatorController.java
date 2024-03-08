/**
 * 
 */
package com.parserlabs.phr.controller.v1.profile;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.face.FaceValidationRequest;
import com.parserlabs.phr.model.face.FaceValidationResponse;
import com.parserlabs.phr.model.response.TransactionResponse;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Rajesh
 *
 */
@Timed
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized Access."),
		@ApiResponse(code = 400, message = "Bad request, check request before retrying"),
		@ApiResponse(code = 500, message = "Downstream system(s) is down.\nUnhandled exceptions.") })
@Api(tags = { "Profile Collection API's." }, hidden = true)
public interface FaceValidatorController {

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return true in case of profile pic have valid face.", response = FaceValidationResponse.class),
			@ApiResponse(code = 422, message = "Request in invalid, Check the request Parameter.", response = ApiError.class) })
	@ApiOperation(value = "API to Validate the Profile photo.", response = TransactionResponse.class, notes = "##  Validate the Profile Photo in the PHR Account.\r\n"
			+ "## Request\r\n" + "Below is the Request Parameters description \r\n" + "| Attributes | Description |\r\n"
			+ "| ------ | ------ |\r\n"
			+ "| profilePhoto <sup style='color:red'>* required</sup> |  <BASE64 ENCODED STRING> |\r\n")
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X-Token")
	ResponseEntity<FaceValidationResponse> valideProfilePhotoFace(@Valid FaceValidationRequest request);
}