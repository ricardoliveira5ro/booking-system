package com.booking.system.common.aop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(OutputCaptureExtension.class)
class LoggingUtilsTest {

    @Test
    void constructorShouldBePrivateAndThrowException() throws Exception {
        var constructor = LoggingUtils.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()), "Constructor should be private");

        constructor.setAccessible(true);

        Executable exec = constructor::newInstance;
        var ex = assertThrows(InvocationTargetException.class, exec);
        assertInstanceOf(UnsupportedOperationException.class, ex.getCause());
        assertEquals("Utility class", ex.getCause().getMessage());
    }

    @Test
    void shouldLogMethodCallWithoutException(CapturedOutput output) {
        LoggingUtils.logMethodCall(this, "arg1", 123);

        assertTrue(output.getOut().contains("called with arguments:"), "Should contain 'called with arguments'");
    }

    @Test
    void shouldLogMethodReturnWithoutException(CapturedOutput output) {
        LoggingUtils.logMethodReturn(this, "result-value");

        assertTrue(output.getOut().contains("returned:"), "Should contain 'returned:'");
    }

    @Test
    void shouldLogMethodCompletionWithoutException(CapturedOutput output) {
        LoggingUtils.logMethodCompletion(this);

        assertTrue(output.getOut().contains("completed successfully"), "Should contain 'completed successfully'");
    }
}