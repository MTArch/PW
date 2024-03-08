package com.parserlabs.phr.config.security;

import static com.parserlabs.phr.constants.Constants.X_TOKEN_HEADER;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.parserlabs.phr.cache.RedisCacheService;
import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	@Lazy
	private RedisCacheService redisCacheService;

	@Value("${apis.secured: false}")
	private boolean apiSecurityApplied;

	@Override
	@Transactional
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		setUserByJwtToken(request);
		chain.doFilter(request, response);
	}

	private void setUserByJwtToken(HttpServletRequest request) {
		final String accessTokenHeader = request.getHeader(X_TOKEN_HEADER);
		String phrAddress = null;
		String accessToken = null;
		boolean isValid = false;
		String accessTokenClientId = null;

		String token = accessTokenHeader;
		
		// If URL is not secure then skip the token validation
		if (!SecurityUtil.isSecureUrl(request, InsecureUrls.INSECURE_URLS)) {
			return;
		}
		
		String getBearer=(Objects.isNull(token) || ObjectUtils.isEmpty(token.trim())?token
				:token.trim().length()>7?token.trim().substring(0,6):token);

		if (Objects.isNull(getBearer) || ObjectUtils.isEmpty(getBearer.trim()) || !"Bearer".equalsIgnoreCase(getBearer)) {
			logger.debug("JWT Token is not found in header or its malformed.");
			if (SecurityUtil.isSecureUrl(request, InsecureUrls.INSECURE_URLS)) {
				SecurityContextHolder.getContext().setAuthentication(null);
			}
			return;
		}
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		try {
			accessToken = ObjectUtils.isEmpty(accessTokenHeader) ? null : accessTokenHeader.substring(7).trim();
			phrAddress = ObjectUtils.isEmpty(accessToken) ? null : jwtTokenUtil.getUsernameFromToken(accessToken);
			accessTokenClientId = ObjectUtils.isEmpty(accessToken) ? null
					: jwtTokenUtil.getClientIDFromToken(accessToken);

		} catch (SignatureException signatureEx) {
			logger.warn("Bad JWT Token recieved." + signatureEx.toString());
			logger.warn("sourceIP: " + request.getRemoteAddr() + " Target Uri:" + request.getRequestURI());
		} catch (IllegalArgumentException e) {
			logger.warn("Unable to get JWT Token: " + e.toString());
		} catch (ExpiredJwtException e) {
			logger.warn("JWT Token has expired");
		} catch (Exception e) {
			logger.warn("Unable to parse JWT Token: " + e.toString());
		}

		// Once we get the token validate it.
		if (phrAddress != null && SecurityContextHolder.getContext().getAuthentication() != null
				&& !redisCacheService.match(accessToken)) {

			if (!ObjectUtils.isEmpty(phrAddress)) {
				UserDTO user = null;
				try {
					user = this.userService.getUser(phrAddress);
				} catch (Exception e) {
					log.error("User not found");
				}
				// if token is valid configure Spring Security to manually set
				// authentication Token will be valid
				if (Objects.nonNull(user) && jwtTokenUtil.validateToken(accessToken, user)) {
					SecurityClientContext clientContext = (SecurityClientContext) SecurityContextHolder.getContext()
							.getAuthentication().getPrincipal();
                    PHRContextHolder.phrAddress(user.getPhrAddress());
					if (!apiSecurityApplied) {
						isValid = true;
					} else if (clientContext.getClientId().equalsIgnoreCase(accessTokenClientId)) {
						isValid = true;
					}
					
					// TODO: Security true for all cases
					isValid = true;
					clientContext.setUser(user);
				}
			}
		}

		if (!isValid) { // If token is not valid then remove the authentication from Security Context
			SecurityContextHolder.getContext().setAuthentication(null);
		} else { // If token is valid then store the JWT token into a Context
			PHRContextHolder.accessToken(accessToken);
		}
	}

}
