package io.github.cmartinezs.stl4j.task.chain;

import io.github.cmartinezs.stl4j.task.AbstractTask;
import io.github.cmartinezs.stl4j.task.Task;
import io.github.cmartinezs.stl4j.task.TaskStatus;
import io.github.cmartinezs.stl4j.task.utils.consumer.TaskExceptionConsumerFactory;
import io.github.cmartinezs.stl4j.task.utils.exception.TaskException;
import io.github.cmartinezs.stl4j.task.utils.predicate.TaskPredicateFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
public abstract class TaskChain extends AbstractTask {

    /** The next task. */
    private Task nextTask;

    private Predicate<Task> propagateChainException = TaskPredicateFactory._false();

    private Predicate<Task> failOnReplaceActualNextTask = TaskPredicateFactory._false();

    private Predicate<Task> executeNext = TaskPredicateFactory._true();

    private Consumer<TaskException> catchChainTaskException = TaskExceptionConsumerFactory.empty();

    protected TaskChain(String taskName) {
        super(taskName);
        this.setSuccess(task -> log.debug("{}: Execution successful", task.getName()));
        this.setError(task -> log.debug("{}: Execution failed", task.getName()));
    }

    public TaskChain assignNextTask(@NonNull Task nextTask) throws TaskException {
        sameTaskVerification(nextTask);
        failOnReplaceActualNextTask();
        taskChainVerification(nextTask);

        nextTask.setStatus(TaskStatus.WAITING);
        this.setNextTask(nextTask);
        return this;
    }

    private void sameTaskVerification(Task nextTask) {
        if(this == nextTask) {
            throw new IllegalArgumentException(String.format("%s: The next task cannot be this same", this.getName()));
        }
    }

    private void taskChainVerification(Task nextTask) throws TaskException {
        if (this.nextTask != null && nextTask instanceof TaskChain) {
            var taskChain = (TaskChain) nextTask;
            taskChain.setExecuteNext(TaskPredicateFactory._true());
            taskChain.assignNextTask(this.nextTask);
        }
    }

    private void failOnReplaceActualNextTask() throws TaskException {
        if(this.getNextTask() != null && this.failOnReplaceActualNextTask.test(this)){
            throw new TaskException(this, String.format("%s: It is not allowed to replace the next task already assigned: %s"
                    , this.getName(), this.getNextTask().getName()));
        }
    }

    @Override
    public final void execute() throws TaskException {
        super.execute();
        Task localNextTask = this.getNextTask();
        String name = this.getName();
        if (this.executeNextTask()) {
            log.debug("{}: Starting the next task: {}[{}]", name
                    , localNextTask.getClass().getSimpleName(), localNextTask.getName());
            try {
                localNextTask.execute();
            } catch (TaskException e) {
                if(this.propagateChainException()) {
                    this.setStatus(TaskStatus.FAILED);
                    throw e;
                } else {
                    this.catchChainTaskException(e);
                }
            } finally {
                log.debug("{}: Execution of the following task {} has finished", name, localNextTask.getName());
            }
        } else if (localNextTask != null) {
            log.debug("{}: This task has not allowed to execute the following task: {}", name, localNextTask.getName());
        } else {
            log.debug("{}: There is no next task to run", name);
        }
    }

    private boolean propagateChainException() {
        return this.propagateChainException.test(this.getNextTask());
    }

    private boolean executeNextTask() {
        Task localNextTask = this.getNextTask();
        return localNextTask != null && this.executeNext.test(localNextTask);
    }

    private void catchChainTaskException(TaskException e) {
        log.debug("{}: An exception occurred in task {} was caught. Error -> {}", this.getName()
                , this.getNextTask().getName(), e.getMessage());
        this.catchChainTaskException.accept(e);
    }

    public Task getNextTask() {
        return this.nextTask;
    }

    private void setNextTask(@NonNull Task nextTask) {
        this.nextTask = nextTask;
    }

    public void setPropagateChainException(@NonNull Predicate<Task> propagateChainException) {
        this.propagateChainException = propagateChainException;
    }

    public void setFailOnReplaceActualNextTask(@NonNull Predicate<Task> failOnReplaceActualNextTask) {
        this.failOnReplaceActualNextTask = failOnReplaceActualNextTask;
    }

    public void setExecuteNext(@NonNull Predicate<Task> executeNext) {
        this.executeNext = executeNext;
    }

    public void setCatchChainTaskException(@NonNull Consumer<TaskException> catchChainTaskException) {
        this.catchChainTaskException = catchChainTaskException;
    }
}
