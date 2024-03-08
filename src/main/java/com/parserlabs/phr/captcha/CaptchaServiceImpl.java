package com.parserlabs.phr.captcha;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.proxy.CaptchaProxy;
import com.parserlabs.phr.utils.GeneralUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@CustomSpanned
public class CaptchaServiceImpl implements CaptchaService {
	@Autowired
	private CaptchaProxy captchaProxy;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	protected CaptchaSettings captchaSettings;

	@Override
	public boolean isCaptchaValid(String token) {
		boolean captchaResult = false;
		String client_ip = GeneralUtils.getClientIP(request);
		CaptchaRequest captchaRequest = CaptchaRequest.builder().event(Event.of(token, captchaSettings.getSite(), ""))
				.build();
		CaptchaEnterpriseResponse captchaResponse = CaptchaEnterpriseResponse.builder().build();
		try {
			captchaResponse=captchaProxy.validateCapcha(captchaSettings.getApikey(),captchaRequest);
		} catch (Exception exp) {
			log.error("Error occured while calling the Google captcha service.", exp);
		}
		if (captchaResponse.getTokenProperties().isValid() && captchaResponse.getScore() > 0.6) {
			log.info("Captcha was not successfully validated");
			captchaResult = true;
			//throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
		}else {
			log.warn("Captcha validation failed with reason {}", 
					captchaResponse.getTokenProperties().getInvalidReason());
			captchaResult = false;

		}
		// NOTE: added for checking security violations. 
		//Later it should be added in main captcha validation logic above.
		if(captchaResult==true 
				&& !captchaResponse.getEvent().getUserIpAddress().equals(client_ip)) {
			log.warn("Security Alert :: Non IP matching captcha request. "
					+ "captcha IP: {} and actual IP: {}",
					captchaResponse.getEvent().getUserIpAddress(),
					client_ip);
		}
		log.info(String.format("reCaptcha validation success statas is {}", captchaResult));
		return  captchaResult;
	}

}
