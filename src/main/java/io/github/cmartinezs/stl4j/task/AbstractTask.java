package io.github.cmartinezs.stl4j.task;

import io.github.cmartinezs.stl4j.task.utils.consumer.TaskConsumerFactory;
import io.github.cmartinezs.stl4j.task.utils.exception.TaskException;
import io.github.cmartinezs.stl4j.task.utils.function.TaskExceptionFunctionFactory;
import io.github.cmartinezs.stl4j.task.utils.predicate.TaskPredicateFactory;
import io.github.cmartinezs.stl4j.task.utils.supplier.BooleanSupplierFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An abstract implementation of a {@link Task}
 *
 * @since 0.1.0
 * @author Carlos
 * @version 1.0
 * @see Task
 */
@Slf4j
public abstract class AbstractTask implements Task {

    /** The task time pattern. */
    public static final String TASK_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";

    /** The task time formatter. */
    public static final DateTimeFormatter TASK_TIME_FORMATER = DateTimeFormatter.ofPattern(TASK_TIME_PATTERN);

    /** The task name. */
    private final String name;

    /** The task status. */
    private TaskStatus status = TaskStatus.CREATED;

    /** The task itself that supplies a Boolean result associated with the success or failure of the execution.
     * By default it corresponds to the supply of the Boolean associated with the successful result. */
    private BooleanSupplier self = BooleanSupplierFactory._true();

    /** The task predicate to determine if an exception should be thrown when there is an error in the execution
     * of the task. The default  value is represented by a false task predicate. */
    private Predicate<Task> throwOnError = TaskPredicateFactory._false();

    /** The task exception function that processes this same task to return a task exception associated with itself.
     * By default this function performs only and directly the return of what is described above. */
    private Function<Task, TaskException> exceptionOnError = TaskExceptionFunctionFactory.task();

    /** The task consumer that is accepted when the boolean supplier {@code self} returns {@code false}.
     * By default it is a consumer that performs an empty action. */
    private Consumer<Task> error = TaskConsumerFactory.empty();

    /** The task consumer that is accepted when the boolean supplier {@code self} returns {@code true}.
     * By default it is a consumer that performs an empty action. */
    private Consumer<Task> success = TaskConsumerFactory.empty();

    protected AbstractTask(@NonNull String name) {
        this.name = name;
        this.setSelf(this.selfRegister());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskStatus getStatus() {
        return this.status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatus(@NonNull TaskStatus status) {
        this.status = status;
    }

    /**
     * Perform the execution of this task. Assign {@link TaskStatus#EXECUTING} as the initial state to the task.
     * Then it gets the Boolean supply of {@link this#self}, if this is {@code true} it executes the
     * {@code onSuccess} method, otherwise it executes the {@code onError} method,
     * where additionally, if an exception has been configured it will be thrown this point.
     *
     * @see this#executeSelf()
     * @see this#onSuccess()
     * @see this#onError()
     *
     * @throws TaskException a task exception
     */
    @Override
    public void execute() throws TaskException {
        log.debug("{}: Starting task", getName());
        this.setStatus(TaskStatus.EXECUTING);
        if(this.executeSelf()){
            this.onSuccess();
        } else {
            this.onError();
        }
        log.debug("{}: Finishing task", this.getName());
    }

    /**
     * Method to be used when the boolean supplier {@link this#self} returns a {@code false} boolean. Assign
     * {@link TaskStatus#FAILED} as the state to the task. Accept the consumer {@link this#error}. Then it tests
     * the predicate {@code throwOnError} to decide if it should throw the exception to apply in
     * {@code exceptionOnError} method.
     *
     * @see this#throwOnError
     * @see this#exceptionOnError
     *
     * @throws TaskException a task exception
     */
    private void onError() throws TaskException {
        log.debug("{}: Failed task", this.getName());
        this.setStatus(TaskStatus.FAILED);
        this.error.accept(this);
        if (this.throwOnError.test(this)) {
            log.debug("{}: Throw exception task", this.getName());
            throw exceptionOnError.apply(this);
        }
    }

    /**
     * Method to be used when the boolean supplier {@link this#self} returns a {@code true} boolean. Assign
     * {@link TaskStatus#FAILED} as the state to the task. Accept the consumer {@link this#success}.
     */
    private void onSuccess() {
        log.debug("{}: Success task", this.getName());
        this.setStatus(TaskStatus.SUCCESS);
        this.success.accept(this);
    }

    /**
     * Gets the boolean supplied by {@code self}
     *
     * @see this#self
     *
     * @return the boolean supplied
     */
    private boolean executeSelf() {
        log.debug("{}: Starting task at {}", this.getName(), TASK_TIME_FORMATER.format(LocalDateTime.now()));
        boolean selfAsBoolean = this.self.getAsBoolean();
        log.debug("{}: Ending task at {}", this.getName(), TASK_TIME_FORMATER.format(LocalDateTime.now()));
        return selfAsBoolean;
    }

    /**
     * Sets the boolean supplier
     *
     * @see this#self
     *
     * @param self the boolean supplier
     */
    private void setSelf(@NonNull BooleanSupplier self) {
        this.self = self;
    }

    /**
     *
     * @return a boolean supplier
     */
    protected abstract BooleanSupplier selfRegister();

    /**
     * {@inheritDoc}
     */
    @Override
    public void setThrowOnError(@NonNull Predicate<Task> throwOnError) {
        this.throwOnError = throwOnError;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExceptionOnError(@NonNull Function<Task, TaskException> exceptionOnError) {
        this.exceptionOnError = exceptionOnError;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setError(@NonNull Consumer<Task> error) {
        this.error = error;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSuccess(@NonNull Consumer<Task> success) {
        this.success = success;
    }
}
