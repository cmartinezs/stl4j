package io.github.cmartinezs.stl4j.task.group;

import io.github.cmartinezs.stl4j.task.AbstractTask;
import io.github.cmartinezs.stl4j.task.Task;
import io.github.cmartinezs.stl4j.task.TaskStatus;
import io.github.cmartinezs.stl4j.task.utils.exception.TaskException;
import io.github.cmartinezs.stl4j.task.utils.predicate.TaskPredicateFactory;
import io.github.cmartinezs.stl4j.task.utils.supplier.BooleanSupplierFactory;
import io.github.cmartinezs.stl4j.task.utils.StreamUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @since 0.1.0
 * @author Carlos
 * @version 1.0
 */
@Slf4j
public final class TaskGroup extends AbstractTask {

    /** The task list. */
    private final List<Task> tasks;

    /** The Boolean supplier that indicates whether the group task has completed.
     * By default it supplies {@code true} */
    private BooleanSupplier executionCompleted = BooleanSupplierFactory._true();

    /** The task predicate that tests whether it should stop when an exception occurs.
     * By default the test is {@code false}. */
    private Predicate<Task> stopExecutionOnException = TaskPredicateFactory._false();

    /**
     * It initializes the task list as an empty list and records its work itself.
     *
     * @see this#selfRegister()
     *
     * @param taskName the {@code non-null} task name.
     */
    public TaskGroup(String taskName) {
        super(taskName);
        this.tasks = new ArrayList<>();
    }

    /**
     * Gets the boolean supplier to registers the group task itself. It is responsible for changing the initial state
     * to the next waiting state for all tasks. It then runs each of the tasks in the internal list through a
     * {@code breakableForEach}. Assign the status of the group task according to how the set of internal tasks ends.
     *
     * @see StreamUtils#breakableForEach(Stream, BiConsumer, Consumer)
     * @see this#breakableTaskConsumer()
     * @see this#executionCompleted()
     *
     * @return the boolean supplier
     */
    @Override
    protected BooleanSupplier selfRegister() {
        return () -> {
            log.debug("{}: Executing group of {} tasks", this.getName(), tasks.size());
            this.getTasks().forEach(task -> task.setStatus(TaskStatus.WAITING));
            StreamUtils.breakableForEach(this.tasks.stream() , breakableTaskConsumer(), breaker -> this.setExecutionCompleted(() -> !breaker.get()));
            log.debug("{}: The execution of the tasks has been completed", this.getName());
            boolean completed = this.executionCompleted();
            this.setStatus(completed ? TaskStatus.SUCCESS : TaskStatus.FAILED);
            return completed;
        };
    }

    /**
     * It initializes the task list as an empty list and records its work itself. Add the parameterized task list to
     * the already initialized empty internal list. The list cannot be null or empty.
     *
     * @see this#addAll(Collection)
     *
     * @param taskName the {@code non-null} task name.
     * @param tasks the {@code non-null} tasks list.
     */
    public TaskGroup(String taskName, @NonNull List<Task> tasks) {
        this(taskName);
        this.addAll(tasks);
    }

    /**
     * Add a task to the group's task list. Initializes the task in the initial state.
     * 
     * @see this#setInitialStatus(Task)
     * 
     * @param task a {@code non-null} task.
     * @return this task group.
     */
    public TaskGroup add(@NonNull Task task) {
        this.setInitialStatus(task);
        this.getTasks().add(task);
        return this;
    }

    /**
     * Add the parameterized task list to the already internal list. The list cannot be null or empty.
     * Initializes the tasks in the initial state.
     *
     * @see this#setInitialStatus(Task)
     *
     * @param collection a {@code non-null} tasks collection.
     * @return this task group.
     */
    public TaskGroup addAll(@NonNull Collection<Task> collection) {
        if(collection.isEmpty()){
            throw new IllegalArgumentException("The collection can't be empty");
        }
        this.getTasks().addAll(collection);
        this.getTasks().forEach(this::setInitialStatus);
        return this;
    }



    /**
     * Gets the boolean supplied by {@link this#executionCompleted}.
     *
     * @return the execution completed boolean.
     */
    private boolean executionCompleted() {
        return this.executionCompleted.getAsBoolean();
    }

    /**
     * Gets the boolean supplied by {@link this#stopExecutionOnException} testing the task.
     * @param task a task
     * @return the stop execution on exception boolean
     */
    private boolean stopExecutionOnException(Task task) {
        return this.stopExecutionOnException.test(task);
    }

    /**
     * Sets the initial status {@link TaskStatus#READY}.
     *
     * @param task a task
     */
    private void setInitialStatus(Task task) {
        task.setStatus(TaskStatus.READY);
    }

    /**
     * Gets the task-breaker (bi)consumer. Run a task. In case of exception, it tests the task predicate
     * {@link this#stopExecutionOnException} with the task that threw the exception, which if it is true,
     * stops the {@code breaker}, otherwise it ignores the exception.
     *
     * @see this#selfRegister() 
     * @see this#stopExecutionOnException(Task)
     *
     * @return the bi-consumer
     */
    private BiConsumer<Task, StreamUtils.Breaker> breakableTaskConsumer() {
        return (task, breaker) -> {
            try {
                task.execute();
            } catch (TaskException e) {
                log.debug("{}: An exception occurred in task {} was caught. [{}]", this.getName(), task.getName(), e.getMessage());
                if (this.stopExecutionOnException(task)) {
                    breaker.stop();
                }
            }
        };
    }

    /**
     * Gets the optional task by task name from the internal tasks list.
     *
     * @param name the {@code non-null} task name.
     * @return an optional task.
     */
    public Optional<Task> getByName(@NonNull String name) {
        return this.getTasks()
            .stream()
            .filter(task -> task.getName().equals(name))
            .findFirst();
    }

    /**
     * Gets the tasks list.
     *
     * @return the tasks list.
     */
    public List<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Sets a boolean supplier.
     *
     * @see this#executionCompleted
     *
     * @param executionCompleted the boolean supplier
     */
    private void setExecutionCompleted(@NonNull BooleanSupplier executionCompleted) {
        this.executionCompleted = executionCompleted;
    }

    /**
     * Sets a task predicate.
     *
     * @see this#stopExecutionOnException
     *
     * @param stopExecutionOnException the task predicate
     */
    public void setStopExecutionOnException(@NonNull Predicate<Task> stopExecutionOnException) {
        this.stopExecutionOnException = stopExecutionOnException;
    }
}
