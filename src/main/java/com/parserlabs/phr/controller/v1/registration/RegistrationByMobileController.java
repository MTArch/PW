package com.parserlabs.phr.controller.v1.registration;

import java.util.List;

import javax.validation.Valid;

import com.parserlabs.phr.model.response.JwtResponse;
import org.springframework.http.ResponseEntity;

import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.registration.GenerateOTPRequest;
import com.parserlabs.phr.model.registration.ResendOTPRequest;
import com.parserlabs.phr.model.registration.VerifyOTPRequest;
import com.parserlabs.phr.model.request.CreatePHRRequest;
import com.parserlabs.phr.model.request.PHRSuggestionRequst;
import com.parserlabs.phr.model.request.RegistrationByMobileOrEmailRequest;
import com.parserlabs.phr.model.response.SuccessResponse;
import com.parserlabs.phr.model.response.TransactionResponse;
import com.parserlabs.phr.model.response.TransactionWithPHRResponse;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Timed
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized Access."),
		@ApiResponse(code = 400, message = "Bad request, check request before retrying", response = ApiError.class),
		@ApiResponse(code = 500, message = "Downstream system(s) is down.\nUnhandled exceptions.", response = ApiError.class) })
@Api(tags = { "Registration by Mobile Or Email APIs" })
public interface RegistrationByMobileController {

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transaction id in case of success", response = TransactionResponse.class),
			@ApiResponse(code = 422, message = "Could not Generate OTP or Mobile number may be already used.", response = ApiError.class) })
	@ApiOperation(value = "Generate Mobile OTP to start registration transaction", response = TransactionResponse.class, notes = "<b>Explanation</b>	- Api Accepts <b>Mobile Number</b> and then Generates <b>OTP</b> for it.\r\n"
			+ "<b>Request Body</b>	- Required\r\n"
			+ "<b>Response</b>	- Api Accepts <b>Mobile Number</b> and then Generates <b>OTP</b> for it. if number not found then throws error."
			+ "\r\n\r\n" + "<p>Note : \r\n</p>"
			+ "<p>1. <b style='color:red'>OTP will be valid for 10 Minutes only</b>")
	ResponseEntity<TransactionResponse> generateOTP(@Valid GenerateOTPRequest request);

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return List phrAddresses in case of success", response = List.class),
			@ApiResponse(code = 422, message = "No Suggestion found.", response = ApiError.class) })
	@ApiOperation(value = "Generate List phrAddresses in case", response = List.class, notes = "")
	ResponseEntity<List<String>> getPHRSuggestion(@Valid PHRSuggestionRequst request);

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transaction id in case of success", response = TransactionResponse.class),
			@ApiResponse(code = 422, message = "Could not Generate OTP or Mobile number may be already used.", response = ApiError.class) })
	@ApiOperation(value = "Resend Mobile OTP f registration transaction", response = SuccessResponse.class, notes = "<b>Explanation</b>	- Api Accepts <b>Transaction Number</b> and then Resend <b>OTP</b> for it.\r\n"
			+ "<b>Request Body</b>	- Required\r\n"
			+ "<b>Response</b>	- Api Accepts <b>Transaction Number</b> and then Resend <b>OTP</b> for it. if transaction number not found then throws error.")
	ResponseEntity<SuccessResponse> resendOTP(@Valid ResendOTPRequest request);

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transaction id in case of success", response = TransactionResponse.class),
			@ApiResponse(code = 422, message = "Could not Generate PHR ID, Invalid data in the request. See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Register the Beneficiary to the PHR using the Mobile/Email Address", response = TransactionResponse.class, notes = "## To Register the beneficiary.\r\n"
			+ "\r\n" + "- Beneficiary data required to pass in the request\r\n" + "## Request\r\n"
			+ "Below is the Request Parameters description \r\n" + "| Attributes | Description |\r\n"
			+ "| ------ | ------ |\r\n"
			+ "| transactionId <sup style='color:red'>* required</sup> | Transaction number, Based on UUID. |\r\n"
			+ "| firstName <sup style='color:red'>* required</sup> | First Name of the Beneficiary mentioned in the documents |\r\n"
			+ "| middleName | Middle Name of the Beneficiary mentioned in the documents |\r\n"
			+ "| lastName  | Last Name of the Beneficiary mentioned in the documents |\r\n"
			+ "| gender <sup style='color:red'>* required</sup> | Male - <b>M</b>, Female - <b>M</b>, Other - <b>O</b> |\r\n"
			+ "| email <sup style='color:red'>* required</sup> | Valid E-mail address of the beneficiary |\r\n"
			+ "| mobile <sup style='color:red'>* required</sup> | Valid 10-digit Mobile Number<sup style='color:red'>(Note - Mobile number must be encrypted)</sup> of the Beneficiary.|\r\n"
			+ "| dayOfBirth | day of birth |\r\n" + "| monthOfBirth | month of birth |\r\n"
			+ "| yearOfBirth <sup style='color:red'>* required</sup> | year of birth |\r\n"
			+ "| stateCode <sup style='color:red'>* required</sup> | Valid State Code [(LGD)](https://lgdirectory.gov.in/) |\r\n"
			+ "| districtCode | Valid District Code [(LGD)](https://lgdirectory.gov.in/). |\r\n"
			+ "| countryCode | Country code. |\r\n"
			+ "| pincode <sup style='color:red'>* required</sup> | Pincode |\r\n"
			+ "| address | Valid Address as per documents.|")
	ResponseEntity<TransactionResponse> registerDetails(@Valid RegistrationByMobileOrEmailRequest request);

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return user token id in case of success", response = JwtResponse.class),
			@ApiResponse(code = 422, message = "Could not Generate PHR ID, Invalid data in the request. See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Register the Beneficiary to the PHR using the Mobile/Email Address", response = TransactionResponse.class, notes = "## To Register the beneficiary.\r\n"
			+ "\r\n" + "- Beneficiary data required to pass in the request\r\n" + "## Request\r\n"
			+ "Below is the Request Parameters description \r\n" + "| Attributes | Description |\r\n"
			+ "| ------ | ------ |\r\n"
			+ "| transactionId <sup style='color:red'>* required</sup> | Transaction number, Based on UUID. |\r\n"
			+ "| phrAddress <sup style='color:red'>* required</sup> | PHR address of the Beneficiary or user |\r\n"
			+ "| password <sup style='color:red'>* required</sup>| Password for the account. Same will be used to login to PHR Account.<b style='color:red'> Password must contain an uppercase, a lowercase, a number, a special character (@,_$,#) and at least 8 or more characters. It should not contain any sequences (like 123)</b>|\r\n"
			+ "\r\n\r\n" + "<p>Note : \r\n</p>"
			+ "<p>1. <b style='color:red'>Password must be in encrypted form</b>, Plain text form Password(like Abdm@india1122) is not allowed in request</p>")
	ResponseEntity<JwtResponse> createPHR(@Valid CreatePHRRequest request);

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return Transaction Number in case of success", response = TransactionWithPHRResponse.class),
			@ApiResponse(code = 422, message = "Could not Find Account or Non-Existing/Expired Tocken", response = ApiError.class) })
	@ApiOperation(value = "Verify the mobile OTP to create the PHR ID.", response = TransactionWithPHRResponse.class, notes = "## API to verify the Mobile OTP\r\n"
			+ "## Request\r\n" + "Below is the Request Parameters description \r\n" + "| Attributes | Description |\r\n"
			+ "| ------ | ------ |\r\n"
			+ "| transactionId <sup style='color:red'>* required</sup> | Transaction number, Based on UUID. |\r\n"
			+ "| otp <sup style='color:red'>* required</sup> | OTP reviced on the mobile number. |\r\n" + "\r\n\r\n"
			+ "<p>Note : \r\n</p>"
			+ "<p>1. <b style='color:red'>OTP must be in encrypted form</b>, Plain text form OTP is not allowed</p>")
	ResponseEntity<TransactionWithPHRResponse> verifyOTP(@Valid VerifyOTPRequest request);
}
