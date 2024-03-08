/**
 * 
 */
package com.parserlabs.phr.controller.v1.search;

import org.springframework.http.ResponseEntity;

import com.parserlabs.phr.exception.model.ApiError;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Rajesh
 *
 */
@Timed
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized Access."),
		@ApiResponse(code = 400, message = "Bad request, check request before retrying", response = ApiError.class),
		@ApiResponse(code = 500, message = "Downstream system(s) is down.\nUnhandled exceptions.", response = ApiError.class) })
@Api(tags = {
		"Public certificate Collection API's" }, description = "Get the Public certificate for the Encryption of the sensetive Data.")
public interface SecertKeyController {
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return public certificate in case of success", response = String.class),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Authentication token public certificate. This certificate is also used to encrypt the data.", response = String.class)
	ResponseEntity<String> cert() throws Exception;
}
