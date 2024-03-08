package com.parserlabs.phr.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.config.CmClientConfig;
import com.parserlabs.phr.model.ShareCMRequestPlayLoad;
import com.parserlabs.phr.model.response.CmResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(value = "cm-proxy", url = "${api.cm.url}" ,configuration = CmClientConfig.class)
@CustomSpanned
public interface CmProxy {
	
	@PostMapping("/profile/update")
    CmResponse sharePHRProfile(@RequestBody ShareCMRequestPlayLoad shareCMRequestPlayLoad);
	
	
}
