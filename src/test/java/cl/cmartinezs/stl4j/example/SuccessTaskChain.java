package cl.cmartinezs.stl4j.example;

import cl.cmartinezs.stl4j.task.chain.TaskChain;

public class SuccessTaskChain extends TaskChain {
    public SuccessTaskChain(String name) {
        super(name);
    }

    @Override
    protected boolean executeSelf() {
        System.out.println(getName() + ": Yo soy una tarea Hello Task");
        return true;
    }
}