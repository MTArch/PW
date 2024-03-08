/**
 * 
 */
package com.parserlabs.phr.controller.v1.profile;

import java.io.IOException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.zxing.WriterException;
import com.parserlabs.phr.constants.DocumentMessagesRegistrationByHID;
import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.adapter.response.PHRLinkResponse;
import com.parserlabs.phr.model.authentication.ValidateTokenRequest;
import com.parserlabs.phr.model.login.phr.DeletePhrRequest;
import com.parserlabs.phr.model.profile.User;
import com.parserlabs.phr.model.request.LinkedHidPlayLoad;
import com.parserlabs.phr.model.request.LinkedPlayLoad;
import com.parserlabs.phr.model.request.PhrGenerateOtpRequest;
import com.parserlabs.phr.model.response.DeletedUserInfoResponse;
import com.parserlabs.phr.model.response.TransactionResponse;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author Rajesh
 *
 */
@Timed
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized Access."),
		@ApiResponse(code = 400, message = "Bad request, check request before retrying"),
		@ApiResponse(code = 500, message = "Downstream system(s) is down.\nUnhandled exceptions.") })
@Api(tags = { "Profile Collection API's." })
public interface ProfileController {

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Return PHR User Account Details in case of success"),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.") })
	@ApiOperation(value = "Get the user details", response = User.class)
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X-Token")
	ResponseEntity<User> get();

//	@ApiResponses(value = { @ApiResponse(code = 200, message = "PHR ACCOUNT deleted in case of success"),
//			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.") })
//	@ApiOperation(value = "Delete the user details", response = Void.class)
//	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = DeletedUserInfoResponse.class, example = "Bearer X-Token")
//	public ResponseEntity<DeletedUserInfoResponse> delete(DeletePhrRequest request);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "link and delink phr address with health id number"),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.") })
	@ApiOperation(value = "link and delink phr address with health id number", response = Map.class)
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X-Token")

	ResponseEntity<Map<String, Boolean>> link(LinkedPlayLoad linkedPlayLoad);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "PHR QrCode in case of success"),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.") })
	@ApiOperation(value = "Get QR code in PNG format for this account.", produces = MediaType.IMAGE_PNG_VALUE)
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X Token")
	ResponseEntity<byte[]> getQrCode();

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = DocumentMessagesRegistrationByHID.SEARCH_MESSAGE_Code_200, response = Map.class),
			@ApiResponse(code = 422, message = DocumentMessagesRegistrationByHID.SEARCH_MESSAGE_Code_422, response = ApiError.class) })
	@ApiOperation(value = "It will fetch details of User along with the creation and verification of PHR registration transaction.", response = TransactionResponse.class, notes = "<b>Explanation</b>	- It will fetch details of User along with the creation and verification of PHR registration transaction.\r\n"
			+ "\r\n" + "<b>Request Body</b>	- Required\r\n" + "\r\n" + "<b>Response</b>	- Retruns user details.")
	ResponseEntity<PHRLinkResponse> linkProfileDetails(@Valid @RequestBody ValidateTokenRequest request);

	@ApiResponses(value = { @ApiResponse(code = 200, message = "link phr address with health id number use token"),
			@ApiResponse(code = 422, message = "Invalid Request data, See API error for more details.") })
	@ApiOperation(value = "link phr address with health id number", response = Map.class)
	ResponseEntity<Map<String, Boolean>> linkHidToken(LinkedHidPlayLoad linkedHidPlayLoad);

	@ApiIgnore
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X Token")
	ResponseEntity<byte[]> getQrCodeInStringFormat() throws WriterException, IOException;

	@ApiOperation(value = "Generate ABHA CARD in (PDF, PNG, SVG) format", response = UserDTO.class)
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer X-Token"),
			@ApiImplicitParam(name = "mediaType", value = "mediaType", required = true, allowableValues = "PDF, PNG, SVG", allowEmptyValue = false, paramType = "path", dataTypeClass = String.class, example = "PDF") })
	ResponseEntity<ByteArrayResource> generateCard(String mediaType);
	
	
	@ApiIgnore
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return profile details in case of success", response = User.class),
			@ApiResponse(code = 422, message = "Could not find the document", response = ApiError.class)
			})
	@ApiOperation(value = "Fetch Profile by QR Code Scan.", response = User.class, notes = "This API returns details of the facility being asked for.")
	ResponseEntity<User> fetchProfileDetailsByQrCodeScan(@ApiParam(name =  "abhaAddress",type = "String", value = "abhaAddress",example = "53-3155-4688-2372", required = true) String abhaAddress,
			@ApiParam(name =  "code",type = "code", value = "code",example = "", required = true) String code);
	
	
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return status ok in case of success", response = Void.class),
			@ApiResponse(code = 422, message = "Could not Log out or Invalid Token", response = ApiError.class)})
	@ApiOperation(value = "Logout the HealthID.", response = Void.class,
			notes="<b>Explanation</b>	- Api Accepts <b>Auth Token</b> and log outs the Phr Address.\r\n" + 
					"\r\n" + 
					"<b>Request Body</b>	- Required\r\n" + 
					"\r\n" + 
					"<b>Responce</b>	- Api Accepts <b>Auth Token</b> and log outs the Phr Address. Returns Error for <b>Unauthorized token </b>.")
	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer XToken")
	ResponseEntity<Void> logout();

	@ApiImplicitParam(name = "X-Token", value = "Auth Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer XToken")
	ResponseEntity<TransactionResponse> initLogin(PhrGenerateOtpRequest request);

}
