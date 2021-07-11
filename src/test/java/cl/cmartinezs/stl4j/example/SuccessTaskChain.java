package cl.cmartinezs.stl4j.example;

import cl.cmartinezs.stl4j.task.chain.TaskChain;
import cl.cmartinezs.stl4j.task.utils.supplier.BooleanSupplierFactory;

public class SuccessTaskChain extends TaskChain {
    public SuccessTaskChain(String name) {
        super(name);
        this.setSelf(BooleanSupplierFactory._true());
    }
}