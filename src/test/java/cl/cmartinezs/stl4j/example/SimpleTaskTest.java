package cl.cmartinezs.stl4j.example;

import cl.cmartinezs.stl4j.task.AbstractTask;
import cl.cmartinezs.stl4j.task.exception.TaskException;

public class SimpleTaskTest extends AbstractTask {
    private final boolean executionResult;

    public SimpleTaskTest(String name) {
        this(name, true);
    }

    public SimpleTaskTest(String name, boolean executionResult) {
        super(name);
        this.executionResult = executionResult;
    }

    @Override
    protected boolean executeSelf() throws TaskException {
        return executionResult;
    }
}
