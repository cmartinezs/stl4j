package io.github.cmartinezs.stl4j.task.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static <T> void breakableForEach(Stream<T> stream, BiConsumer<T, Breaker> consumer, Consumer<Breaker> breakerConsumer) {
        Spliterator<T> spliterator = stream.spliterator();
        boolean hadNext = true;
        Breaker breaker = new Breaker();
        while (hadNext && !breaker.get()) {
            hadNext = spliterator.tryAdvance(elem -> consumer.accept(elem, breaker));
        }
        breakerConsumer.accept(breaker);
    }

    public static <T> void breakableForEach(Stream<T> stream, Consumer<T> consumer, Predicate<T> breakableCondition, Predicate<T> breakOnException){
        Spliterator<T> spliterator = stream.spliterator();
        boolean hadNext = true;
        AtomicBoolean aContinue = new AtomicBoolean();
        while (hadNext && aContinue.get()) {
            hadNext = spliterator.tryAdvance(element -> {
                try {
                    consumer.accept(element);
                    aContinue.set(!breakableCondition.test(element));
                } catch (Exception e) {
                    aContinue.set(!breakOnException.test(element));
                }
            });
        }
    }
}
