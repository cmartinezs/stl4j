package cl.cmartinezs.stl4j.utils;

import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class StreamUtils {
    public static class Breaker {
        private boolean shouldBreak = false;
        public void stop() {
            shouldBreak = true;
        }
        public boolean get() {
            return shouldBreak;
        }
    }

    public static <T> void breakableForEach(Stream<T> stream, BiConsumer<T, Breaker> consumer) {
        Spliterator<T> spliterator = stream.spliterator();
        boolean hadNext = true;
        Breaker breaker = new Breaker();

        while (hadNext && !breaker.get()) {
            hadNext = spliterator.tryAdvance(elem -> consumer.accept(elem, breaker));
        }
    }
}
