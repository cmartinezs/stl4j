package cl.cmartinezs.stl4j.task;

import cl.cmartinezs.stl4j.example.BooleanTask;
import cl.cmartinezs.stl4j.example.SelfNullTask;
import cl.cmartinezs.stl4j.task.utils.consumer.TaskConsumerFactory;
import cl.cmartinezs.stl4j.task.utils.exception.TaskException;
import cl.cmartinezs.stl4j.task.utils.function.TaskExceptionFunctionFactory;
import cl.cmartinezs.stl4j.task.utils.predicate.TaskPredicateFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AbstractTaskTest {

    private static final String BOOLEAN_TASK_NAME = "boolean-task";
    private static final String SELF_NULL_TASK_NAME = "self-null-task";

    @Test
    void task_name() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME);
        Assertions.assertEquals(BOOLEAN_TASK_NAME, task.getName(), "The task name is not correct");
    }

    @Test
    void task_name_null() {
        Assertions.assertThrows(NullPointerException.class, () -> new BooleanTask(null), "The execution should have ended in NullPointerException");
    }

    @Test
    void task_status_null() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME);
        Assertions.assertThrows(NullPointerException.class, () -> task.setStatus(null), "The execution should have ended in NullPointerException");
    }

    @Test
    void task_self_null() {
        Assertions.assertThrows(NullPointerException.class, () -> new SelfNullTask(SELF_NULL_TASK_NAME), "The execution should have ended in NullPointerException");
    }

    @Test
    void task_throwOnError_null() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME);
        Assertions.assertThrows(NullPointerException.class, () -> task.setThrowOnError(null), "The execution should have ended in NullPointerException");
    }

    @Test
    void task_exceptionOnError() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME);
        Assertions.assertDoesNotThrow(() -> task.setExceptionOnError(TaskExceptionFunctionFactory.task()), "The execution should have ended in NullPointerException");
    }

    @Test
    void task_error() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME);
        Assertions.assertDoesNotThrow(() -> task.setError(TaskConsumerFactory.empty()), "The execution should have ended in NullPointerException");
    }

    @Test
    void task_success() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME);
        Assertions.assertDoesNotThrow(() -> task.setSuccess(TaskConsumerFactory.empty()), "The execution should have ended in NullPointerException");
    }

    @Test
    void task_exceptionOnError_null() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME);
        Assertions.assertThrows(NullPointerException.class, () -> task.setExceptionOnError(null), "The execution should have ended in NullPointerException");
    }

    @Test
    void task_error_null() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME);
        Assertions.assertThrows(NullPointerException.class, () -> task.setError(null), "The execution should have ended in NullPointerException");
    }

    @Test
    void task_success_null() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME);
        Assertions.assertThrows(NullPointerException.class, () -> task.setSuccess(null), "The execution should have ended in NullPointerException");
    }

    @Test
    void task_initialStatus() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME);
        Assertions.assertEquals(TaskStatus.CREATED, task.getStatus(), "The initial status of the task does not correspond");
    }

    @Test
    void task_successExecution() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME);
        Assertions.assertDoesNotThrow(task::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.SUCCESS, task.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void task_errorExecution() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME, false);
        task.setThrowOnError(TaskPredicateFactory._false());
        Assertions.assertDoesNotThrow(task::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void task_errorExecutionWithThrow() {
        Task task = new BooleanTask(BOOLEAN_TASK_NAME, false);
        task.setThrowOnError(TaskPredicateFactory._true());
        Assertions.assertThrows(TaskException.class, task::execute, "The execution should have ended in TaskException");
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
    }
}
