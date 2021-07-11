package cl.cmartinezs.stl4j.task.utils.exception;

import cl.cmartinezs.stl4j.task.Task;
import lombok.Getter;

public class TaskException extends Exception {
    @Getter
    private final Task task;
    public TaskException(Task task) {
        this(task, "An exception has occurred");
    }

    public TaskException(Task task, String message) {
        super(String.format("Task %s: %s", task.getName(), message));
        this.task = task;
    }
}
