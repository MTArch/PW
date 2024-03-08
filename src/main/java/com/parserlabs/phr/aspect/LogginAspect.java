package com.parserlabs.phr.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import com.parserlabs.phr.commons.PHRContextHolder;
import com.parserlabs.phr.utils.PHRIdUtils;
import com.parserlabs.phr.utils.PhrUtilits;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Configuration
@Slf4j
public class LogginAspect {

	private static final String AFTER_LOGS = "{} - Finished execution for the request.";
	private static final String BEFORE_LOGS = "{} - Starts execution for the request.";

	@Before("execution(* com.parserlabs.phr.controller.v1.*.*.*(..))")
	public void beforeController(JoinPoint joinPoint) {
		uniqueRequestId();
		printLogs(BEFORE_LOGS, joinPoint);
	}

	@After("execution(* com.parserlabs.phr.controller.v1.*.*.*(..))")
	public void afterController(JoinPoint joinPoint) {
		printLogs(AFTER_LOGS, joinPoint);
		PHRContextHolder.remove();

	}

	@Before("execution(* com.parserlabs.phr.service.*.*(..))")
	public void beforeService(JoinPoint joinPoint) {
		printLogs(BEFORE_LOGS, joinPoint);
	}

	@After("execution(* com.parserlabs.phr.service.*.*(..))")
	public void afterService(JoinPoint joinPoint) {
		printLogs(AFTER_LOGS, joinPoint);
	}

	private void printLogs(String message, JoinPoint joinPoint) {
		if (StringUtils.isNotBlank(PHRContextHolder.uniqueRequestId())) {
			log.info(message, PHRContextHolder.uniqueRequestId(), joinPoint.getSignature());
		} else {
			log.info(message, joinPoint.getSignature());

		}
	}

	private String phrAddress() {
		try {
			return PhrUtilits.phrAddress();
		} catch (Exception e) {
			return "";
		}
	}

	private String uniqueRequestId() {
		String uniqueRequestId = PHRContextHolder.uniqueRequestId();
		if (StringUtils.isEmpty(uniqueRequestId)) {
			uniqueRequestId = "UID:".concat(String.valueOf(Thread.currentThread().getId()))
					.concat("||TST:" + String.valueOf(System.currentTimeMillis()))
					.concat("||CID:" + PhrUtilits.clientId().toUpperCase())
					.concat("||HIP:" + PHRIdUtils.xHipId().toUpperCase()).concat("||CIP:" + PHRIdUtils.clientIp());
			try {
				uniqueRequestId = StringUtils.isEmpty(phrAddress()) ? uniqueRequestId
						: uniqueRequestId.concat("||PHRID:").concat(phrAddress());
			} catch (Exception e) {
				// Do nothing
				log.info("Exception in uniqueRequestId: {}", e.getMessage());
			}
			PHRContextHolder.uniqueRequestId(uniqueRequestId);
		}
		return uniqueRequestId;
	}

}
