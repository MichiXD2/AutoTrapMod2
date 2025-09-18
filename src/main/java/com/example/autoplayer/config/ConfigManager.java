package com.example.autoplayer.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Handles persistence of the user configurable options.
 */
public class ConfigManager {

    private static final String CONFIG_DIR = "config";
    private static final String CONFIG_FILE = "autoplayer.properties";

    private final Path configPath;

    public ConfigManager() {
        this.configPath = Paths.get(CONFIG_DIR, CONFIG_FILE);
    }

    public AutoPlayerConfig load() {
        ensureDirectory();
        AutoPlayerConfig config = new AutoPlayerConfig();
        if (!Files.exists(configPath)) {
            save(config);
            return config;
        }

        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(configPath)) {
            properties.load(in);
            config.setEnabled(Boolean.parseBoolean(properties.getProperty("enabled", "true")));
            config.setEnableHud(Boolean.parseBoolean(properties.getProperty("enableHud", "true")));
            config.setAutoCrafting(Boolean.parseBoolean(properties.getProperty("autoCrafting", "true")));
            config.setScanRadius(Integer.parseInt(properties.getProperty("scanRadius", "32")));
            config.setCautiousMode(Boolean.parseBoolean(properties.getProperty("cautiousMode", "true")));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load AutoPlayer configuration", e);
        }
        return config;
    }

    public void save(AutoPlayerConfig config) {
        ensureDirectory();
        Properties properties = new Properties();
        properties.setProperty("enabled", Boolean.toString(config.isEnabled()));
        properties.setProperty("enableHud", Boolean.toString(config.isEnableHud()));
        properties.setProperty("autoCrafting", Boolean.toString(config.isAutoCrafting()));
        properties.setProperty("scanRadius", Integer.toString(config.getScanRadius()));
        properties.setProperty("cautiousMode", Boolean.toString(config.isCautiousMode()));

        try (OutputStream out = Files.newOutputStream(configPath)) {
            properties.store(out, "AutoPlayer Mod Configuration");
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save AutoPlayer configuration", e);
        }
    }

    private void ensureDirectory() {
        try {
            Files.createDirectories(configPath.getParent());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create config directory", e);
        }
    }
}
