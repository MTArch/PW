package com.parserlabs.phr.interceptor;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.config.security.SecurityClientContext;
import com.parserlabs.phr.utils.PhrUtilits;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Aspect
@Configuration
@Slf4j
public class ResponseInterceptor {

	private HttpServletResponse response;

	@After("execution(* com.parserlabs.phr.controller.v1.*.*.*(..))")
	public void postHandler(JoinPoint joinPoint) {
		SecurityClientContext securityClientContext = PhrUtilits.clientContext();
		Boolean isCaptch = securityClientContext.isCaptch();
		log.info("checking the captcha status  {} ",isCaptch);
		if (isCaptch) {
			response.setHeader("X-HIP-ID", "PHRIdWeb");
			response.setHeader("Access-Control-Expose-Headers", "X-HIP-ID");
			PHRContextHolder.clientId("PHR-WEB");
		}
	}
}
