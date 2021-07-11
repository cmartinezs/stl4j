package cl.cmartinezs.stl4j.task.chain;

import cl.cmartinezs.stl4j.example.ErrorTaskChain;
import cl.cmartinezs.stl4j.example.SuccessTaskChain;
import cl.cmartinezs.stl4j.example.BooleanTask;
import cl.cmartinezs.stl4j.task.Task;
import cl.cmartinezs.stl4j.task.TaskStatus;
import cl.cmartinezs.stl4j.task.utils.consumer.TaskExceptionConsumerFactory;
import cl.cmartinezs.stl4j.task.utils.exception.TaskException;
import cl.cmartinezs.stl4j.task.utils.predicate.TaskPredicateFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskChainTest {
    private static final String SIMPLE_TASK_NAME = "simpleTask";

    @Test
    void taskChain_propagateChainException_null() {
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertThrows(NullPointerException.class, () -> taskChain.setPropagateChainException(null), "The execution should have ended in IllegalArgumentException");
    }

    @Test
    void taskChain_failOnReplaceActualNextTask_null() {
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertThrows(NullPointerException.class, () -> taskChain.setFailOnReplaceActualNextTask(null), "The execution should have ended in IllegalArgumentException");
    }

    @Test
    void taskChain_executeNext_null() {
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertThrows(NullPointerException.class, () -> taskChain.setExecuteNext(null), "The execution should have ended in IllegalArgumentException");
    }

    @Test
    void taskChain_catchChainTaskException_null() {
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertThrows(NullPointerException.class, () -> taskChain.setCatchChainTaskException(null), "The execution should have ended in IllegalArgumentException");
    }

    @Test
    void taskChain_setNext() {
        Task task = new BooleanTask(SIMPLE_TASK_NAME);
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertDoesNotThrow(() -> taskChain.assignNextTask(task), "An exception occurred");
        Assertions.assertEquals(TaskStatus.WAITING, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(task, taskChain.getNextTask(), "It must be the same task");
    }

    @Test
    void taskChain_setNext_null() {
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertThrows(NullPointerException.class, () -> taskChain.assignNextTask(null), "The execution should have ended in IllegalArgumentException");
    }

    @Test
    void taskChain_setNext_same() {
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskChain.assignNextTask(taskChain), "The execution should have ended in IllegalArgumentException");
    }

    @Test
    void taskChain_setNext_continueOnReplace() {
        Task task = new BooleanTask(SIMPLE_TASK_NAME);
        Task task2 = new BooleanTask(SIMPLE_TASK_NAME + "_2");
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");

        Assertions.assertDoesNotThrow(() -> taskChain.assignNextTask(task), "An exception occurred");
        Assertions.assertDoesNotThrow(() -> taskChain.assignNextTask(task2), "An exception occurred");
    }

    @Test
    void taskChain_setNext_failOnReplace() {
        Task task = new BooleanTask(SIMPLE_TASK_NAME);
        Task task2 = new BooleanTask(SIMPLE_TASK_NAME + "_2");
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        taskChain.setFailOnReplaceActualNextTask(TaskPredicateFactory._true());

        Assertions.assertDoesNotThrow(() -> taskChain.assignNextTask(task), "An exception occurred");
        Assertions.assertThrows(TaskException.class, () -> taskChain.assignNextTask(task2), "The execution should have ended in TaskException");
    }

    @Test
    void taskChain_setNext_otherTaskChain() {
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        TaskChain taskChain2 = new SuccessTaskChain("aTaskChain_2");
        Task task = new BooleanTask(SIMPLE_TASK_NAME);

        Assertions.assertDoesNotThrow(() -> taskChain.assignNextTask(task), "An exception occurred");
        Assertions.assertDoesNotThrow(() -> taskChain.assignNextTask(taskChain2), "An exception occurred");
        Assertions.assertEquals(TaskStatus.WAITING, taskChain2.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(taskChain2, taskChain.getNextTask(), "It must be the same task");
    }

    @Test
    void taskChain_execute() {
        Task task = new BooleanTask(SIMPLE_TASK_NAME);
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertDoesNotThrow(() -> taskChain.assignNextTask(task), "An exception occurred");
        Assertions.assertDoesNotThrow(taskChain::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.SUCCESS, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskChain.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskChain_execute_error() {
        TaskChain taskChain = new ErrorTaskChain("aTaskChain");
        taskChain.setThrowOnError(TaskPredicateFactory._false());
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
        Task task = new BooleanTask(SIMPLE_TASK_NAME);
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        taskChain.setExecuteNext(TaskPredicateFactory._false());
        Assertions.assertDoesNotThrow(() -> taskChain.assignNextTask(task), "An exception occurred");
        Assertions.assertDoesNotThrow(taskChain::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.WAITING, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskChain.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskChain_execute_nextException() {
        Task task = new BooleanTask(SIMPLE_TASK_NAME, false);
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        Assertions.assertDoesNotThrow(() -> taskChain.assignNextTask(task), "An exception occurred");
        Assertions.assertDoesNotThrow(taskChain::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskChain.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskChain_execute_nextException_propagate() {
        Task task = new BooleanTask(SIMPLE_TASK_NAME, false);
        task.setThrowOnError(TaskPredicateFactory._true());
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        taskChain.setPropagateChainException(TaskPredicateFactory._true());
        Assertions.assertDoesNotThrow(() -> taskChain.assignNextTask(task), "An exception occurred");
        Assertions.assertThrows(TaskException.class, taskChain::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.FAILED, taskChain.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskChain_execute_nextException_catch() {
        Task task = new BooleanTask(SIMPLE_TASK_NAME, false);
        task.setThrowOnError(TaskPredicateFactory._true());
        TaskChain taskChain = new SuccessTaskChain("aTaskChain");
        taskChain.setCatchChainTaskException(TaskExceptionConsumerFactory.empty());
        Assertions.assertDoesNotThrow(() -> taskChain.assignNextTask(task), "An exception occurred");
        Assertions.assertDoesNotThrow(taskChain::execute, "An exception occurred");
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskChain.getStatus(), "The final status of the task does not correspond");
    }
}
