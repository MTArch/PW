package com.parserlabs.phr.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import cn.apiclub.captcha.Captcha;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CaptchaUtils {
	public String encodeBase64(Captcha captcha) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(captcha.getImage(), "png", outputStream);
			return DatatypeConverter.printBase64Binary(outputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] getCaptchaByteStream(Captcha captcha) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(captcha.getImage(), "png", outputStream);
			return outputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * URI encoder
	 * 
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String encodeValue(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
	}

	/***
	 * URI decoder
	 * 
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String decode(String value) throws UnsupportedEncodingException {
		return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
	}

	public String decodebase64(String base64Str) {
		byte[] decoded = Base64.getDecoder().decode(base64Str);
		return new String(decoded, StandardCharsets.UTF_8);
	}

}
