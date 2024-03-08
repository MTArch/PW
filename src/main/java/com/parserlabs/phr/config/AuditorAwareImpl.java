package com.parserlabs.phr.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;
import com.parserlabs.phr.commons.PHRContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {

		String auditor = "hpr_web";
		
		if (StringUtils.hasLength(PHRContextHolder.clientID())) {
			auditor = PHRContextHolder.clientID();
		}

		return Optional.ofNullable(auditor);
	}
}