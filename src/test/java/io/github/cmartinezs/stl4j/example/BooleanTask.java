package io.github.cmartinezs.stl4j.example;

import io.github.cmartinezs.stl4j.task.AbstractTask;
import lombok.SneakyThrows;

import java.util.function.BooleanSupplier;

public class BooleanTask extends AbstractTask {

    private final boolean executionResult;

    public BooleanTask(String name) {
        this(name, true);
    }

    public BooleanTask(String name, boolean executionResult) {
        super(name);
        this.executionResult = executionResult;
    }

    @SneakyThrows
    @Override
    public void execute() {
        super.execute();
    }

    @Override
    protected BooleanSupplier selfRegister() {
        return () -> this.executionResult;
    }
}
