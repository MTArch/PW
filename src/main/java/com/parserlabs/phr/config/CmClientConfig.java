package com.parserlabs.phr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.util.ObjectUtils;

import com.parserlabs.phr.commons.PHRContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class CmClientConfig {
	private static final String AUTHORIZATION = "Authorization";

	@Bean
	public RequestInterceptor requestInterceptor() {

		return new RequestInterceptor() {

			@Override
			public void apply(RequestTemplate template) {
				addHeader(template, AUTHORIZATION, PHRContextHolder.authrizationToken());
			}
		};
	}

	private void addHeader(RequestTemplate template, String key, String value) {

		if (!ObjectUtils.isEmpty(value)) {
			template.header(key, value);
		}

	}

	
	
}
