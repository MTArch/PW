package com.parserlabs.phr.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parserlabs.phr.exception.HealthIDSystemException;
import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.proxy.HealthIdProxy;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HiDFeginException implements ErrorDecoder  {

	
	private ApiError getPhrErrorResponse(String errorResponse) {
		ApiError error = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			error = objectMapper.readValue(errorResponse, new TypeReference<ApiError>() {
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error while converting Phr System Error response to ApiError");
		}
		return error;
	}

	@Override
	public HealthIDSystemException decode(String methodKey, Response response) {
		// TODO Auto-generated method stub
		try {
			byte[] errorResponse = response.body().asInputStream().readAllBytes();
			String s = new String(errorResponse, StandardCharsets.UTF_8);
			ApiError error = getPhrErrorResponse(s);
			return new HealthIDSystemException(error);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	

}
