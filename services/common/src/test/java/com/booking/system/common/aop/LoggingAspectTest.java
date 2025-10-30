package com.booking.system.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
class LoggingAspectTest {

    private LoggingAspect loggingAspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature signature;

    @BeforeEach
    void setUp() {
        loggingAspect = new LoggingAspect();
    }

    @Test
    void shouldLogSuccessfulBusinessOperation(CapturedOutput output) throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringType()).thenReturn(BusinessService.class);
        when(signature.getName()).thenReturn("performOperation");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"inputValue"});
        when(signature.getParameterNames()).thenReturn(new String[]{"input"});
        when(signature.getMethod())
                .thenReturn(BusinessService.class.getDeclaredMethod("performOperation", String.class));
        when(joinPoint.proceed()).thenReturn("successResult");

        Object result = loggingAspect.logControllerMethods(joinPoint);

        assertEquals("successResult", result);
        assertTrue(output.getOut().contains("called with arguments:"), "Should log method call");
        assertTrue(output.getOut().contains("returned:"), "Should log method return");
    }

    @Test
    void shouldLogWhenBusinessMethodThrowsException(CapturedOutput output) throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringType()).thenReturn(BusinessService.class);
        when(signature.getName()).thenReturn("performOperation");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"inputValue"});
        lenient().when(signature.getParameterNames()).thenReturn(new String[]{"input"});
        lenient().when(signature.getMethod())
                .thenReturn(BusinessService.class.getDeclaredMethod("performOperation", String.class));
        when(joinPoint.proceed()).thenThrow(new IllegalStateException("Business failure"));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> loggingAspect.logControllerMethods(joinPoint)
        );

        assertEquals("Business failure", exception.getMessage());
        assertTrue(output.getOut().contains("threw exception:"), "Should log exception details");
    }

    @Test
    void shouldLogCompletionNoLogReturn(CapturedOutput output) throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringType()).thenReturn(BusinessService.class);
        when(signature.getName()).thenReturn("performOperationWithoutReturnLogging");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"inputValue"});
        when(signature.getParameterNames()).thenReturn(new String[]{"input"});
        when(signature.getMethod())
                .thenReturn(BusinessService.class.getDeclaredMethod("performOperationWithoutReturnLogging", String.class));
        when(joinPoint.proceed()).thenReturn("ignoredResult");

        Object result = loggingAspect.logControllerMethods(joinPoint);

        assertEquals("ignoredResult", result);
        assertTrue(output.getOut().contains("completed successfully"), "Should log completion instead of return");
    }

    @Test
    void shouldHandleNullParameterNames(CapturedOutput output) throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringType()).thenReturn(BusinessService.class);
        when(signature.getName()).thenReturn("performOperation");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"sensitiveValue"});
        when(signature.getParameterNames()).thenReturn(null);
        when(signature.getMethod())
                .thenReturn(BusinessService.class.getDeclaredMethod("performOperation", String.class));
        when(joinPoint.proceed()).thenReturn("operationComplete");

        Object result = loggingAspect.logControllerMethods(joinPoint);

        assertEquals("operationComplete", result);
        assertTrue(output.getOut().contains("called with arguments:"), "Should log even when parameter names are null");
    }

    @Test
    void shouldRedactSensitiveParametersAndHandleExtraArguments(CapturedOutput output) throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringType()).thenReturn(BusinessService.class);
        when(signature.getName()).thenReturn("performOperation");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"myPassword123", "extraValue"});
        when(signature.getParameterNames()).thenReturn(new String[]{"password"});
        when(signature.getMethod())
                .thenReturn(BusinessService.class.getDeclaredMethod("performOperation", String.class));
        when(joinPoint.proceed()).thenReturn("success");

        Object result = loggingAspect.logControllerMethods(joinPoint);

        assertEquals("success", result);

        String logs = output.getOut();
        assertTrue(logs.contains("[REDACTED]"), "Should redact sensitive parameter values");
        assertTrue(logs.contains("extraValue"), "Should log extra args without error");
    }

    private static class BusinessService {
        public String performOperation(String input) {
            return "processed";
        }

        @NoLogReturn
        public String performOperationWithoutReturnLogging(String input) {
            return "processed";
        }
    }
}