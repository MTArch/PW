package com.parserlabs.phr.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class CacheKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuilder key = new StringBuilder();
		for (Object param : params) {
			if(param != null) {
				key.append(param.toString());
			}
		}
		return key.toString();
	}
}