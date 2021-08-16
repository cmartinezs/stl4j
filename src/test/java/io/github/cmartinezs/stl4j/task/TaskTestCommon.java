/*
  This file is part of Simple Task Library 4 J. Distributed under GPL v3.0 license
 */
package io.github.cmartinezs.stl4j.task;

import java.util.UUID;

/**
 * @author Carlos
 * @version 1.0
 * @since 0.1.0
 */
public class TaskTestCommon {
    protected String createTaskName(String name) {
        return name.concat("-").concat(UUID.randomUUID().toString());
    }
}
