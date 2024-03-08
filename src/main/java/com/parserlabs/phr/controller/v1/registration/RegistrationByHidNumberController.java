package com.parserlabs.phr.controller.v1.registration;

import java.util.Map;

import javax.validation.Valid;

import com.parserlabs.phr.model.response.JwtResponse;
import org.springframework.http.ResponseEntity;

import com.parserlabs.phr.constants.Constants.DocumentMessages;
import com.parserlabs.phr.constants.DocumentMessagesRegistrationByHID;
import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.adapter.response.HidResponse;
import com.parserlabs.phr.model.request.AuthIntRequestPayLoad;
import com.parserlabs.phr.model.request.CreatePHRRequest;
import com.parserlabs.phr.model.request.LoginRequestPayload;
import com.parserlabs.phr.model.request.SearchRequestPayLoad;
import com.parserlabs.phr.model.response.SearchResponsePayLoad;
import com.parserlabs.phr.model.response.TransactionResponse;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Timed
@ApiResponses(value = { @ApiResponse(code = 401, message = DocumentMessages.MESSAGE_Code_401),
		@ApiResponse(code = 400, message = DocumentMessages.MESSAGE_Code_400, response = ApiError.class),
		@ApiResponse(code = 500, message = DocumentMessages.MESSAGE_Code_500, response = ApiError.class) })
@Api(value = "Registration", tags = { "Registration By Health ID Number" })
public interface RegistrationByHidNumberController {

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = DocumentMessagesRegistrationByHID.SEARCH_MESSAGE_Code_200, response = Map.class),
			@ApiResponse(code = 422, message = DocumentMessagesRegistrationByHID.SEARCH_MESSAGE_Code_422, response = ApiError.class) })
	@ApiOperation(value = "Sequence_1: Search a user by Health ID Number.", response = SearchResponsePayLoad.class, notes = "<b>Explanation</b>	- Api Checks <b>Health ID Number</b> to <b>find User</b>.\r\n"
			+ "\r\n" + "<b>Request Body</b>	- Required\r\n" + "\r\n"
			+ "<b>Response</b>	- Retrun partial details of Health ID Number.")
	ResponseEntity<SearchResponsePayLoad> sequence_1(@Valid SearchRequestPayLoad searchRequestPayLoad);

	

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = DocumentMessagesRegistrationByHID.SEARCH_MESSAGE_Code_200, response = Map.class),
			@ApiResponse(code = 422, message = DocumentMessagesRegistrationByHID.SEARCH_MESSAGE_Code_422, response = ApiError.class) })
	@ApiOperation(value = "Sequence_2: It will create the transaction and send the otp on mobile number", response = TransactionResponse.class, notes = "<b>Explanation</b>	- Api Checks <b>Health ID Number</b> to <b>find User</b>.\r\n"
			+ "\r\n" + "<b>Request Body</b>	- Required\r\n" + "\r\n"
			+ "<b>Response</b>	- Retrun transaction in case of success against request.")
	ResponseEntity<TransactionResponse> sequence_2(@Valid AuthIntRequestPayLoad authIntRequest);

	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = DocumentMessagesRegistrationByHID.SEARCH_MESSAGE_Code_200, response = Map.class),
			@ApiResponse(code = 422, message = DocumentMessagesRegistrationByHID.SEARCH_MESSAGE_Code_422, response = ApiError.class) })
	@ApiOperation(value = "Sequence_2: it will the transaction and send the otp on mobile number", response = TransactionResponse.class, notes = "<b>Explanation</b>	- Api Checks <b>Health ID Number</b> to <b>find User</b>.\r\n"
			+ "\r\n" + "<b>Request Body</b>	- Required\r\n" + "\r\n"
			+ "<b>Response</b>	- Retrun transaction in case of success against request.")
	ResponseEntity<HidResponse> sequence_3(LoginRequestPayload loginRequestPayload);

	ResponseEntity<JwtResponse> sequence_4(@Valid CreatePHRRequest createPHRRequest);

}
