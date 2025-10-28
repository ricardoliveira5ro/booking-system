package com.booking.system.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerClassMethods() {}

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceClassMethods() {}

    @Around("controllerClassMethods() || serviceClassMethods()")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        log.info("[{}] - {} - called with arguments: {}", className, methodName, Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();
            log.info("[{}] - {} - returned: {}", className, methodName, result);
            return result;
        } catch (Throwable ex) {
            log.error("[{}] - {} - threw exception: {}", className, methodName, ex.getMessage(), ex);
            throw ex;
        }
    }
}
