/**
 * 
 */
package com.parserlabs.phr.controller.v1.search.impl;

import static com.parserlabs.phr.constants.APIBasePathConstants.EndPoints.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.config.security.JwtTokenUtil;
import com.parserlabs.phr.controller.v1.search.SecertKeyController;

import lombok.AllArgsConstructor;

/**
 * @author Rajesh
 *
 */

@RestController
@CrossOrigin
@RequestMapping(PUBLIC_CERT_V1_ENDPOINT)
@AllArgsConstructor
@CustomSpanned
public class SecertKeyControllerImpl implements SecertKeyController {

	@GetMapping("/certificate")
	@Override
	public ResponseEntity<String> cert() throws Exception {
		return ResponseEntity.ok(JwtTokenUtil.publicKeyContent);
	}

}
