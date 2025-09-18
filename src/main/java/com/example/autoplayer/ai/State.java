package com.example.autoplayer.ai;

import com.example.autoplayer.task.TaskContext;

/**
 * Represents an action state controlled by the state machine.
 */
public interface State {
    String getName();

    default void onEnter(TaskContext context) {
        // no-op
    }

    void tick(TaskContext context);

    default void onExit(TaskContext context) {
        // no-op
    }
}
