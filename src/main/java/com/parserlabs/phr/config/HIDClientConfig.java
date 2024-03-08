package com.parserlabs.phr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import com.parserlabs.phr.commons.PHRContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;

/**
 * @author suraj
 *
 */
@Configuration
public class HIDClientConfig {

	private static final String AUTHORIZATION = "Authorization";
	private static final String X_TOKEN = "X-Token";



	@Bean
	public RequestInterceptor requestInterceptor() {

		return new RequestInterceptor() {

			@Override
			public void apply(RequestTemplate template) {
				addHeader(template, X_TOKEN, PHRContextHolder.healthIdUserToken());
				addHeader(template, AUTHORIZATION, PHRContextHolder.authrizationToken());
			}
		};
	}

	private void addHeader(RequestTemplate template, String key, String value) {

		if (!ObjectUtils.isEmpty(value)) {
			template.header(key, value);
		}

	}
	
	@Bean
    public ErrorDecoder errorDecoder() {
        return new HiDFeginException();
    }

}
