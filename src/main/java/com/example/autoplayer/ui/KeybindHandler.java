package com.example.autoplayer.ui;

import com.example.autoplayer.config.AutoPlayerConfig;

import java.time.Duration;
import java.time.Instant;

/**
 * Processes keybind toggles. In this simulation we expose methods that tasks can call.
 */
public class KeybindHandler {

    private final AutoPlayerConfig config;
    private final AutoPlayerHud hud;
    private Instant lastToggle = Instant.now();

    public KeybindHandler(AutoPlayerConfig config, AutoPlayerHud hud) {
        this.config = config;
        this.hud = hud;
    }

    public void toggleAutomation() {
        if (Duration.between(lastToggle, Instant.now()).toMillis() < 500) {
            return;
        }
        config.setEnabled(!config.isEnabled());
        lastToggle = Instant.now();
        System.out.println("Automation toggled: " + (config.isEnabled() ? "enabled" : "disabled"));
    }

    public void poll() {
        // placeholder for integration with actual keybind system
    }

    public AutoPlayerHud getHud() {
        return hud;
    }
}
