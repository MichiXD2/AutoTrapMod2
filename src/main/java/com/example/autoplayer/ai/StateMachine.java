package com.example.autoplayer.ai;

import com.example.autoplayer.task.TaskContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Small state machine used by tasks to sequence sub-actions.
 */
public class StateMachine {

    private final Map<String, State> states = new HashMap<>();
    private State activeState;

    public void register(State state) {
        states.put(state.getName(), state);
    }

    public void transition(String stateName, TaskContext context) {
        State next = states.get(stateName);
        if (next == null) {
            throw new IllegalStateException("Unknown state: " + stateName);
        }
        if (!Objects.equals(activeState, next)) {
            if (activeState != null) {
                activeState.onExit(context);
            }
            activeState = next;
            activeState.onEnter(context);
        }
    }

    public void tick(TaskContext context) {
        if (activeState != null) {
            activeState.tick(context);
        }
    }

    public String getActiveStateName() {
        return activeState == null ? "idle" : activeState.getName();
    }
}
