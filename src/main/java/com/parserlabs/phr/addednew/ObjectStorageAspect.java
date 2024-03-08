package com.parserlabs.phr.addednew;

import java.math.BigDecimal;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@Log4j2
public class ObjectStorageAspect {


    @Around(value = "@annotation(com.parserlabs.phr.addednew.S3Metric)")
    private Object addSpan(ProceedingJoinPoint point)throws Throwable{

        log.info("Object Storage log started ");

        Long startTime = System.nanoTime();

        Object next = point.proceed();
        Long endTime = System.nanoTime();
        BigDecimal divisor = new BigDecimal("1000000000");
        BigDecimal diff = new BigDecimal(String.valueOf(endTime-startTime));
        BigDecimal result = diff.divide(divisor);
        log.info ("[{}] - [{}] - ObjectStorage invocation time",result,point.getTarget().getClass().getName() + "." + point.getSignature().getName());
        return next;

    }


}
