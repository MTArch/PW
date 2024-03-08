package com.parserlabs.phr.controller.v1.authentication.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.config.security.JwtTokenUtil;
import com.parserlabs.phr.controller.v1.authentication.AuthenticationController;
import com.parserlabs.phr.model.authentication.RefreshTokenRequest;
import com.parserlabs.phr.model.authentication.ValidateTokenRequest;
import com.parserlabs.phr.model.response.AccessTokenResponse;
import com.parserlabs.phr.service.RefreshTokenService;
import com.parserlabs.phr.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suraj
 *
 */
@RestController
@CrossOrigin
@RequestMapping(AUTH_V1_ENDPOINT)
@AllArgsConstructor
@Slf4j
@CustomSpanned
public class AuthenticationImpl implements AuthenticationController {

    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;
    private RefreshTokenService refreshTokenService;


    @PostMapping("/verify/token")
    @Override
    public ResponseEntity<Boolean> validateToken(@Valid @RequestBody ValidateTokenRequest request) {
        String jwtToken = request.getAuthToken();
        Boolean isValid = false;
        try {
            String phrAddres = jwtTokenUtil.getUsernameFromToken(jwtToken);
            if(userService.isPhrAddressAlreadyExist(phrAddres)){
                isValid = !jwtTokenUtil.isTokenExpired(jwtToken);
            }
        } catch (Exception e) {
            log.error("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
        }
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/generate/access-token")
    @Override
    public  ResponseEntity<AccessTokenResponse> generateAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(refreshTokenService.generateAccessToken(refreshTokenRequest));
    }
}
