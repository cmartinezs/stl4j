package cl.cmartinezs.stl4j.task.group;

import cl.cmartinezs.stl4j.example.SimpleTaskTest;
import cl.cmartinezs.stl4j.task.Task;
import cl.cmartinezs.stl4j.task.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TaskGroupTest {

    private static final String SIMPLE_TASK_NAME = "simpleTask";
    private static final String TASK_GROUP_NAME = "taskGroupTest";

    @Test
    void taskGroup_name() {
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME);
        Assertions.assertEquals(TASK_GROUP_NAME, taskGroup.getName(), "The task name is not correct");
    }

    @Test
    void taskGroup_addTask() {
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME);
        SimpleTaskTest simpleTask = new SimpleTaskTest(SIMPLE_TASK_NAME);

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
        SimpleTaskTest simpleTask = new SimpleTaskTest(SIMPLE_TASK_NAME);
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
        SimpleTaskTest simpleTask = new SimpleTaskTest(SIMPLE_TASK_NAME);

        taskGroup.addAll(Collections.singletonList(simpleTask));
        Assertions.assertEquals(TaskStatus.READY, simpleTask.getStatus(), "The initial status of the task does not correspond");

        List<Task> tasks = taskGroup.getTasks();
        Assertions.assertNotNull(tasks, "Task list can't be null");
        Assertions.assertNotEquals(0, tasks.size(), "Task group must have at least one task");

        Task task = taskGroup.getByName(SIMPLE_TASK_NAME).orElseThrow();
        Assertions.assertEquals(SIMPLE_TASK_NAME, task.getName(), "The task name is not correct");
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
        ArrayList<Task> arrayList = null;
        Assertions.assertThrows(NullPointerException.class, () -> taskGroup.addAll(arrayList), "The execution should have ended in NullPointerException");
    }

    @Test
    void taskGroup_executeSelf() {
        SimpleTaskTest simpleTask = new SimpleTaskTest(SIMPLE_TASK_NAME);
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME, Collections.singletonList(simpleTask));

        Assertions.assertDoesNotThrow(taskGroup::executeSelf, "An exception occurred");
        Assertions.assertTrue(taskGroup.executeSelf(), "The execution did not finish with the expected result");

        Task task = taskGroup.getByName(SIMPLE_TASK_NAME).orElseThrow();
        Assertions.assertEquals(TaskStatus.SUCCESS, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskGroup.getStatus(), "The final status of the task does not correspond");

    }

    @Test
    void taskGroup_executeSelf_errorTask_continueExecution() {
        SimpleTaskTest simpleTask = new SimpleTaskTest(SIMPLE_TASK_NAME, false);
        simpleTask.setThrowOnError(true);
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME, Collections.singletonList(simpleTask));

        Assertions.assertDoesNotThrow(taskGroup::executeSelf, "An exception occurred");
        Assertions.assertTrue(taskGroup.executeSelf(), "The execution did not finish with the expected result");

        Task task = taskGroup.getByName(SIMPLE_TASK_NAME).orElseThrow();
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.SUCCESS, taskGroup.getStatus(), "The final status of the task does not correspond");
    }



    @Test
    void taskGroup_executeSelf_errorTask_stopExecution() {
        SimpleTaskTest simpleTask = new SimpleTaskTest(SIMPLE_TASK_NAME, false);
        simpleTask.setThrowOnError(true);
        TaskGroup taskGroup = new TaskGroup(TASK_GROUP_NAME, Collections.singletonList(simpleTask));
        taskGroup.setStopExecutionOnExceptionB(true);

        Assertions.assertDoesNotThrow(taskGroup::executeSelf, "An exception occurred");
        Assertions.assertFalse(taskGroup.executeSelf(), "The execution did not finish with the expected result");

        Task task = taskGroup.getByName(SIMPLE_TASK_NAME).orElseThrow();
        Assertions.assertEquals(TaskStatus.FAILED, task.getStatus(), "The final status of the task does not correspond");
        Assertions.assertEquals(TaskStatus.FAILED, taskGroup.getStatus(), "The final status of the task does not correspond");
    }
}
