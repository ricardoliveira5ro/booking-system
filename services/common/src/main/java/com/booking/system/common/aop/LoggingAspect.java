package com.booking.system.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

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
        Object[] filteredArgs = filterSensitiveArgumentsByName(signature, args);

        log.info("[{}] - {} - called with arguments: {}", className, methodName, Arrays.toString(filteredArgs));

        try {
            Object result = joinPoint.proceed();

            boolean skipReturnLog = signature.getMethod().isAnnotationPresent(NoLogReturn.class);

            if (skipReturnLog)
                log.info("[{}] - {} - completed successfully", className, methodName);
            else
                log.info("[{}] - {} - returned: {}", className, methodName, result);

            return result;
        } catch (Throwable ex) {
            log.error("[{}] - {} - threw exception: {}", className, methodName, ex.getMessage(), ex);
            throw ex;
        }
    }

    private Object[] filterSensitiveArgumentsByName(MethodSignature signature, Object[] args) {
        String[] parameterNames = signature.getParameterNames();
        Object[] filtered = new Object[args.length];

        Set<String> sensitiveNames = Set.of("password", "cancelKey", "token", "secret", "apiKey", "privateKey");

        for (int i = 0; i < args.length; i++) {
            String paramName = i < parameterNames.length ? parameterNames[i].toLowerCase() : "";
            filtered[i] = sensitiveNames.stream().anyMatch(paramName::contains) ? "[REDACTED]" : args[i];
        }

        return filtered;
    }
}
