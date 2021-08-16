package io.github.cmartinezs.stl4j.example;

import io.github.cmartinezs.stl4j.task.AbstractTask;
import lombok.SneakyThrows;

import java.util.function.BooleanSupplier;

public class SelfNullTask extends AbstractTask {

    public SelfNullTask(String name) {
        this(name, true);
    }

    public SelfNullTask(String name, boolean executionResult) {
        super(name);
    }

    @SneakyThrows
    @Override
    public void execute() {
        super.execute();
    }

    @Override
    protected BooleanSupplier selfRegister() {
        return null;
    }
}
