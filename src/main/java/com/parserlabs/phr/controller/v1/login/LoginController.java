package com.parserlabs.phr.controller.v1.login;

import java.util.Map;

/**
 * 
 * @author Rajesh
 *
 */

import javax.validation.Valid;

import com.parserlabs.phr.model.response.JwtResponse;
import org.springframework.http.ResponseEntity;

import com.parserlabs.phr.constants.DocumentMessagesRegistrationByHID;
import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.login.mobileemail.LoginPostVerificationRequest;
import com.parserlabs.phr.model.login.mobileemail.LoginPreVerificationRequest;
import com.parserlabs.phr.model.login.mobileemail.LoginPreVerificationResponse;
import com.parserlabs.phr.model.login.phr.LoginViaMobileEmailRequest;
import com.parserlabs.phr.model.login.phr.LoginViaPhrRequest;
import com.parserlabs.phr.model.login.phr.VerifyPasswordOtpLoginRequest;
import com.parserlabs.phr.model.profile.User;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.request.AuthIntRequestPayLoad;
import com.parserlabs.phr.model.request.SearchByHealthIdNumberRequest;
import com.parserlabs.phr.model.response.SearchResponsePayLoad;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.model.search.SearchPhrAuthResponse;

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
@Api(tags = { "Login Collection API's" }, description = "Login API's by Phr Addres, Mobile, Email-Id.")
public interface LoginController {

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transaction id in case of success", response = TransactionResponse.class),
			@ApiResponse(code = 422, message = "Could not Generate OTP, Invalid request data", response = ApiError.class) })
	@ApiOperation(value = "Initiate the Login transaction using the PHR ADDRESS, Based on the Auth mode generate Mobile/Email OTP to start Login transaction", response = TransactionResponse.class, notes = "<b>Explanation</b>	- Api Accepts <b>PHR ADDRESS</b> and then Generates <b>OTP</b>based on Authentication mode for it.\r\n"
			+ "<b>Request Body</b>	- Required\r\n"
			+ "<b>Response</b>	- Api Accepts <b>PHR ADDRESS</b> and then Generates <b>OTP</b>based on the auth methods for it. if number not found then throws error."
			+ "\r\n\r\n" + "<p>Note : \r\n</p>"
			+ "<p>1. <b style='color:red'>OTP will be valid for 10 Minutes only</b>")
	ResponseEntity<TransactionResponse> initLogin(@Valid LoginViaPhrRequest request);

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return user Token id in case of success", response = JwtResponse.class),
			@ApiResponse(code = 422, message = "Could not Verify OTP or Password, Input Request Data.", response = ApiError.class) })
	@ApiOperation(value = "Verify the Login transaction, Based on the Auth mode verify password, Mobile/Email OTP", response = TransactionResponse.class, notes = "<b>Explanation</b>	- Api Accepts <b>Transaction Id and Passowrd, Mobile/Email OTP</b> based on Auth methods, Plain text credentails is not allowed.\r\n"
			+ "<b>Request Body</b>	- Required\r\n" + "\r\n\r\n" + "<p>Note : \r\n</p>"
			+ "<p>1. <b style='color:red'>OTP will be valid for 10 Minutes only</b>")
	ResponseEntity<JwtResponse> VerifyLogin(@Valid VerifyPasswordOtpLoginRequest request);
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transaction id in case of success", response = TransactionResponse.class),
			@ApiResponse(code = 422, message = "Could not Generate OTP, Invalid Request data", response = ApiError.class) })
	@ApiOperation(value = "Generate Mobile/Email OTP to start Login transaction", response = TransactionResponse.class, notes = "<b>Explanation</b>	- Api Accepts <b>Mobile/Email address</b> and then Generates <b>OTP</b> for it.\r\n"
			+ "<b>Request Body</b>	- Required\r\n"
			+ "<b>Response</b>	- Api Accepts <b>PHR ADDRESS</b> and then Generates <b>OTP</b>based on the auth methods for it. if number not found then throws error."
			+ "\r\n\r\n" + "<p>Note : \r\n</p>"
			+ "<p>1. <b style='color:red'>OTP will be valid for 10 Minutes only</b>")
	ResponseEntity<TransactionResponse> initLoginViaMobileEmail(@Valid LoginViaMobileEmailRequest request);
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return list of phr address mapped with mobile in case of success", response = JwtResponse.class),
			@ApiResponse(code = 422, message = "Could not Verify OTP , Invalid Request Data.", response = ApiError.class) })
	@ApiOperation(value = "Verify Mobile/Email OTP to start Login ", response = TransactionResponse.class, notes = "<b>Explanation</b>	- Api Accepts <b>Transaction  Number</b> and <b>OTP(Encrypted form)</b> for it.\r\n"
			+ "<b>Request Body</b>	- Required\r\n" + "\r\n\r\n" + "<p>Note : \r\n</p>"
			+ "<p>1. <b style='color:red'>OTP will be valid for 10 Minutes only</b>")
	ResponseEntity<LoginPreVerificationResponse> preVerificationViaMobileEmail(@Valid LoginPreVerificationRequest request);

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return list of phr address mapped with mobile in case of success", response = JwtResponse.class),
			@ApiResponse(code = 422, message = "Could not Verify OTP , Invalid Request Data.", response = ApiError.class) })
	@ApiOperation(value = "Get the User Token in the mobile/email login flow", notes = "fetch the User token ", response = JwtResponse.class)
	ResponseEntity<JwtResponse> postVerificationViaMobileEmail(@Valid LoginPostVerificationRequest request);
	
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transaction id in case of success", response = TransactionResponse.class),
			@ApiResponse(code = 422, message = "Could not Generate OTP , Invalid request data.", response = ApiError.class) })
	@ApiOperation(value = "Resend Mobile/Email OTP to provided transaction Id", response = TransactionResponse.class, notes = "<b>Explanation</b>	- Api Accepts <b>transaction number</b> and then Re-Generates <b>OTP</b> for it.\r\n"
			+ "<b>Request Body</b>	- Required\r\n"
			+ "<b>Response</b>	- Api Accepts <b>Transaction number</b> and then Re-Generates <b>OTP</b>based on the auth methods for it. if number not found then throws error."
			+ "\r\n\r\n" + "<p>Note : \r\n</p>"
			+ "<p>1. <b style='color:red'>OTP will be valid for 10 Minutes only</b>")
	ResponseEntity<TransactionResponse>resendOtpLogin(@Valid ResendOTPRequest request);
	

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = DocumentMessagesRegistrationByHID.SEARCH_MESSAGE_Code_200, response = Map.class),
			@ApiResponse(code = 422, message = DocumentMessagesRegistrationByHID.SEARCH_MESSAGE_Code_422, response = ApiError.class) })
	@ApiOperation(value = "Sequence_1: Search a user by Health ID Number.", response = SearchResponsePayLoad.class, notes = "<b>Explanation</b>	- Api Checks <b>Health ID Number</b> to <b>find User</b>.\r\n"
			+ "\r\n" + "<b>Request Body</b>	- Required\r\n" + "\r\n"
			+ "<b>Response</b>	- Retrun partial details of Health ID Number.")
	ResponseEntity<SearchResponsePayLoad> SearchByHealthIdNumber(SearchByHealthIdNumberRequest request);
	
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transaction id in case of success", response = TransactionResponse.class),
			@ApiResponse(code = 422, message = "Could not Generate OTP, Invalid request data", response = ApiError.class) })
	@ApiOperation(value = "Initiate the Login transaction using the healthId number, Based on the Auth mode generate OTP to start Login transaction", response = TransactionResponse.class, notes = "<b>Explanation</b>	- Api Accepts <b>PHR ADDRESS</b> and then Generates <b>OTP</b>based on Authentication mode for it.\r\n"
			+ "<b>Request Body</b>	- Required\r\n"
			+ "<b>Response</b>	- Api Accepts <b>HealthId Number</b> and then Generates <b>OTP</b>based on the auth methods for it. if number not found then throws error."
			+ "\r\n\r\n" + "<p>Note : \r\n</p>"
			+ "<p>1. <b style='color:red'>OTP will be valid for 10 Minutes only</b>")
	ResponseEntity<TransactionResponse> healthIdNumberInit(@Valid AuthIntRequestPayLoad authIntRequest);
	
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return PHR ADDRESS Authentication method Details in case of success", response = SearchPhrAuthResponse.class),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Get the PHR Address authentication methods.", response = String.class)
	@ApiImplicitParam(value = "PHR Address", name = "phrAddress", required = true)
	ResponseEntity<SearchPhrAuthResponse> fetchPhrAuthMode(@Valid String phrAddress);


	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return PHR ADDRESS Details in case of success", response = SearchPhrAuthResponse.class),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Get the PHR Address authentication methods.", response = User.class)
	@ApiImplicitParam(value = "PHR Address", name = "phrAddress", required = true)
	ResponseEntity<User> searchPhrAdrees(String phrAddress);

}
