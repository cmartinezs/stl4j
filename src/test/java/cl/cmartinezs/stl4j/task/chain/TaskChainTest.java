package cl.cmartinezs.stl4j.task.chain;

import cl.cmartinezs.stl4j.example.ErrorTaskChain;
import cl.cmartinezs.stl4j.example.SuccessTaskChain;
import cl.cmartinezs.stl4j.example.SimpleTaskTest;
import cl.cmartinezs.stl4j.task.Task;
import cl.cmartinezs.stl4j.task.TaskStatus;
import cl.cmartinezs.stl4j.task.exception.TaskException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskChainTest {
    private static final String SIMPLE_TASK_NAME = "simpleTask";

    @Test
    void taskChain_setNext() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME);
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertDoesNotThrow(() -> taskChain.setNext(task), "An exception occurred");
        Assertions.assertEquals(TaskStatus.WAITING, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(task, taskChain.getNext(), "It must be the same task");
    }

    @Test
    void taskChain_setNext_null() {
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskChain.setNext(null), "The execution should have ended in IllegalArgumentException");
    }

    @Test
    void taskChain_setNext_same() {
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskChain.setNext(taskChain), "The execution should have ended in IllegalArgumentException");
    }

    @Test
    void taskChain_setNext_continueOnReplace() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME);
        Task task2 = new SimpleTaskTest(SIMPLE_TASK_NAME + "_2");
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");

        Assertions.assertDoesNotThrow(() -> taskChain.setNext(task), "An exception occurred");
        Assertions.assertDoesNotThrow(() -> taskChain.setNext(task2), "An exception occurred");
    }

    @Test
    void taskChain_setNext_failOnReplace() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME);
        Task task2 = new SimpleTaskTest(SIMPLE_TASK_NAME + "_2");
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        taskChain.setFailOnReplaceActualNextTask(true);

        Assertions.assertDoesNotThrow(() -> taskChain.setNext(task), "An exception occurred");
        Assertions.assertThrows(TaskException.class, () -> taskChain.setNext(task2), "The execution should have ended in TaskException");
    }

    @Test
    void taskChain_setNext_otherTaskChain() {
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        TaskChain taskChain2 = new SuccessTaskChain("aTaskChain_");
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME);

        Assertions.assertDoesNotThrow(() -> taskChain.setNext(task), "An exception occurred");
        Assertions.assertDoesNotThrow(() -> taskChain.setNext(taskChain2), "An exception occurred");
        Assertions.assertEquals(TaskStatus.WAITING, taskChain2.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(taskChain2, taskChain.getNext(), "It must be the same task");
    }

    @Test
    void taskChain_execute() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME);
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertDoesNotThrow(() -> taskChain.setNext(task), "An exception occurred");
        Assertions.assertDoesNotThrow(taskChain::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.SUCCESS, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskChain.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskChain_execute_error() {
        TaskChain taskChain = new ErrorTaskChain("aTaskChain");
        Assertions.assertDoesNotThrow(taskChain::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.FAILED, taskChain.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskChain_execute_withoutNext() {
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertDoesNotThrow(taskChain::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskChain.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskChain_execute_noExecuteNext() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME);
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        taskChain.setExecuteNext(false);
        Assertions.assertDoesNotThrow(() -> taskChain.setNext(task), "An exception occurred");
        Assertions.assertDoesNotThrow(taskChain::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.WAITING, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskChain.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskChain_execute_nextException() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME, false);
        task.setThrowOnError(true);
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertDoesNotThrow(() -> taskChain.setNext(task), "An exception occurred");
        Assertions.assertDoesNotThrow(taskChain::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskChain.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskChain_execute_nextException_propagate() {
        Task task = new SimpleTaskTest(SIMPLE_TASK_NAME, false);
        task.setThrowOnError(true);
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        taskChain.setPropagateChainException(true);
        Assertions.assertDoesNotThrow(() -> taskChain.setNext(task), "An exception occurred");
        Assertions.assertThrows(TaskException.class, taskChain::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.FAILED, taskChain.getStatus(), "The final status of the task does not correspond");
    }
}
