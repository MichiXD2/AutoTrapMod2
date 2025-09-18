package com.example.autoplayer.task;

import java.util.Optional;

/**
 * Common contract for automation tasks.
 */
public interface AutoPlayerTask {

    String getName();

    default String getDescription() {
        return getName();
    }

    void onStart(TaskContext context);

    void tick(TaskContext context);

    boolean isComplete(TaskContext context);

    default void onComplete(TaskContext context) {
        // no-op by default
    }

    default Optional<String> getStatus(TaskContext context) {
        return Optional.empty();
    }
}
