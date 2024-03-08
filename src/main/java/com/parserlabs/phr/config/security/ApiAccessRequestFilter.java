
package com.parserlabs.phr.config.security;

import static com.parserlabs.phr.exception.model.ErrorCode.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.parserlabs.phr.cache.RedisCacheService;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.exception.CaptchaCodeValidationException;
import com.parserlabs.phr.exception.CaptchaSessionExpiredException;
import com.parserlabs.phr.exception.model.ApiError;
import com.parserlabs.phr.exception.model.ErrorDetails;
import com.parserlabs.phr.security.captcha.CaptchaSecurityService;
import com.parserlabs.phr.utils.GeneralUtils;
import com.parserlabs.phr.utils.PHRIdUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApiAccessRequestFilter extends OncePerRequestFilter {

	private static final String CAPTCHA_TOKEN_PREFIX = "CT-";
	private static final long BRUTE_FORCE_ALLOWED_TIME_IN_SEC = 30;
	private static final long BRUTE_FORCE_ALLOWED_FREQUENCY = 5;

	private static final int USER_CAPTCHA_EXPIRY_MINUTES = 15;

	public static boolean IS_MOBILE = false;

	@Value("${apis.secured: false}")
	private boolean apiSecurityApplied;
	@Value("${api.client.id}")
	private String clientId;

	@Value("${api.client.secret}")
	private String clientSecret;

	@Autowired
	private KeyCloakAuthService keyCloakAuthService;

//	@Autowired
//	private CaptchaService captchaService;

	@Autowired
	private CaptchaSecurityService captchaSecurityService;

	private Set<String> excludeUris = Set.of("/api/actuator/prometheus");


	private static final String Z_AUTHORIZATION = "Authorization";
	private static final String X_HIP_ID = "X-HIP-ID";
	private static final String SECURITY_DISABLED = "";

	@Autowired
	private RedisCacheService redisCacheService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private GatewayTokenConfig gatewayTokenConfig;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
				Long startTime = System.nanoTime();
		try {
		 if(!request.getRequestURI().contains("/api/actuator/prometheus")){

			log.info("Security enabled : {}", apiSecurityApplied);

			log.info("Added MDC");
			ThreadContext.put("request.id", UUID.randomUUID().toString().replace("-", ""));
			ThreadContext.put("endpoint", request.getRequestURI().replace("/api/v1", ""));
		   }

			String keycloakAccessToken = null;
			String keycloakRefreshToken = null;
			String relativePath = SecurityUtil.getRelativePath(request);

			log.info("Security enabled : {}", apiSecurityApplied);

			if (apiSecurityApplied && SecurityUtil.isSecureUrl(request, InsecureUrls.GUEST_URLS)) {
				String token = request.getHeader(Z_AUTHORIZATION);
				String clientId = null;
				String xhipId = request.getHeader(X_HIP_ID);
				Boolean isCaptch = false;
				if (!ObjectUtils.isEmpty(token) && token.startsWith("Captcha_") && token.length() > 30) {
					token = token.substring(8).trim();
					// Only check URLs which are subject to reCaptcha brute-force attack.
					if (InsecureUrls.CAPTCHA_BRUTE_FORCE_CHECK_URLS.contains(relativePath)) {
						handleBruteForceAttack(token);
					}

					if (Objects.nonNull(token) && doesCaptchaTokenValid(token)) {
						clientId = this.clientId;
						isCaptch = true;
					}

					if (InsecureUrls.HEALTH_ID_URLS.contains(relativePath)) {
						log.info("relative path to call health id {}", relativePath);
						PHRContextHolder.authrizationToken(gatewayTokenConfig.fetchGatewayToken());
					}
				} else if (!ObjectUtils.isEmpty(token) && (token.startsWith("Bearer ") || token.startsWith("bearer "))) {
					clientId = keyCloakAuthService.fetchClientId(token, request);

					IS_MOBILE = true;

				}
				if (!ObjectUtils.isEmpty(clientId)) {
					SecurityContextHolder.getContext().setAuthentication(populateAuthentication(request, clientId,
							keycloakAccessToken, keycloakRefreshToken, xhipId, isCaptch));

					// set header attributes in context
					PHRContextHolder.clientId(clientId);
					// HealthIdContextHolder.set(Constants.REQUESTER_ID_HEADER,
					// request.getHeader("requester-id"));
					// HealthIdContextHolder.set(Constants.REQUESTER_TYPE_HEADER,
					// request.getHeader("requester-type"));
					// HealthIdContextHolder.set(Constants.PURPOSE_HEADER,
					// request.getHeader("purpose"));
				}
			} else {
				SecurityContextHolder.getContext()
						.setAuthentication(populateAuthentication(request, SECURITY_DISABLED, SECURITY_DISABLED));
				if (!SecurityUtil.isSecureUrl(request, InsecureUrls.GUEST_URLS)) {
					PHRContextHolder.clientId(this.clientId);
				}

			}
			if (!StringUtils.hasLength(PHRContextHolder.authrizationToken())
					&& InsecureUrls.HEALTH_ID_URLS.contains(relativePath)) {
				PHRContextHolder.authrizationToken(gatewayTokenConfig.fetchGatewayToken());
			}
			chain.doFilter(request, response);
		} catch (CaptchaCodeValidationException e) {
			log.debug("CaptchaCodeValidationException: ", e);
			ErrorDetails errorDetails = ErrorDetails.builder().code(e.getCode().code()).message(e.getMessage()).build();
			ApiError error = ApiError.builder().code(BUSINESS_EXCEPTION.code())
					.message(HttpStatus.UNPROCESSABLE_ENTITY.name()).details(Arrays.asList(errorDetails)).build();

			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			response.getWriter().write(new Gson().toJson(error));
		} catch (CaptchaSessionExpiredException e) {
			log.debug("CaptchaSessionExpiredException: ", e);
			ErrorDetails errorDetails = ErrorDetails.builder().code(e.getCode().code()).message(e.getMessage()).build();
			ApiError error = ApiError.builder().code(UNAUTHORIZED.code()).message(HttpStatus.UNAUTHORIZED.name())
					.details(Arrays.asList(errorDetails)).build();

			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write(new Gson().toJson(error));
		}
		finally
	{
		if (!excludeUris.contains(request.getRequestURI())) {
			System.out.println(request.getRequestURI());
			Long endTime = System.nanoTime();

			BigDecimal divisor = new BigDecimal("1000000000");
			BigDecimal diff = new BigDecimal(String.valueOf(endTime - startTime));
			BigDecimal result = diff.divide(divisor);
			log.debug("[{}] - Http Request Completed ", result);
			ThreadContext.remove("request.id");
			ThreadContext.remove("endpoint");
		}
	}
	}

	private UsernamePasswordAuthenticationToken populateAuthentication(HttpServletRequest request, String clientId,
			String xhipId) {
		return populateAuthentication(request, clientId, null, null, xhipId, false);
	}

	private UsernamePasswordAuthenticationToken populateAuthentication(HttpServletRequest request, String clientId,
			String keycloakAccessToken, String keycloakRefreshToken, String xhipId, Boolean isCaptch) {

		SecurityClientContext clientContext = SecurityClientContext.of(clientId, keycloakAccessToken,
				keycloakRefreshToken, xhipId, isCaptch);
		clientContext.setClientIp(GeneralUtils.getClientIP(request));
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				clientContext, null, null);
		usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return usernamePasswordAuthenticationToken;
	}

	/**
	 * Validate if Captcha is correct. Do not store Captcha for search so that it
	 * requires Captcha everytime. For /v1/auth/init call we will only allow 1 call
	 * per /v1/auth/init call. However you are allowed to use this Captcha for
	 * subsequent calls i.e. confirmLogin.
	 * 
	 * @param token
	 * @return
	 */
	private boolean doesCaptchaTokenValid(String token) {
		// String uri = request.getRequestURI();
		String captchaCacheKey = CAPTCHA_TOKEN_PREFIX.concat(PHRIdUtils.checksum(token));
		String cachedCaptchaTokenExpiry = redisCacheService.get(captchaCacheKey);
		boolean isValid = false;

		// Validate CAPTCHA(if exist in local cache) expire time.
		if (!ObjectUtils.isEmpty(cachedCaptchaTokenExpiry)) {
			isValid = System.currentTimeMillis() < Long.valueOf(cachedCaptchaTokenExpiry);
			if (isValid) {
				isValid = captchaSecurityService.validateSessionCaptcha(token, USER_CAPTCHA_EXPIRY_MINUTES);
			}
		} else {
			// isValid = captchaService.isCaptchaValid(token);
			isValid = captchaSecurityService.validateSessionCaptcha(token, USER_CAPTCHA_EXPIRY_MINUTES);
			// If Google server validate the CAPTCHA token then only store to REDIS CACHE.
			if (isValid) {
				long tokenExpiryTime = PHRIdUtils.expiryTime(USER_CAPTCHA_EXPIRY_MINUTES, ChronoUnit.MINUTES);
				// Store this valid token into Cache.
				redisCacheService.put(captchaCacheKey, String.valueOf(tokenExpiryTime));
				PHRContextHolder.captchaTokenExpiryTime(tokenExpiryTime);
			}
		}
		if (isValid) {
			PHRContextHolder.captchaTokenChecksum(captchaCacheKey);
		}
		return isValid;
	}

	/**
	 * In order to handle brute-force attack on system, we are rate limiting APIs
	 * based on token. It will create checksum based on token and API url then it
	 * will store calls by this checksum.
	 */
	private void handleBruteForceAttack(String token) {
		String uri = request.getRequestURI().substring(7);
		String checksum = PHRIdUtils.checksum(token + uri);
		String value = redisCacheService.get(checksum);

		if (ObjectUtils.isEmpty(value)) {
			redisCacheService.put(checksum, System.currentTimeMillis() + "-1");
		} else {
			String[] values = value.split("-");
			Long createdTime = Long.valueOf(values[0]);
			Integer frequency = Integer.valueOf(values[1]);

			if (timeDiffInSec(createdTime) < BRUTE_FORCE_ALLOWED_TIME_IN_SEC) {
				if (frequency <= BRUTE_FORCE_ALLOWED_FREQUENCY) {
					redisCacheService.put(checksum, createdTime + "-" + (frequency + 1));
				} else {
					redisCacheService.remove(CAPTCHA_TOKEN_PREFIX.concat(PHRIdUtils.checksum(token)));
					throw new BadCredentialsException("Multiple requests are not allowed.");
				}
			} else {
				redisCacheService.put(checksum, System.currentTimeMillis() + "-1");
			}
			if (log.isDebugEnabled()) {
				log.debug("Checksum : {} and frequency : {}", checksum, frequency);
			}
		}
	}

	private long timeDiffInSec(long createdTime) {
		return (System.currentTimeMillis() - createdTime) / 1000;
	}

}
