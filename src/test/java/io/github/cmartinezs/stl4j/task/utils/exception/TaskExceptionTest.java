package io.github.cmartinezs.stl4j.task.utils.exception;

import io.github.cmartinezs.stl4j.example.BooleanTask;
import io.github.cmartinezs.stl4j.task.Task;
import io.github.cmartinezs.stl4j.task.utils.predicate.TaskPredicateFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskExceptionTest {
    @Test
    void taskException_task() {
        Task task = new BooleanTask("aSimpleTask", false);
        task.setThrowOnError(TaskPredicateFactory._true());
        Task task1 = Assertions
                .assertThrows(TaskException.class, task::execute, "The execution should have ended in TaskException")
                .getTask();
        Assertions.assertEquals(task1, task, "Must be the same");
    }
}
