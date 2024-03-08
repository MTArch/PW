package com.parserlabs.phr.controller.v1.search;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.request.SearchRequestPayLoad;
import com.parserlabs.phr.model.response.SearchResponsePayLoad;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Timed
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized Access."),
		@ApiResponse(code = 400, message = "Bad request, check request before retrying", response = ApiError.class),
		@ApiResponse(code = 500, message = "Downstream system(s) is down.\nUnhandled exceptions.", response = ApiError.class) })
@Api(tags = { "Search Collection API's" }, description = "Fetch the PHR Address Details.")
public interface SearchController {

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return PHR ADDRESS  Details in case of success", response = SearchResponsePayLoad.class),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Get the PHR Address details using the healthID Number.", response = SearchResponsePayLoad.class)
	ResponseEntity<SearchResponsePayLoad> SearchByHealthId(@Valid SearchRequestPayLoad searchRequestPayLoad);

	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return PHR ADDRESS  exist or not in case of success", response = Map.class),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Check the PHR address mapped with the provided health ID number", response = Map.class)
	ResponseEntity<Map<String, Boolean>> doesPhrAddressExists(String phrAddress);

}
