package com.parserlabs.phr.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.captcha.CaptchaEnterpriseResponse;
import com.parserlabs.phr.captcha.CaptchaRequest;

@FeignClient(value = "chpcha-proxy", url = "${google.recaptcha.url_template}")
@CustomSpanned
public interface CaptchaProxy {

	@PostMapping("/v1beta1/projects/healthid-327411/assessments")
	CaptchaEnterpriseResponse validateCapcha(@RequestParam("key") String key, @RequestBody CaptchaRequest request);

}
