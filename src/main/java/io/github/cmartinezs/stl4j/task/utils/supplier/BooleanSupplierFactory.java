package io.github.cmartinezs.stl4j.task.utils.supplier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.BooleanSupplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BooleanSupplierFactory {
    public static BooleanSupplier _true() {
        return () -> true;
    }
}
