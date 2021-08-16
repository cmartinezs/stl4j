package io.github.cmartinezs.stl4j.example;

import io.github.cmartinezs.stl4j.task.chain.TaskChain;
import io.github.cmartinezs.stl4j.task.utils.supplier.BooleanSupplierFactory;

import java.util.function.BooleanSupplier;

public class SuccessTaskChain extends TaskChain {
    public SuccessTaskChain(String name) {
        super(name);
    }

    @Override
    protected BooleanSupplier selfRegister() {
        return BooleanSupplierFactory._true();
    }
}