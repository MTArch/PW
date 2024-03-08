package com.parserlabs.phr.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.model.KeycloakTokenRequest;
import com.parserlabs.phr.model.KeycloakTokenResponse;


@FeignClient(value = "gateway-proxy", url = "${api.client.url}" )
@CustomSpanned
public interface GatewayProxy {
	
	@PostMapping
	KeycloakTokenResponse fetchGatewayToken(@RequestBody KeycloakTokenRequest request);

	
	

}
