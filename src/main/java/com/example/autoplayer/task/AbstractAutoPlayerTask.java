package com.example.autoplayer.task;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Convenience base class for tasks.
 */
public abstract class AbstractAutoPlayerTask implements AutoPlayerTask {

    private Instant startTime;
    private String status;

    @Override
    public final void onStart(TaskContext context) {
        startTime = Instant.now();
        status = "Starting";
        doOnStart(context);
    }

    protected void doOnStart(TaskContext context) {
        // optional override
    }

    protected void updateStatus(String status) {
        this.status = status;
    }

    public Duration elapsed() {
        return Duration.between(startTime, Instant.now());
    }

    @Override
    public Optional<String> getStatus(TaskContext context) {
        return Optional.ofNullable(status);
    }
}
