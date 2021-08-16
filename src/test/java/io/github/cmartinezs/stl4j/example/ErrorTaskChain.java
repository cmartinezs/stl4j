package io.github.cmartinezs.stl4j.example;

import io.github.cmartinezs.stl4j.task.TaskStatus;
import io.github.cmartinezs.stl4j.task.chain.TaskChain;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BooleanSupplier;

@Slf4j
public class ErrorTaskChain extends TaskChain {
    private final BooleanTask booleanTask;
    public ErrorTaskChain(String name) {
        super(name);
        this.booleanTask = new BooleanTask(String.format("%s", name), false);
    }

    @Override
    protected BooleanSupplier selfRegister() {
        return () -> {
            log.debug("{}: I'm a {} task", getName(), getClass().getSimpleName());
            booleanTask.execute();
            return TaskStatus.SUCCESS.equals(booleanTask.getStatus());
        };
    }
}