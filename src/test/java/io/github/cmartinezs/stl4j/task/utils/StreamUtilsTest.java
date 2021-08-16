package io.github.cmartinezs.stl4j.task.utils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StreamUtilsTest {

    @Test
    void breakableForEach() {
        StringBuilder sb = new StringBuilder();
        StreamUtils.breakableForEach(Stream.of("Hola", "Mundo"), (s, breaker) -> sb.append(s));
        assertEquals("HolaMundo", sb.toString());
    }

    @Test
    void testBreakableForEach() {
        StringBuilder sb = new StringBuilder();
        AtomicBoolean ab = new AtomicBoolean();
        StreamUtils.breakableForEach(
            Stream.of("Hola", "Mundo")
            , (s, breaker) -> { sb.append(s); if("Hola".equals(s)) breaker.stop(); }
            , breaker -> ab.set(breaker.get())
        );
        assertEquals("Hola", sb.toString());
        assertTrue(ab.get());
    }
}