package com.parserlabs.phr.captcha;

import org.springframework.stereotype.Service;


@Service
public class ReCaptchaAttemptService {

//	private final int MAX_ATTEMPT = 4;
//	private LoadingCache<String, Integer> attemptsCache;
//
//	public ReCaptchaAttemptService() {
//		super();
//		attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(4, TimeUnit.HOURS)
//				.build(new CacheLoader<String, Integer>() {
//					@Override
//					public Integer load(final String key) {
//						return 0;
//					}
//				});
//	}
//
//	public void reCaptchaSucceeded(final String key) {
//		attemptsCache.invalidate(key);
//	}
//
//	public void reCaptchaFailed(final String key) {
//		int attempts = attemptsCache.getUnchecked(key);
//		attempts++;
//		attemptsCache.put(key, attempts);
//	}
//
//	public boolean isBlocked(final String key) {
//		return attemptsCache.getUnchecked(key) >= MAX_ATTEMPT;
//	}

}
