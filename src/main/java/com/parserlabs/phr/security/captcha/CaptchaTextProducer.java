/**
 * 
 */
package com.parserlabs.phr.security.captcha;

import cn.apiclub.captcha.text.producer.TextProducer;

/**
 * @author Rajesh
 *
 */
public class CaptchaTextProducer implements TextProducer {

	private String captchaText;

	public CaptchaTextProducer(String captchaText) {
		this.captchaText = captchaText;
	}

	@Override
	public String getText() {
		return captchaText;
	}
}
