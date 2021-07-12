package io.github.cmartinezs.stl4j.task.utils.predicate;

import io.github.cmartinezs.stl4j.task.Task;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskPredicateFactory {

    public static Predicate<Task> _false() {
        return task -> false;
    }

    public static Predicate<Task> _true() {
        return task -> true;
    }

    public static Predicate<Task> isNull() {
        return Objects::isNull;
    }

    public static Predicate<Task> isNonNull() {
        return Objects::nonNull;
    }

    public static Predicate<Task> isCreated() {
        return task -> TaskStatusPredicateFactory.isCreated().test(task.getStatus());
    }

    public static Predicate<Task> isReady() {
        return task -> TaskStatusPredicateFactory.isReady().test(task.getStatus());
    }

    public static Predicate<Task> isWaiting() {
        return task -> TaskStatusPredicateFactory.isWaiting().test(task.getStatus());
    }

    public static Predicate<Task> isExecuting() {
        return task -> TaskStatusPredicateFactory.isExecuting().test(task.getStatus());
    }

    public static Predicate<Task> isSuccess() {
        return task -> TaskStatusPredicateFactory.isSuccess().test(task.getStatus());
    }

    public static Predicate<Task> isFailed() {
        return task -> TaskStatusPredicateFactory.isFailed().test(task.getStatus());
    }

    public static Predicate<Task> isEnded() {
        return task -> TaskStatusPredicateFactory.isEnded().test(task.getStatus());
    }

    public static Predicate<Task> isStarted() {
        return task -> TaskStatusPredicateFactory.isStarted().test(task.getStatus());
    }
}
