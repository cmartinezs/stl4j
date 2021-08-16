package io.github.cmartinezs.stl4j.task;

import io.github.cmartinezs.stl4j.task.utils.exception.TaskException;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An interface for tasks
 *
 * @since 0.1.0
 * @author Carlos
 * @version 1.0
 */
public interface Task {

    /**
     * Gets the task name.
     *
     * @return the task name
     */
    String getName();

    /**
     * Gets the task status.
     *
     * @return the status
     */
    TaskStatus getStatus();

    /**
     * Sets the task status.
     *
     * @param status the task status
     */
    void setStatus(TaskStatus status);

    /**
     * Sets the task predicate to determine if an exception should be thrown when there is an error in the execution
     * of the task.
     *
     * @param throwOnError a task predicate
     */
    void setThrowOnError(Predicate<Task> throwOnError);

    /**
     * Sets the task exception function that processes this same task to return a task exception associated with itself.
     *
     * @param exceptionOnError the task exception function
     */
    void setExceptionOnError(Function<Task, TaskException> exceptionOnError);

    /**
     * Sets a task consumer when an error occurs. The task to consume corresponds to this same
     *
     * @param error the task consumer on error
     */
    void setError(Consumer<Task> error);

    /**
     * Sets a task consumer when it has completed successfully. The task to consume corresponds to this same
     *
     * @param success the task consumer on success
     */
    void setSuccess(Consumer<Task> success);

    /**
     * Execute this task
     *
     * @throws TaskException a task exception
     */
    void execute() throws TaskException;
}
