package com.parserlabs.phr.addednew;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@Log4j2
public class SpanAdd {

    public final Tracer trace;

    public SpanAdd(Tracer trace) {
        this.trace = GlobalTracer.get();
    }

    @Pointcut(value = "@within(com.parserlabs.phr.addednew.CustomSpanned)")
    private void spanClass() {

    }

    @Pointcut(value = "execution(* *(..))")
    private void spanMethod() {

    }

    @Around(value = "spanClass() && spanMethod()")
    private Object addSpan(ProceedingJoinPoint point) throws Throwable {

        log.info("Span Start");
        MethodSignature signature = (MethodSignature) point.getSignature();
        Span span = trace.buildSpan(point.getSignature().getDeclaringType().getName() + "." + signature.getName()) .start();
        trace.activateSpan(span);
        Object next = point.proceed();
        span.finish();
        log.info("Span Finished");
        return next;
    }
}
