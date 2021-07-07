package cl.cmartinezs.stl4j.task;

import cl.cmartinezs.stl4j.example.SimpleTaskTest;
import cl.cmartinezs.stl4j.task.exception.TaskException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    private static final String SIMPLE_TASK_NAME = "simpleTask";

    @Test
    void task_name() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME);
        Assertions.assertEquals(SIMPLE_TASK_NAME, task.getName(), "The task name is not correct");
    }

    @Test
    void task_initialStatus() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME);
        Assertions.assertEquals(TaskStatus.CREATED, task.getStatus(), "The initial status of the task does not correspond");
    }

    @Test
    void task_successExecution() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME);
        Assertions.assertDoesNotThrow(task::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.SUCCESS, task.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void task_errorExecution() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME, false);
        Assertions.assertDoesNotThrow(task::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void task_errorExecutionWithThrow() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME, false);
        task.setThrowOnError(true);
        Assertions.assertThrows(TaskException.class, task::execute, "The execution should have ended in TaskException");
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
    }
}
