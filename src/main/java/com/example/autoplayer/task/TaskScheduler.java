package com.example.autoplayer.task;

import com.example.autoplayer.config.ProgressPersistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Executes registered tasks sequentially and keeps their progress persistent.
 */
public class TaskScheduler {

    private final ProgressPersistence progressPersistence;
    private final TaskContext context;
    private final List<AutoPlayerTask> tasks = new ArrayList<>();
    private final Set<String> completedTasks;
    private AutoPlayerTask currentTask;
    private int currentIndex;
    private boolean started;

    public TaskScheduler(ProgressPersistence progressPersistence, TaskContext context) {
        this.progressPersistence = progressPersistence;
        this.context = context;
        this.completedTasks = new HashSet<>(progressPersistence.loadCompletedTasks());
    }

    public void registerTask(AutoPlayerTask task) {
        tasks.add(task);
    }

    public void start() {
        if (started) {
            return;
        }
        started = true;
        String savedCurrent = progressPersistence.loadCurrentTask();
        if (!savedCurrent.isEmpty()) {
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getName().equals(savedCurrent)) {
                    currentIndex = i;
                    break;
                }
            }
        }
        skipCompletedTasks();
    }

    private void skipCompletedTasks() {
        while (currentIndex < tasks.size() && completedTasks.contains(tasks.get(currentIndex).getName())) {
            currentIndex++;
        }
    }

    public void tick() {
        context.getSafetyManager().tick(context);
        if (!context.getConfig().isEnabled()) {
            context.getStateMachine().tick(context);
            return;
        }
        if (currentTask == null && currentIndex < tasks.size()) {
            beginTask();
        }
        if (currentTask != null) {
            currentTask.tick(context);
            if (currentTask.isComplete(context)) {
                currentTask.onComplete(context);
                completedTasks.add(currentTask.getName());
                progressPersistence.saveCompletedTasks(completedTasks, "");
                currentTask = null;
                currentIndex++;
                skipCompletedTasks();
                if (currentIndex < tasks.size()) {
                    progressPersistence.saveCompletedTasks(completedTasks, tasks.get(currentIndex).getName());
                } else {
                    progressPersistence.saveCompletedTasks(completedTasks, "");
                }
            }
        }
        context.getStateMachine().tick(context);
    }

    private void beginTask() {
        currentTask = tasks.get(currentIndex);
        currentTask.onStart(context);
        progressPersistence.saveCompletedTasks(completedTasks, currentTask.getName());
    }

    public boolean isComplete() {
        return currentIndex >= tasks.size();
    }

    public Optional<String> getCurrentTaskName() {
        return currentTask == null ? Optional.empty() : Optional.of(currentTask.getName());
    }

    public Set<String> getCompletedTaskNames() {
        return new HashSet<>(completedTasks);
    }

    public Optional<AutoPlayerTask> getCurrentTask() {
        return Optional.ofNullable(currentTask);
    }
}
