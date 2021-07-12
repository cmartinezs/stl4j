package io.github.cmartinezs.stl4j.task;

import io.github.cmartinezs.stl4j.task.utils.exception.TaskException;

import java.io.Serializable;
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
public interface Task extends Serializable {

    /**
     * Obtain tha task name
     *
     * @return the name
     */
    String getName();

    /**
     * Obtain the task status
     *
     * @return the status
     */
    TaskStatus getStatus();

    /**
     * Set the task status
     *
     * @param status the status
     */
    void setStatus(TaskStatus status);

    /**
     * Config throw on task error
     *
     * @param throwOnError a predicate indicating whether to throw an exception on error
     */
    void setThrowOnError(Predicate<Task> throwOnError);

    /**
     * Config throw on task error
     *
     * @param exceptionOnError a function that throws an exception
     */
    void setExceptionOnError(Function<Task, TaskException> exceptionOnError);

    /**
     *
     * @param error the consumer on error
     */
    void setError(Consumer<Task> error);

    /**
     *
     * @param success the consumer on success
     */
    void setSuccess(Consumer<Task> success);

    /**
     * Execute a task
     * @throws TaskException a task exception
     */
    void execute() throws TaskException;
}
