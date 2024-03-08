package com.parserlabs.phr.controller.v1.search;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.District;
import com.parserlabs.phr.model.States;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Timed
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized Access."),
		@ApiResponse(code = 400, message = "Bad request, check request before retrying", response = ApiError.class),
		@ApiResponse(code = 500, message = "Downstream system(s) is down.\nUnhandled exceptions.", response = ApiError.class) })
@Api(tags = { "Location APIs" })
public interface LocationController {

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return district list in case of success", response = List.class),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Fetch The list of districts inside state.")
	ResponseEntity<List<District>> getDistrictsInState(String stateCode);

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return State list in case of success", response = List.class),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Fetch the list of states.")
	ResponseEntity<List<States>> getStates();

}