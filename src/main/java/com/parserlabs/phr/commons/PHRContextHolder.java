package com.parserlabs.phr.commons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.parserlabs.phr.constants.Constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PHRContextHolder {

	private static final ThreadLocal<String> HEALTH_ID_NUMBER = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> PHR_ADDRESS = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> AUTHORIZATION_TOKEN = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> HEALTH_ID_USER_TOKEN = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> CLIENT_ID = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> ACCESS_TOKEN = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> UNIQUE_REQUEST_ID = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> X_HIP_ID = new InheritableThreadLocal<>();
	private static final ThreadLocal<Set<String>> CLIENT_ROLE = InheritableThreadLocal.withInitial(HashSet::new);
	private static final ThreadLocal<String> CAPTCHA_TOKEN_CHECKSUM = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> CLIENT_IP = new InheritableThreadLocal<>();
	private static final ThreadLocal<Long> CAPTCHA_TOKEN_EXPIRY_TIME = new InheritableThreadLocal<>();
	private static final ThreadLocal<Map<String, Object>> MAP = new InheritableThreadLocal<>();

	public String healthIdNumber() {
		return HEALTH_ID_NUMBER.get();
	}

	public void healthIdNumber(String clientID) {
		HEALTH_ID_NUMBER.set(clientID);
	}

	public String clientID() {
		return CLIENT_ID.get();
	}

	public void clientID(String clientID) {
		CLIENT_ID.set(clientID);
	}

	public void phrAddress(String facilityId) {
		PHR_ADDRESS.set(facilityId);
	}

	public String phrAddress() {
		return PHR_ADDRESS.get();
	}

	public String authrizationToken() {
		return AUTHORIZATION_TOKEN.get();
	}

	public void authrizationToken(String authorizationToken) {
		AUTHORIZATION_TOKEN.set(Constants.BEARER + authorizationToken);
	}

	public String healthIdUserToken() {
		return HEALTH_ID_USER_TOKEN.get();
	}

	public void healthIdUserToken(String healthIdUserToken) {
		if (StringUtils.hasLength(healthIdUserToken) && healthIdUserToken.startsWith(Constants.BEARER)) {
			HEALTH_ID_USER_TOKEN.set(healthIdUserToken);
		} else {
			HEALTH_ID_USER_TOKEN.set(Constants.BEARER + healthIdUserToken);
		}
	}

	public void accessToken(String token) {
		ACCESS_TOKEN.set(token);
	}

	public String accessToken() {
		return ACCESS_TOKEN.get();
	}

	public void clientId(String clientId) {
		CLIENT_ID.set(clientId);
	}

	public String clientId() {
		return CLIENT_ID.get();
	}

	public String uniqueRequestId() {
		return UNIQUE_REQUEST_ID.get();
	}

	public void uniqueRequestId(String healthIdNumber) {
		UNIQUE_REQUEST_ID.set(healthIdNumber);
	}

	public String xHipId() {
		return X_HIP_ID.get();
	}

	public void xHipId(String xHipId) {
		X_HIP_ID.set(xHipId);
	}

	public void captchaTokenChecksum(String checksum) {
		CAPTCHA_TOKEN_CHECKSUM.set(checksum);
	}

	public String captchaTokenChecksum() {
		return CAPTCHA_TOKEN_CHECKSUM.get();
	}

	public void captchaTokenExpiryTime(Long expiryTime) {
		CAPTCHA_TOKEN_EXPIRY_TIME.set(expiryTime);
	}

	public Long captchaTokenExpiryTime() {
		return CAPTCHA_TOKEN_EXPIRY_TIME.get();
	}

	public void clientIp(String ip) {
		CLIENT_IP.set(ip);
	}

	public String clientIp() {
		return CLIENT_IP.get();
	}

	public void clientRole(Set<String> clientRole) {
		CLIENT_ROLE.set(clientRole);
	}

	public Set<String> clientRole() {
		return CLIENT_ROLE.get();
	}

	public void set(String key, Object value) {
		if (CollectionUtils.isEmpty(MAP.get())) {
			Map<String, Object> map = new HashMap<>();
			MAP.set(map);
		}
		Map<String, Object> map = MAP.get();
		map.put(key, value);
	}

	public Object get(String key) {
		Map<String, Object> map = MAP.get();
		return CollectionUtils.isEmpty(map) ? null : map.get(key);
	}

	public void remove() {
		HEALTH_ID_NUMBER.remove();
		PHR_ADDRESS.remove();
		AUTHORIZATION_TOKEN.remove();
		HEALTH_ID_USER_TOKEN.remove();
		ACCESS_TOKEN.remove();
		CLIENT_ROLE.remove();
		CAPTCHA_TOKEN_CHECKSUM.remove();
		CLIENT_IP.remove();
		CAPTCHA_TOKEN_EXPIRY_TIME.remove();
		UNIQUE_REQUEST_ID.remove();
		CLIENT_ID.remove();
		X_HIP_ID.remove();
		MAP.remove();
	}

}
