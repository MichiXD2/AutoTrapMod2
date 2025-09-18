package com.example.autoplayer.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Stores high level progression state between sessions.
 */
public class ProgressPersistence {

    private static final String CONFIG_DIR = "config";
    private static final String PROGRESS_FILE = "autoplayer-progress.properties";

    private final Path progressPath;

    public ProgressPersistence() {
        this.progressPath = Paths.get(CONFIG_DIR, PROGRESS_FILE);
    }

    public Set<String> loadCompletedTasks() {
        ensureDirectory();
        if (!Files.exists(progressPath)) {
            return new HashSet<>();
        }

        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(progressPath)) {
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load AutoPlayer progress", e);
        }

        String completedValue = properties.getProperty("completedTasks", "");
        if (completedValue.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(completedValue.split(",")));
    }

    public String loadCurrentTask() {
        ensureDirectory();
        if (!Files.exists(progressPath)) {
            return "";
        }
        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(progressPath)) {
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load AutoPlayer progress", e);
        }
        return properties.getProperty("currentTask", "");
    }

    public void saveCompletedTasks(Set<String> completedTasks, String currentTask) {
        ensureDirectory();
        Properties properties = new Properties();
        properties.setProperty("completedTasks", String.join(",", completedTasks));
        properties.setProperty("currentTask", currentTask);

        try (OutputStream out = Files.newOutputStream(progressPath)) {
            properties.store(out, "AutoPlayer Progress");
        } catch (IOException e) {
            throw new IllegalStateException("Unable to store AutoPlayer progress", e);
        }
    }

    private void ensureDirectory() {
        try {
            Files.createDirectories(progressPath.getParent());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create progress directory", e);
        }
    }
}
