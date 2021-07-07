package cl.cmartinezs.stl4j.example;

import cl.cmartinezs.stl4j.task.chain.TaskChain;

public class ErrorTaskChain extends TaskChain {
    public ErrorTaskChain(String name) {
        super(name);
    }

    @Override
    protected boolean executeSelf() {
        System.out.println(getName() + ": Yo soy una tarea Bye Task");
        return false;
    }
}