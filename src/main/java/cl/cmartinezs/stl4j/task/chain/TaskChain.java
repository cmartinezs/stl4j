package cl.cmartinezs.stl4j.task.chain;

import cl.cmartinezs.stl4j.task.AbstractTask;
import cl.cmartinezs.stl4j.task.Task;
import cl.cmartinezs.stl4j.task.exception.TaskException;
import cl.cmartinezs.stl4j.task.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TaskChain extends AbstractTask {
    @Getter
    private Task next;

    @Getter
    @Setter
    private boolean propagateChainException;

    @Getter
    @Setter
    private boolean failOnReplaceActualNextTask;

    @Getter
    @Setter
    private boolean executeNext;

    protected TaskChain(String taskName) {
        super(taskName);
        this.executeNext = true;
    }

    public TaskChain setNext(Task next) throws TaskException {
        if(next == null) {
            throw new IllegalArgumentException(getName() + ": La siguiente tarea no puede ser nula");
        }

        if(this == next) {
            throw new IllegalArgumentException(getName() + ": La siguiente tarea no puede ser esta misma");
        }

        if(this.next != null && isFailOnReplaceActualNextTask()){
            throw new TaskException(getName() + ": No está permitido reemplazar la siguiente tarea ya asignada");
        }

        if (this.next != null && next instanceof TaskChain) {
            ((TaskChain)next).setNext(this.next);
        }
        next.setStatus(TaskStatus.WAITING);
        this.next = next;
        return this;
    }

    @Override
    public final void execute() throws TaskException {
        super.execute();
        if (this.next != null && this.isExecuteNext()) {
            TaskChain.log.debug("{}: Ejecutando siguiente la tarea de tipo {}", getName(), next.getName());
            try {
                next.execute();
            } catch (TaskException e) {
                if(isPropagateChainException()) {
                    this.setStatus(TaskStatus.FAILED);
                    throw e;
                } else {
                    catchNextTaskException(e);
                }
            } finally {
                TaskChain.log.debug("{}: Ejecución de la siguiente tarea {} ha finalizado", getName(), next.getName());
            }
        } else if (this.next != null) {
            TaskChain.log.debug("{}: Esta tarea no ha permitido ejecutar la siguiente tarea {}", getName(), next.getName());
        } else {
            TaskChain.log.debug("{}: No hay siguiente tarea para ejecutar", getName());
        }
    }

    protected void catchNextTaskException(TaskException e) {
        TaskChain.log.debug("{}: Se ha capturado una exception ocurrida en la tarea {}. Error -> {}", getName(), next.getName(), e.getMessage());
    }

    protected void onError() {
        TaskChain.log.debug("{}: Ejecución finalizada con error", getName());
    }

    protected void onSuccess() {
        TaskChain.log.debug("{}: Ejecución realizada correctamente", getName());
    }
}
