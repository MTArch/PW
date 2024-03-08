package com.parserlabs.phr.controller.v1.captcha;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.captcha.CaptchaBuilderResponse;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@Timed
@ApiResponses(value = {
		@ApiResponse(responseCode = "400", description = "Bad Request. Please provide valid request syntax", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
		@ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
		@ApiResponse(responseCode = "500", description = "Server encountered an unexpected condition that prevented it from fulfilling the request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))) })
@Tag(name =  "Security Captcha" , description = "Generate the Security Captcha.")
public interface CaptchaController {

	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully Generated the Catpacha", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CaptchaBuilderResponse.class))) })
	@ApiOperation(value = "Generate Captcha PNG", notes  ="Generate Captcha PNG" , response = CaptchaBuilderResponse.class)
	public ResponseEntity<CaptchaBuilderResponse> generateCaptcha(@Valid String request);

	
	
	
}
