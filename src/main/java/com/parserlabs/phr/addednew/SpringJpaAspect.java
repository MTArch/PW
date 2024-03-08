package com.parserlabs.phr.addednew;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;

@Component
@Aspect
@Log4j2
public class SpringJpaAspect {

    @Pointcut(value = "target(org.springframework.data.jpa.repository.JpaRepository)")
    public void anyJpaImplementor() {
    }

    @Around(value = "anyJpaImplementor()")
    public Object logJpaInvocationTime(ProceedingJoinPoint point) throws Throwable {
        Long startTime = System.nanoTime();
        Object next = point.proceed();
        Long endTime = System.nanoTime();
        BigDecimal divisor = new BigDecimal("1000000000");
        BigDecimal diff = new BigDecimal(String.valueOf(endTime - startTime));
        BigDecimal result = diff.divide(divisor);
        log.info("[{}] - [{}] - Database invocation time", result,
                point.getTarget().getClass().getInterfaces()[0].getName() + "." + point.getSignature().getName());
        return next;
    }

    @AfterThrowing(pointcut = "target(org.springframework.data.jpa.repository.JpaRepository)", throwing = "ex")
    public void handleJpaError(JoinPoint joinPoint, Throwable ex) {
        log.error("Error in retrieving the Aspect: {}", ex.getMessage());
    }
}
