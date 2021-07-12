package io.github.cmartinezs.stl4j.example;

import io.github.cmartinezs.stl4j.task.AbstractTask;
import lombok.SneakyThrows;

public class BooleanTask extends AbstractTask {

    public BooleanTask(String name) {
        this(name, true);
    }

    public BooleanTask(String name, boolean executionResult) {
        super(name);
        this.setSelf(() -> executionResult);
    }

    @SneakyThrows
    @Override
    public void execute() {
        super.execute();
    }
}
