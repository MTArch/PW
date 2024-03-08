package com.parserlabs.phr.addednew;

import java.math.BigDecimal;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@Aspect
public class RedisLogging {

    @Pointcut(value = "target(org.springframework.data.redis.core.RedisOperations)")
    public void anyRedisImplementorRedisOps() {
    }

    @Around(value = "anyRedisImplementorRedisOps()")
    public Object logRedisInvocationTime(ProceedingJoinPoint point) throws Throwable {
        Long startTime = System.nanoTime();

        Object next = point.proceed();
        Long endTime = System.nanoTime();
        BigDecimal divisor = new BigDecimal("1000000000");
        BigDecimal diff = new BigDecimal(String.valueOf(endTime - startTime));
        BigDecimal result = diff.divide(divisor);
        log.info("[{}] - [{}] - Redis invocation time", result,
                point.getTarget().getClass().getName() + "." + point.getSignature().getName());
        return next;

    }

    @AfterThrowing(pointcut = "target(org.springframework.data.redis.core.RedisOperations)", throwing = "ex")
    public void handleRedisError(JoinPoint joinPoint, Throwable ex) {
        log.error("Error in Redis: {}", ex.getMessage());
    }
}
