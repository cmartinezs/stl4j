package cl.cmartinezs.stl4j.task;

import cl.cmartinezs.stl4j.task.utils.consumer.TaskConsumerFactory;
import cl.cmartinezs.stl4j.task.utils.exception.TaskException;
import cl.cmartinezs.stl4j.task.utils.function.TaskExceptionFunctionFactory;
import cl.cmartinezs.stl4j.task.utils.predicate.TaskPredicateFactory;
import cl.cmartinezs.stl4j.task.utils.supplier.BooleanSupplierFactory;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An abstract implementation of a task
 *
 * @since 0.1.0
 * @author Carlos
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractTask implements Task {

    @Getter @NonNull
    private final String name;

    @Getter @Setter @NonNull
    private TaskStatus status = TaskStatus.CREATED;

    @Setter(AccessLevel.PROTECTED) @NonNull
    private transient BooleanSupplier self = BooleanSupplierFactory._true();

    @Setter(onMethod=@__({@Override})) @NonNull
    private transient Predicate<Task> throwOnError = TaskPredicateFactory._false();

    @Setter(onMethod=@__({@Override})) @NonNull
    private transient Function<Task, TaskException> exceptionOnError = TaskExceptionFunctionFactory.task();

    @Setter(onMethod=@__({@Override})) @NonNull
    private transient Consumer<Task> error = TaskConsumerFactory.empty();

    @Setter(onMethod=@__({@Override})) @NonNull
    private transient Consumer<Task> success = TaskConsumerFactory.empty();

    @Override
    public void execute() throws TaskException {
        log.debug("{}: Starting task", getName());
        this.setStatus(TaskStatus.EXECUTING);
        if(this.onSelf()){
            log.debug("{}: Success task", this.getName());
            this.setStatus(TaskStatus.SUCCESS);
            this.onSuccess();
        } else {
            log.debug("{}: Failed task", this.getName());
            this.setStatus(TaskStatus.FAILED);
            this.onError();
            if (this.throwOnError()) {
                log.debug("{}: Throw exception task", this.getName());
                throw this.exceptionOnError();
            }
        }
        log.debug("{}: Finishing task", this.getName());
    }

    private boolean throwOnError() {
        return this.throwOnError.test(this);
    }

    private TaskException exceptionOnError()  {
        return exceptionOnError.apply(this);
    }

    private void onError() {
        this.error.accept(this);
    }

    private void onSuccess() {
        this.success.accept(this);
    }

    private boolean onSelf() {
        return this.self.getAsBoolean();
    }
}
