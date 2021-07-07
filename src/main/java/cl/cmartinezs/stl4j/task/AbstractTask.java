package cl.cmartinezs.stl4j.task;

import cl.cmartinezs.stl4j.task.exception.TaskException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * An abstract implementation of a task
 */
@Slf4j
public abstract class AbstractTask implements Task {
    @Getter
    protected final String name;
    @Getter
    @Setter
    protected TaskStatus status;
    @Setter
    private boolean throwOnError;

    protected AbstractTask(String name) {
        Objects.requireNonNull(name, "The name is required");
        this.name = name;
        this.status = TaskStatus.CREATED;
    }

    @Override
    public void execute() throws TaskException {
        AbstractTask.log.debug("{}: Iniciando tarea", getName());
        this.setStatus(TaskStatus.EXECUTING);
        if(this.executeSelf()){
            this.setStatus(TaskStatus.SUCCESS);
            this.onSuccess();
        } else {
            this.setStatus(TaskStatus.FAILED);
            this.onError();
            if (this.throwOnError) {
                this.exceptionOnError();
            }
        }
        AbstractTask.log.debug("{}: Finalizando tarea", this.getName());
    }

    protected void exceptionOnError() throws TaskException {
        throw new TaskException(this.getName() + ": La ejecución ha finalizado con error. Se lanza exception");
    }

    protected abstract boolean executeSelf() throws TaskException;

    protected void onError() {
        AbstractTask.log.debug("{}: Ejecución finalizada con error", this.getName());
    }

    protected void onSuccess() {
        AbstractTask.log.debug("{}: Ejecución realizada correctamente", this.getName());
    }
}
