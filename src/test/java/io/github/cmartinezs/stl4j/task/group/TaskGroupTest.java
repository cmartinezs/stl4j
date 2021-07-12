package io.github.cmartinezs.stl4j.task.group;

import io.github.cmartinezs.stl4j.example.BooleanTask;
import io.github.cmartinezs.stl4j.task.Task;
import io.github.cmartinezs.stl4j.task.TaskStatus;
import io.github.cmartinezs.stl4j.task.utils.predicate.TaskPredicateFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

class TaskGroupTest {

    private static final String SIMPLE_TASK_NAME = "simpleTask";
    private static final String TASK_GROUP_NAME = "taskGroupTest";

    @Test
    void taskGroup_name() {
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME);
        Assertions.assertEquals(TASK_GROUP_NAME, taskGroup.getName(), "The task name is not correct");
    }

    @Test
    void taskGroup_add_null() {
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME);
        Assertions.assertThrows(NullPointerException.class, () -> taskGroup.add(null), "The execution should have ended in NullPointerException");
    }

    @Test
    void taskGroup_stopExecutionOnException_null() {
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME);
        Assertions.assertThrows(NullPointerException.class, () -> taskGroup.setStopExecutionOnException(null), "The execution should have ended in NullPointerException");
    }

    @Test
    void taskGroup_addTask() {
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME);
        BooleanTask simpleTask = new BooleanTask(SIMPLE_TASK_NAME);

        taskGroup.add(simpleTask);
        Assertions.assertEquals(TaskStatus.READY, simpleTask.getStatus(), "The initial status of the task does not correspond");

        List<Task> tasks = taskGroup.getTasks();
        Assertions.assertNotNull(tasks, "Task list can't be null");
        Assertions.assertNotEquals(0, tasks.size(), "Task group must have at least one task");

        Task task = taskGroup.getByName(SIMPLE_TASK_NAME).orElseThrow();
        Assertions.assertEquals(SIMPLE_TASK_NAME, task.getName(), "The task name is not correct");
    }

    @Test
    void taskGroup_addTasks_construct() {
        BooleanTask simpleTask = new BooleanTask(SIMPLE_TASK_NAME);
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME, Collections.singletonList(simpleTask));

        Assertions.assertEquals(TaskStatus.READY, simpleTask.getStatus(), "The initial status of the task does not correspond");

        List<Task> tasks = taskGroup.getTasks();
        Assertions.assertNotNull(tasks, "Task list can't be null");
        Assertions.assertNotEquals(0, tasks.size(), "Task group must have at least one task");

        Task task = taskGroup.getByName(SIMPLE_TASK_NAME).orElseThrow();
        Assertions.assertEquals(SIMPLE_TASK_NAME, task.getName(), "The task name is not correct");
    }

    @Test
    void taskGroup_addTasks() {
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME);
        BooleanTask simpleTask = new BooleanTask(SIMPLE_TASK_NAME);

        taskGroup.addAll(Collections.singletonList(simpleTask));
        Assertions.assertEquals(TaskStatus.READY, simpleTask.getStatus(), "The initial status of the task does not correspond");

        List<Task> tasks = taskGroup.getTasks();
        Assertions.assertNotNull(tasks, "Task list can't be null");
        Assertions.assertNotEquals(0, tasks.size(), "Task group must have at least one task");

        Task task = taskGroup.getByName(SIMPLE_TASK_NAME).orElseThrow();
        Assertions.assertEquals(SIMPLE_TASK_NAME, task.getName(), "The task name is not correct");
    }

    @Test
    void taskGroup_getByName_other() {
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME);
        BooleanTask simpleTask = new BooleanTask(SIMPLE_TASK_NAME);
        taskGroup.addAll(Collections.singletonList(simpleTask));

        Assertions.assertThrows(NoSuchElementException.class, () -> taskGroup.getByName("SIMPLE_TASK_NAME").orElseThrow(), "The execution should have ended in NoSuchElementException");
    }

    @Test
    void taskGroup_getByName_null() {
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME);
        BooleanTask simpleTask = new BooleanTask(SIMPLE_TASK_NAME);
        taskGroup.addAll(Collections.singletonList(simpleTask));

        Assertions.assertThrows(NullPointerException.class, () -> taskGroup.getByName(null), "The execution should have ended in NullPointerException");
    }

    @Test
    void taskGroup_addTasks_empty() {
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME);
        ArrayList<Task> arrayList = new ArrayList<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskGroup.addAll(arrayList), "The execution should have ended in IllegalArgumentException");
    }

    @Test
    void taskGroup_addTasks_null() {
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME);
        Assertions.assertThrows(NullPointerException.class, () -> taskGroup.addAll(null), "The execution should have ended in NullPointerException");
    }

    @Test
    void taskGroup_execute() {
        Task simpleTask = new BooleanTask(SIMPLE_TASK_NAME);
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME, Collections.singletonList(simpleTask));
        Assertions.assertDoesNotThrow(taskGroup::execute, "An exception occurred");

        Task task = taskGroup.getByName(SIMPLE_TASK_NAME).orElseThrow();
        Assertions.assertEquals(TaskStatus.SUCCESS, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskGroup.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskGroup_execute_errorTask_continueExecution_noThrow() {
        Task simpleTask = new BooleanTask(SIMPLE_TASK_NAME, false);
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME, Collections.singletonList(simpleTask));

        Assertions.assertDoesNotThrow(taskGroup::execute, "An exception occurred");

        Task task = taskGroup.getByName(SIMPLE_TASK_NAME).orElseThrow();
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskGroup.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskGroup_eexecute_errorTask_continueExecution_withThrow() {
        Task simpleTask = new BooleanTask(SIMPLE_TASK_NAME, false);
        Task simpleTask2 = new BooleanTask(SIMPLE_TASK_NAME, false);
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME).add(simpleTask).add(simpleTask2);
        simpleTask.setThrowOnError(TaskPredicateFactory._true());

        Assertions.assertDoesNotThrow(taskGroup::execute, "An exception occurred");

        Task task = taskGroup.getByName(SIMPLE_TASK_NAME).orElseThrow();
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskGroup.getStatus(), "The final status of the task does not correspond");
    }

    @Test
    void taskGroup_execute_errorTask_stopExecution() {
        Task simpleTask = new BooleanTask(SIMPLE_TASK_NAME, false);
        simpleTask.setThrowOnError(TaskPredicateFactory._true());
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME, Collections.singletonList(simpleTask));
        taskGroup.setStopExecutionOnException(TaskPredicateFactory._true());

        Assertions.assertDoesNotThrow(taskGroup::execute, "An exception occurred");

        Task task = taskGroup.getByName(SIMPLE_TASK_NAME).orElseThrow();
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.FAILED, taskGroup.getStatus(), "The final status of the task does not correspond");
    }
}
