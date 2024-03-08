/**
 * 
 */
package com.parserlabs.phr.controller.v1.profile;

import javax.validation.Valid;

import com.parserlabs.phr.model.profile.ProfilePasswordUpdateRequestFromHid;
import com.parserlabs.phr.model.request.UpdatePhrAttributePayLoad;

import org.springframework.http.ResponseEntity;

import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.profile.ChangesPasswordRequest;
import com.parserlabs.phr.model.profile.ProfileEditRequest;
import com.parserlabs.phr.model.profile.ProfilePasswordUpdateRequest;
import com.parserlabs.phr.model.response.SuccessResponse;
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
@Api(tags = { "Profile Collection API's." })
public interface ProfileEditUpdateController {


	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return true in case of success", response = SuccessResponse.class),
			@ApiResponse(code = 422, message = "Request in invalid, Check the request Parameter.", response = ApiError.class) })
	@ApiOperation(value = "API to Update the profile.", response = SuccessResponse.class, notes = "##  Update the account details in the PHR Account.\r\n"
			+ "\r\n" + "- Beneficiary data required to pass in the request\r\n" + "## Request\r\n"
			+ "Below is the Request Parameters description \r\n" + "| Attributes | Description |\r\n"
			+ "| ------ | ------ |\r\n"
			+ "| firstName <sup style='color:red'>* required</sup> | First Name of the Beneficiary mentioned in the documents |\r\n"
			+ "| middleName | Middle Name of the Beneficiary mentioned in the documents |\r\n"
			+ "| lastName  | Last Name of the Beneficiary mentioned in the documents |\r\n"
			+ "| gender <sup style='color:red'>* required</sup> | Male - <b>M</b>, Female - <b>M</b>, Other - <b>O</b> |\r\n"
			+ "| dayOfBirth <sup style='color:red'>* required</sup> | day of birth |\r\n"
			+ "| monthOfBirth <sup style='color:red'>* required</sup> | month of birth |\r\n"
			+ "| yearOfBirth <sup style='color:red'>* required</sup> | year of birth |\r\n"
			+ "| stateCode <sup style='color:red'>* required</sup> | Valid State Code [(LGD)](https://lgdirectory.gov.in/) |\r\n"
			+ "| districtCode | Valid District Code [(LGD)](https://lgdirectory.gov.in/). |\r\n"
			+ "| pincode <sup style='color:red'>* required</sup> | Pincode |\r\n"
			+ "| addressLine | Valid Address as per documents.|")
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X-Token")
	ResponseEntity<SuccessResponse> profileEditUpdate(@Valid ProfileEditRequest request);
	
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return true in case of success", response = SuccessResponse.class),
			@ApiResponse(code = 422, message = "Request in invalid, Check the request Parameter.", response = ApiError.class) })
	@ApiOperation(value = "API to Update the password.", response = TransactionResponse.class, notes = "##  Update the password in the PHR Account.\r\n"
			+ "## Request\r\n" + "Below is the Request Parameters description \r\n" + "| Attributes | Description |\r\n"
			+ "| ------ | ------ |\r\n"
			+ "| transactionId <sup style='color:red'>* required</sup> | Transaction number, Based on UUID. |\r\n")
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X-Token")
	ResponseEntity<SuccessResponse> updateProfilePassword(@Valid ProfilePasswordUpdateRequest request);

	ResponseEntity<SuccessResponse> updateProfilePasswordFromHid(@Valid ProfilePasswordUpdateRequestFromHid request);


	ResponseEntity<Boolean> updatePhrAttribute(UpdatePhrAttributePayLoad updatePhrAttributePayLoad);

	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X-Token")
	ResponseEntity<SuccessResponse> changesPassword(@Valid ChangesPasswordRequest request);
	
	 	
}
