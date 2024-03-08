package com.parserlabs.phr.controller.v1.authentication;

import javax.validation.Valid;

import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.authentication.RefreshTokenRequest;
import com.parserlabs.phr.model.authentication.ValidateTokenRequest;
import com.parserlabs.phr.model.response.AccessTokenResponse;
import org.springframework.http.ResponseEntity;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
@Timed
@Api(tags = { "Authentication" })
@SwaggerDefinition(tags = { @Tag(name = "Authentication", description = "Health ID Authentication APIs") })
public interface AuthenticationController {
	
//	
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Return Transaction in case of success", response = TransactionResponse.class)
//			})
//	@ApiOperation(value = "Initiate authentication process for given Health Id Number", response = TransactionResponse.class)
//	ResponseEntity<TransactionResponse> initiateAuth(@Valid AuthIntRequestPayLoad authRequest);
//
//	ResponseEntity<TokenResponse> fetchUser(LoginRequestPayload loginRequestPayload);
//

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return Boolean value True in case of success", response = Boolean.class),
            @ApiResponse(code = 422, message = "Expired or Invalid Token", response = ApiError.class)})
    @ApiOperation(value = "Validate auth token", response = Boolean.class,
            notes="<b>Explanation</b>	- Api Accepts <b>Auth Token</b> and then Checks is it Valid/Invalid/Expired.\r\n" +
                    "\r\n" +
                    "<b>Request Body</b>	- Required\r\n" +
                    "\r\n" +
                    "<b>Responce</b>	- Api Accepts <b>Auth Token</b> and then Checks is it Valid/Invalid/Expired. Returns Error for <b>Unauthorized Token</b>.")
    ResponseEntity<Boolean> validateToken(@Valid ValidateTokenRequest request);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful. create Access Token.", response = AccessTokenResponse.class),
            @ApiResponse(code = 422, message = "Could not create Access Token.", response = ApiError.class)})
    @ApiOperation(value = "create Access Token From Refresh Token", response = AccessTokenResponse.class,
            notes="<b>Explanation</b>	- Api Accepts <b>Refresh Token and Creates  Access Token</b>.\r\n" +
                    "\r\n" +
                    "<b>Request Body</b>	- Required\r\n" +
                    "\r\n" +
                    "<b>Responce</b>	- Api Accepts <b>Refresh Token and Creates  Access Token</b>. Returns Error for <b>Invalid/Incorrect Info.</b>.")
    ResponseEntity<AccessTokenResponse> generateAccessToken(RefreshTokenRequest refreshTokenRequest);


}
