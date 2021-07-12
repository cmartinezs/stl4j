package io.github.cmartinezs.stl4j.utils.predicate;

import io.github.cmartinezs.stl4j.example.BooleanTask;
import io.github.cmartinezs.stl4j.task.Task;
import io.github.cmartinezs.stl4j.task.TaskStatus;
import io.github.cmartinezs.stl4j.task.utils.predicate.TaskPredicateFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskPredicateFactoryTest {
    @Test
    void task_isNull_false() {
        Assertions.assertFalse(TaskPredicateFactory.isNull().test(new BooleanTask("aSimpleTask")));
    }

    @Test
    void task_isNull_true() {
        Assertions.assertTrue(TaskPredicateFactory.isNull().test(null));
    }

    @Test
    void task_isNonNull_true() {
        Assertions.assertTrue(TaskPredicateFactory.isNonNull().test(new BooleanTask("aSimpleTask")));
    }

    @Test
    void task_isNonNull_false() {
        Assertions.assertFalse(TaskPredicateFactory.isNonNull().test(null));
    }

    @Test
    void task_isCreated_true() {
        Assertions.assertTrue(TaskPredicateFactory.isCreated().test(new BooleanTask("aSimpleTask")), "The status of the task does not correspond");
    }

    @Test
    void task_isCreated_false() {
        Task aSimpleTask = new BooleanTask("aSimpleTask");
        Assertions.assertDoesNotThrow(aSimpleTask::execute, "An exception occurred");
        Assertions.assertFalse(TaskPredicateFactory.isCreated().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isReady_true() {
        Task aSimpleTask = new BooleanTask("aSimpleTask");
        aSimpleTask.setStatus(TaskStatus.READY);
        Assertions.assertTrue(TaskPredicateFactory.isReady().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isReady_false() {
        Assertions.assertFalse(TaskPredicateFactory.isReady().test(new BooleanTask("aSimpleTask")), "The status of the task does not correspond");
    }

    @Test
    void task_isWaiting_true() {
        Task aSimpleTask = new BooleanTask("aSimpleTask");
        aSimpleTask.setStatus(TaskStatus.WAITING);
        Assertions.assertTrue(TaskPredicateFactory.isWaiting().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isWaiting_false() {
        Assertions.assertFalse(TaskPredicateFactory.isWaiting().test(new BooleanTask("aSimpleTask")), "The status of the task does not correspond");
    }

    @Test
    void task_isExecuting_true() {
        Task aSimpleTask = new BooleanTask("aSimpleTask");
        aSimpleTask.setStatus(TaskStatus.EXECUTING);
        Assertions.assertTrue(TaskPredicateFactory.isExecuting().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isExecuting_false() {
        Assertions.assertFalse(TaskPredicateFactory.isExecuting().test(new BooleanTask("aSimpleTask")), "The status of the task does not correspond");
    }

    @Test
    void task_isSuccess_true() {
        Task aSimpleTask = new BooleanTask("aSimpleTask");
        Assertions.assertDoesNotThrow(aSimpleTask::execute, "An exception occurred");
        Assertions.assertTrue(TaskPredicateFactory.isSuccess().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isSuccess_false() {
        Assertions.assertFalse(TaskPredicateFactory.isSuccess().test(new BooleanTask("aSimpleTask")), "The status of the task does not correspond");
    }

    @Test
    void task_isFailed_true() {
        Task aSimpleTask = new BooleanTask("aSimpleTask", false);
        Assertions.assertDoesNotThrow(aSimpleTask::execute, "An exception occurred");
        Assertions.assertTrue(TaskPredicateFactory.isFailed().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isFailed_false() {
        Assertions.assertFalse(TaskPredicateFactory.isFailed().test(new BooleanTask("aSimpleTask")), "The status of the task does not correspond");
    }

    @Test
    void task_isEnded_true_success() {
        Task aSimpleTask = new BooleanTask("aSimpleTask");
        Assertions.assertDoesNotThrow(aSimpleTask::execute, "An exception occurred");
        Assertions.assertTrue(TaskPredicateFactory.isEnded().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isEnded_true_failed() {
        Task aSimpleTask = new BooleanTask("aSimpleTask", false);
        Assertions.assertDoesNotThrow(aSimpleTask::execute, "An exception occurred");
        Assertions.assertTrue(TaskPredicateFactory.isEnded().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isEnded_false() {
        Assertions.assertFalse(TaskPredicateFactory.isEnded().test(new BooleanTask("aSimpleTask")), "The status of the task does not correspond");
    }

    @Test
    void task_isStarted_true_ready() {
        Task aSimpleTask = new BooleanTask("aSimpleTask", false);
        aSimpleTask.setStatus(TaskStatus.READY);
        Assertions.assertTrue(TaskPredicateFactory.isStarted().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isStarted_true_waiting() {
        Task aSimpleTask = new BooleanTask("aSimpleTask", false);
        aSimpleTask.setStatus(TaskStatus.WAITING);
        Assertions.assertTrue(TaskPredicateFactory.isStarted().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isStarted_true_executing() {
        Task aSimpleTask = new BooleanTask("aSimpleTask", false);
        aSimpleTask.setStatus(TaskStatus.EXECUTING);
        Assertions.assertTrue(TaskPredicateFactory.isStarted().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isStarted_false_created() {
        Assertions.assertFalse(TaskPredicateFactory.isStarted().test(new BooleanTask("aSimpleTask")), "The status of the task does not correspond");
    }

    @Test
    void task_isStarted_false_success() {
        Task aSimpleTask = new BooleanTask("aSimpleTask", false);
        aSimpleTask.setStatus(TaskStatus.SUCCESS);
        Assertions.assertFalse(TaskPredicateFactory.isStarted().test(aSimpleTask), "The status of the task does not correspond");
    }

    @Test
    void task_isStarted_false_failed() {
        Task aSimpleTask = new BooleanTask("aSimpleTask", false);
        aSimpleTask.setStatus(TaskStatus.FAILED);
        Assertions.assertFalse(TaskPredicateFactory.isStarted().test(aSimpleTask), "The status of the task does not correspond");
    }
}
