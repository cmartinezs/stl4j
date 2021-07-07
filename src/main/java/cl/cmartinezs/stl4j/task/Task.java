package cl.cmartinezs.stl4j.task;

import cl.cmartinezs.stl4j.task.exception.TaskException;

/**
 * a interface for task
 */
public interface Task {

    /**
     * Obtain tha task name
     * @return the name
     */
    String getName();

    /**
     * Obtain the task status
     * @return the status
     */
    TaskStatus getStatus();

    /**
     * Set the task status
     * @param status
     */
    void setStatus(TaskStatus status);

    /**
     * Config throw on task error
     * @param throwOnError
     */
    void setThrowOnError(boolean throwOnError);

    /**
     * Execute a task
     * @throws TaskException
     */
    void execute() throws TaskException;
}
