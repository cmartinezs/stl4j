package cl.cmartinezs.stl4j.task.utils.predicate;

import cl.cmartinezs.stl4j.task.TaskStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskStatusPredicateFactory {

    public static Predicate<TaskStatus> isCreated() {
        return taskStatus -> taskStatus.equals(TaskStatus.CREATED);
    }

    public static Predicate<TaskStatus> isReady() {
        return taskStatus -> taskStatus.equals(TaskStatus.READY);
    }

    public static Predicate<TaskStatus> isWaiting() {
        return taskStatus -> taskStatus.equals(TaskStatus.WAITING);
    }

    public static Predicate<TaskStatus> isExecuting() {
        return taskStatus -> taskStatus.equals(TaskStatus.EXECUTING);
    }

    public static Predicate<TaskStatus> isSuccess() {
        return taskStatus -> taskStatus.equals(TaskStatus.SUCCESS);
    }

    public static Predicate<TaskStatus> isFailed() {
        return taskStatus -> taskStatus.equals(TaskStatus.FAILED);
    }

    public static Predicate<TaskStatus> isEnded() {
        return taskStatus -> isSuccess().or(isFailed()).test(taskStatus);
    }

    public static Predicate<TaskStatus> isStarted() {
        return taskStatus -> isCreated().negate().and(isEnded().negate()).test(taskStatus);
    }
}
