package cl.cmartinezs.stl4j.task.group;

import cl.cmartinezs.stl4j.task.AbstractTask;
import cl.cmartinezs.stl4j.task.Task;
import cl.cmartinezs.stl4j.task.TaskStatus;
import cl.cmartinezs.stl4j.task.exception.TaskException;
import cl.cmartinezs.stl4j.utils.StreamUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Slf4j
public final class TaskGroup extends AbstractTask {

    @Getter
    private final List<Task> tasks;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private boolean executionCompleted = true;

    @Getter(AccessLevel.PRIVATE)
    @Setter
    private Predicate<Task> stopExecutionOnException = task -> false;

    public TaskGroup(String taskName) {
        super(taskName);
        this.tasks = new ArrayList<>();
    }

    public TaskGroup(String taskName, List<Task> tasks) {
        this(taskName);
        this.addAll(tasks);
    }

    public TaskGroup add(Task task) {
        Objects.requireNonNull(task, "The task is required");
        this.setInitialStatus(task);
        this.getTasks().add(task);
        return this;
    }

    public TaskGroup addAll(Collection<Task> collection) {
        Objects.requireNonNull(collection, "The collection is required");
        if(collection.isEmpty()){
            throw new IllegalArgumentException("The collection can't be empty");
        }
        this.getTasks().addAll(collection);
        this.getTasks().forEach(this::setInitialStatus);
        return this;
    }

    public void setStopExecutionOnExceptionB(boolean stop) {
        this.setStopExecutionOnException(task -> stop);
    }

    private void setInitialStatus(Task task) {
        task.setStatus(TaskStatus.READY);
    }

    @Override
    public boolean executeSelf() {
        TaskGroup.log.debug("{}: Ejecutando grupo de {} tareas", this.getName(), tasks.size());
        this.getTasks().forEach(task -> task.setStatus(TaskStatus.WAITING));
        StreamUtils.breakableForEach(this.tasks.stream() , breakableTaskConsumer());
        TaskGroup.log.debug("{}: La ejecución del grupo de tareas a finalizado", this.getName());
        return this.isExecutionCompleted();
    }

    private BiConsumer<Task, StreamUtils.Breaker> breakableTaskConsumer() {
        return (task, breaker) -> {
            try {
                task.execute();
            } catch (TaskException e) {
                TaskGroup.log.debug("{}: Se ha capturado una exception ocurrida en la tarea {}. [{}]", this.getName(), task.getName(), e.getMessage());
                if (getStopExecutionOnException().test(task)) {
                    breaker.stop();
                }
            } finally {
                this.setExecutionCompleted(!breaker.get());
                this.setStatus(this.isExecutionCompleted() ? TaskStatus.SUCCESS : TaskStatus.FAILED);
            }
        };
    }

    public Optional<Task> getByName(String name) {
        return this.getTasks()
            .stream()
            .filter(task -> task.getName().equals(name))
            .findFirst();
    }
}
