package cl.cmartinezs.stl4j.example;

import cl.cmartinezs.stl4j.task.AbstractTask;
import lombok.SneakyThrows;

public class SelfNullTask extends AbstractTask {

    public SelfNullTask(String name) {
        this(name, true);
    }

    public SelfNullTask(String name, boolean executionResult) {
        super(name);
        this.setSelf(null);
    }

    @SneakyThrows
    @Override
    public void execute() {
        super.execute();
    }
}
