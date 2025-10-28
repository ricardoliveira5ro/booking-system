package com.booking.system.common.aop;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class LoggingUtils {

    public static void logMethodCall(Object context, Object... args) {
        String className = context.getClass().getSimpleName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();

        log.info("[{}] - {} - called with arguments: {}", className, methodName, Arrays.toString(args));
    }

    public static void logMethodReturn(Object context, Object result) {
        String className = context.getClass().getSimpleName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();

        log.info("[{}] - {} - returned: {}", className, methodName, result);
    }

    public static void logMethodCompletion(Object context) {
        String className = context.getClass().getSimpleName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();

        log.info("[{}] - {} - completed successfully", className, methodName);
    }
}
