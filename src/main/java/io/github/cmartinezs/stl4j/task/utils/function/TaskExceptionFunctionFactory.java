package io.github.cmartinezs.stl4j.task.utils.function;

import io.github.cmartinezs.stl4j.task.Task;
import io.github.cmartinezs.stl4j.task.utils.exception.TaskException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskExceptionFunctionFactory {

    public static Function<Task, TaskException> task() {
        return TaskException::new;
    }
}
