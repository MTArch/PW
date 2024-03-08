package com.parserlabs.phr.captcha;

public interface CaptchaService {

	boolean isCaptchaValid(final String token);
}
