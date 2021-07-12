package io.github.cmartinezs.stl4j.task.utils.consumer;

import io.github.cmartinezs.stl4j.task.utils.exception.TaskException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskExceptionConsumerFactory {

    public static Consumer<TaskException> empty(){
        return e -> { };
    }
}
