package cl.cmartinezs.stl4j.task.utils.function;

import cl.cmartinezs.stl4j.task.Task;
import cl.cmartinezs.stl4j.task.utils.exception.TaskException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskExceptionFunctionFactory {

    public static Function<Task, TaskException> task() {
        return TaskException::new;
    }
}
