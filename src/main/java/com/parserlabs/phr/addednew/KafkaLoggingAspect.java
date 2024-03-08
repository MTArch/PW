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


@Aspect
@Component
@Log4j2
public class KafkaLoggingAspect {

    @Pointcut(value = "target(org.springframework.kafka.core.KafkaTemplate)")
    public void anyKafkaImplementor() {
    }

    @Around(value = "anyKafkaImplementor()")
    public Object logKafkaInvocationTime(ProceedingJoinPoint point) throws Throwable {

        Long startTime = System.nanoTime();
        Object next = point.proceed();
        Long endTime = System.nanoTime();
        BigDecimal divisor = new BigDecimal("1000000000");
        BigDecimal diff = new BigDecimal(String.valueOf(endTime - startTime));
        BigDecimal result = diff.divide(divisor);
        log.info("[{}] - [{}] - kafka invocation time", result,
                point.getTarget().getClass().getInterfaces()[0].getName() + "." + point.getSignature().getName());
        return next;
    }

    @AfterThrowing(pointcut = "target(org.springframework.kafka.core.KafkaTemplate)", throwing = "ex")
    public void handleKafkaError(JoinPoint joinPoint, Throwable ex) {
        log.error("Error in retrieving Kafka aspect: {}", ex.getMessage());
    }
}
