package com.example.autoplayer.task;

import com.example.autoplayer.ai.Pathfinder;
import com.example.autoplayer.ai.StateMachine;
import com.example.autoplayer.config.AutoPlayerConfig;
import com.example.autoplayer.config.ConfigManager;
import com.example.autoplayer.config.ProgressPersistence;
import com.example.autoplayer.logic.AutomationEngine;
import com.example.autoplayer.logic.CraftingManager;
import com.example.autoplayer.logic.InventoryManager;
import com.example.autoplayer.safety.SafetyManager;
import com.example.autoplayer.ui.AutoPlayerHud;
import com.example.autoplayer.ui.KeybindHandler;
import com.example.autoplayer.world.WorldScanner;

/**
 * Bundles together the systems needed by tasks.
 */
public class TaskContext {

    private final AutoPlayerConfig config;
    private final InventoryManager inventoryManager;
    private final CraftingManager craftingManager;
    private final AutomationEngine automationEngine;
    private final WorldScanner worldScanner;
    private final Pathfinder pathfinder;
    private final SafetyManager safetyManager;
    private final StateMachine stateMachine;
    private final AutoPlayerHud hud;
    private final KeybindHandler keybindHandler;
    private final ProgressPersistence progressPersistence;
    private final ConfigManager configManager;
    private TaskScheduler scheduler;

    public TaskContext(AutoPlayerConfig config,
                       InventoryManager inventoryManager,
                       CraftingManager craftingManager,
                       AutomationEngine automationEngine,
                       WorldScanner worldScanner,
                       Pathfinder pathfinder,
                       SafetyManager safetyManager,
                       StateMachine stateMachine,
                       AutoPlayerHud hud,
                       KeybindHandler keybindHandler,
                       ProgressPersistence progressPersistence,
                       ConfigManager configManager) {
        this.config = config;
        this.inventoryManager = inventoryManager;
        this.craftingManager = craftingManager;
        this.automationEngine = automationEngine;
        this.worldScanner = worldScanner;
        this.pathfinder = pathfinder;
        this.safetyManager = safetyManager;
        this.stateMachine = stateMachine;
        this.hud = hud;
        this.keybindHandler = keybindHandler;
        this.progressPersistence = progressPersistence;
        this.configManager = configManager;
    }

    public void setScheduler(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public TaskScheduler getScheduler() {
        return scheduler;
    }

    public AutoPlayerConfig getConfig() {
        return config;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public CraftingManager getCraftingManager() {
        return craftingManager;
    }

    public AutomationEngine getAutomationEngine() {
        return automationEngine;
    }

    public WorldScanner getWorldScanner() {
        return worldScanner;
    }

    public Pathfinder getPathfinder() {
        return pathfinder;
    }

    public SafetyManager getSafetyManager() {
        return safetyManager;
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public AutoPlayerHud getHud() {
        return hud;
    }

    public KeybindHandler getKeybindHandler() {
        return keybindHandler;
    }

    public ProgressPersistence getProgressPersistence() {
        return progressPersistence;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
