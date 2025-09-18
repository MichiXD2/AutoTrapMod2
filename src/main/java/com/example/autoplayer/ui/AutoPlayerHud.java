package com.example.autoplayer.ui;

import com.example.autoplayer.ai.Vec3i;
import com.example.autoplayer.task.AutoPlayerTask;
import com.example.autoplayer.task.TaskContext;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Simulated HUD for communicating automation state to the user.
 */
public class AutoPlayerHud {

    private String currentTask;
    private String statusMessage;
    private String safetyWarning;
    private List<Vec3i> navigationPath = Collections.emptyList();
    private Instant lastUpdate = Instant.now();

    public void render(TaskContext context, AutoPlayerTask task) {
        if (context != null && !context.getConfig().isEnableHud()) {
            return;
        }
        currentTask = task == null ? "Idle" : task.getName();
        if (task == null) {
            statusMessage = "Awaiting tasks";
        } else {
            statusMessage = task.getStatus(context).orElse("Working...");
        }
        lastUpdate = Instant.now();
        System.out.printf("[HUD %s] Task: %s | Status: %s%n", lastUpdate, currentTask, statusMessage);
        if (safetyWarning != null) {
            System.out.printf("[HUD] Safety warning: %s%n", safetyWarning);
        }
        if (!navigationPath.isEmpty()) {
            System.out.printf("[HUD] Path length: %d (next %s)%n", navigationPath.size(), navigationPath.get(0));
        }
    }

    public void setSafetyWarning(String safetyWarning) {
        this.safetyWarning = safetyWarning;
    }

    public void setNavigationPath(List<Vec3i> navigationPath) {
        this.navigationPath = List.copyOf(navigationPath);
    }

    public Optional<String> getCurrentTask() {
        return Optional.ofNullable(currentTask);
    }
}
