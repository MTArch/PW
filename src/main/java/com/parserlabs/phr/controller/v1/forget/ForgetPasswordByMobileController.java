package com.parserlabs.phr.controller.v1.forget;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.request.ForgetPasswordRequest;
import com.parserlabs.phr.model.request.ForgetPasswordVerifyOtp;
import com.parserlabs.phr.model.request.GenerateForgetPassOtpRequest;
import com.parserlabs.phr.model.response.ForgetPassGenOtpTransResponse;
import com.parserlabs.phr.model.response.ForgetPassVerifyOtpResponse;
import com.parserlabs.phr.model.response.SuccessResponse;
import com.parserlabs.phr.model.search.SearchPhrAuthResponse;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Timed
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized Access."),
		@ApiResponse(code = 400, message = "Bad request, check request before retrying", response = ApiError.class),
		@ApiResponse(code = 500, message = "Downstream system(s) is down.\nUnhandled exceptions.", response = ApiError.class) })
@Api(tags = { "Forget Password API's" }, description = "Forget Password API by Phr Addres.")
public interface ForgetPasswordByMobileController {

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transaction id and auth method in case of success", response = ForgetPassGenOtpTransResponse.class),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Get the PHR Address authentication methods.", response = String.class)
	@ApiImplicitParam(value = "PHR Address", name = "phrAddress", required = true)
	ResponseEntity<ForgetPassGenOtpTransResponse> sendOtpBasedOnPhrAddress(@Valid GenerateForgetPassOtpRequest generateForgetPassOtpReq);

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return transaction id and phr address in case of success", response = ForgetPassVerifyOtpResponse.class),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Verify forget password otp based on auth method.", response = String.class)
	ResponseEntity<ForgetPassVerifyOtpResponse> forgetPasswordVerifyOtp(ForgetPasswordVerifyOtp forgetPassVerifyOtp);
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return boolean value in case of success", response = SuccessResponse.class),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.", response = ApiError.class) })
	@ApiOperation(value = "Forget the phr address password.", response = String.class)
	
	ResponseEntity<SuccessResponse> forgetPasswordAndSetNewPassword(ForgetPasswordRequest forgetPasswordRequest);
}
