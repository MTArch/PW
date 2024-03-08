/**
 * 
 */
package com.parserlabs.phr.controller.v1.profile;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.profile.UpdateProfileRequest;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.registration.VerifyOTPRequest;
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
		@ApiResponse(code = 400, message = "Bad request, check request before retrying", response = ApiError.class),
		@ApiResponse(code = 500, message = "Downstream system(s) is down.\nUnhandled exceptions.", response = ApiError.class) })
@Api(tags = { "Profile Collection API's." })
public interface MobileEmailUpdateController {
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transaction id in case of success", response = TransactionResponse.class),
			@ApiResponse(code = 422, message = "Request is invalid, Please try with valid request format.", response = ApiError.class) })
	@ApiOperation(value = "Generate Mobile OTP to start mobile update transaction", response = TransactionResponse.class, notes = "<b>Explanation</b>	- Api Accepts <b>Mobile Number</b> and then Generates <b>OTP</b> for it.\r\n"
			+ "<b>Request Body</b>	- Required\r\n"
			+ "<b>Response</b>	- Api Accepts <b>Mobile Number</b> and then Generates <b>OTP</b> for it."
			+ "\r\n\r\n" + "<p>Note : \r\n</p>"
			+ "<p>1. <b style='color:red'>OTP will be valid for 10 Minutes only</b>")
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X-Token")
	ResponseEntity<TransactionResponse> generateOTP(@Valid GenerateOTPRequest request);
	
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return Transaction Number in case of success", response = TransactionResponse.class),
			@ApiResponse(code = 422, message = "Could not Find Account or Non-Existing/Expired Token", response = ApiError.class) })
	@ApiOperation(value = "Verify the mobile OTP to create the PHR ID.", response = TransactionResponse.class, notes = "## API to verify the Mobile OTP\r\n"
			+ "## Request\r\n" + "Below is the Request Parameters description \r\n" + "| Attributes | Description |\r\n"
			+ "| ------ | ------ |\r\n"
			+ "| transactionId <sup style='color:red'>* required</sup> | Transaction number, Based on UUID. |\r\n"
			+ "| otp <sup style='color:red'>* required</sup> | OTP recevied on the email/mobile number. |\r\n" + "\r\n\r\n"
			+ "<p>Note : \r\n</p>"
			+ "<p>1. <b style='color:red'>OTP must be in encrypted form</b>, Plain text form OTP is not allowed</p>")
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X-Token")
	ResponseEntity<TransactionResponse> verifyMobileEmailOTP(@Valid VerifyOTPRequest request);
	
	
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return true in case of success", response = SuccessResponse.class),
			@ApiResponse(code = 422, message = "Request in invalid, Check the request Parameter.", response = ApiError.class) })
	@ApiOperation(value = "API to Update the Mobile/Email.", response = TransactionResponse.class, notes = "##  Update the Mobile Number or Email Address in the PHR Account.\r\n"
			+ "## Request\r\n" + "Below is the Request Parameters description \r\n" + "| Attributes | Description |\r\n"
			+ "| ------ | ------ |\r\n"
			+ "| transactionId <sup style='color:red'>* required</sup> | Transaction number, Based on UUID. |\r\n")
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X-Token")
	ResponseEntity<SuccessResponse> updateMobileEmail(@Valid UpdateProfileRequest request);
	
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return true in case of success", response = SuccessResponse.class),
			@ApiResponse(code = 422, message = "Could not Regenerate OTP, Please check the request parameter.", response = ApiError.class) })
	@ApiOperation(value = "Resend Mobile/Email OTP for updation transaction", response = SuccessResponse.class, notes = "<b>Explanation</b>	- Api Accepts <b>Transaction Number</b> and then Resend <b>OTP</b> for it.\r\n"
			+ "<b>Request Body</b>	- Required\r\n"
			+ "<b>Response</b>	- Api Accepts <b>Transaction Number</b> and then Resend <b>OTP</b> for it. if transaction number not found then throws error."
			+ "<p>1. <b style='color:red'>OTP will be valid for 10 Minutes only</b>")
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X-Token")
	ResponseEntity<SuccessResponse> resendMobileEmailOTP(@Valid ResendOTPRequest request);
}
