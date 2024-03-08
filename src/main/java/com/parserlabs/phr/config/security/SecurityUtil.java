package com.parserlabs.phr.config.security;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtil {

	public boolean isSecureUrl(HttpServletRequest request, List<String> insecureUrls) {
		String requestURI = request.getRequestURI();
		Boolean isSecureUrl = false;
		if (requestURI.startsWith("/api")) {

			String relativePath = request.getRequestURI().substring(4);
			isSecureUrl = !insecureUrls.contains(relativePath);
			if (isSecureUrl) {
				List<String> urls = Arrays.asList(insecureUrls.toArray(new String[1])).stream()
						.filter(url -> url.endsWith("**")).collect(Collectors.toList());

				if (Objects.nonNull(urls) && !urls.isEmpty()) {
					isSecureUrl = !urls.stream().anyMatch(url -> relativePath.startsWith(url.replace("/**", "")));
				}

			}
		}
		return isSecureUrl;
	}
	
	/**
	 * Get relative path from Request URI.
	 * This will remove NDHM or API prefix from url and the provide absolute url.
	 * @param request
	 * @return relative url to API context.
	 */
	
	public String getRelativePath(HttpServletRequest request) {
		String relativePath = "";
		String requestURI = request.getRequestURI();
		if (requestURI.startsWith("/api")) {
			relativePath = requestURI.startsWith("/ndhm")? request.getRequestURI().substring(5) : request.getRequestURI().substring(4);
		}
		
		return relativePath;
	}

}
