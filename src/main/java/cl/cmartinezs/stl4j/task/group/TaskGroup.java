package cl.cmartinezs.stl4j.task.group;

import cl.cmartinezs.stl4j.task.AbstractTask;
import cl.cmartinezs.stl4j.task.Task;
import cl.cmartinezs.stl4j.task.TaskStatus;
import cl.cmartinezs.stl4j.task.utils.exception.TaskException;
import cl.cmartinezs.stl4j.task.utils.predicate.TaskPredicateFactory;
import cl.cmartinezs.stl4j.task.utils.supplier.BooleanSupplierFactory;
import cl.cmartinezs.stl4j.utils.StreamUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@Slf4j
public final class TaskGroup extends AbstractTask {

    @Getter
    private final List<Task> tasks;

    @Setter(AccessLevel.PRIVATE)
    private transient BooleanSupplier executionCompleted = BooleanSupplierFactory._true();

    @Setter @NonNull
    private transient Predicate<Task> stopExecutionOnException = TaskPredicateFactory._false();

    public TaskGroup(String taskName) {
        super(taskName);
        this.tasks = new ArrayList<>();
        this.selfRegister();
    }

    public TaskGroup(String taskName, List<Task> tasks) {
        this(taskName);
        this.addAll(tasks);
    }

    public TaskGroup add(@NonNull Task task) {
        this.setInitialStatus(task);
        this.getTasks().add(task);
        return this;
    }

    public TaskGroup addAll(@NonNull Collection<Task> collection) {
        if(collection.isEmpty()){
            throw new IllegalArgumentException("The collection can't be empty");
        }
        this.getTasks().addAll(collection);
        this.getTasks().forEach(this::setInitialStatus);
        return this;
    }

    private void selfRegister() {
        this.setSelf(() -> {
            TaskGroup.log.debug("{}: Ejecutando grupo de {} tareas", this.getName(), tasks.size());
            this.getTasks().forEach(task -> task.setStatus(TaskStatus.WAITING));
            StreamUtils.breakableForEach(this.tasks.stream() , breakableTaskConsumer());
            TaskGroup.log.debug("{}: La ejecución del grupo de tareas a finalizado", this.getName());
            return this.executionCompleted();
        });
    }

    private boolean executionCompleted() {
        return this.executionCompleted.getAsBoolean();
    }

    private void setInitialStatus(Task task) {
        task.setStatus(TaskStatus.READY);
    }

    private BiConsumer<Task, StreamUtils.Breaker> breakableTaskConsumer() {
        return (task, breaker) -> {
            try {
                task.execute();
            } catch (TaskException e) {
                TaskGroup.log.debug("{}: Se ha capturado una exception ocurrida en la tarea {}. [{}]", this.getName(), task.getName(), e.getMessage());
                if (this.stopExecutionOnException(task)) {
                    breaker.stop();
                }
            } finally {
                this.setExecutionCompleted(() -> !breaker.get());
                this.setStatus(executionCompleted() ? TaskStatus.SUCCESS : TaskStatus.FAILED);
            }
        };
    }

    private boolean stopExecutionOnException(Task task) {
        return this.stopExecutionOnException.test(task);
    }

    public Optional<Task> getByName(@NonNull String name) {
        return this.getTasks()
            .stream()
            .filter(task -> task.getName().equals(name))
            .findFirst();
    }
}
