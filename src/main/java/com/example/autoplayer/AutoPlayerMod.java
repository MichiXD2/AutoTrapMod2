package com.example.autoplayer;

import com.example.autoplayer.ai.Pathfinder;
import com.example.autoplayer.ai.StateMachine;
import com.example.autoplayer.config.AutoPlayerConfig;
import com.example.autoplayer.config.ConfigManager;
import com.example.autoplayer.config.ProgressPersistence;
import com.example.autoplayer.logic.AutomationEngine;
import com.example.autoplayer.logic.CraftingManager;
import com.example.autoplayer.logic.InventoryManager;
import com.example.autoplayer.safety.SafetyManager;
import com.example.autoplayer.task.BranchMineForDiamondsTask;
import com.example.autoplayer.task.CraftBasicToolsTask;
import com.example.autoplayer.task.EquipDiamondSetTask;
import com.example.autoplayer.task.GatherWoodTask;
import com.example.autoplayer.task.TaskContext;
import com.example.autoplayer.task.TaskScheduler;
import com.example.autoplayer.ui.AutoPlayerHud;
import com.example.autoplayer.ui.KeybindHandler;
import com.example.autoplayer.world.WorldScanner;

/**
 * Entrypoint that wires together the automated progression system.
 */
public final class AutoPlayerMod {

    private final ConfigManager configManager;
    private final ProgressPersistence progressPersistence;

    public AutoPlayerMod() {
        this.configManager = new ConfigManager();
        this.progressPersistence = new ProgressPersistence();
    }

    public void start() {
        AutoPlayerConfig config = configManager.load();
        WorldScanner worldScanner = new WorldScanner(config.getScanRadius());
        InventoryManager inventoryManager = new InventoryManager();
        CraftingManager craftingManager = new CraftingManager();
        AutomationEngine automationEngine = new AutomationEngine(inventoryManager, craftingManager);
        Pathfinder pathfinder = new Pathfinder();
        StateMachine stateMachine = new StateMachine();
        SafetyManager safetyManager = new SafetyManager(worldScanner);
        AutoPlayerHud hud = new AutoPlayerHud();
        KeybindHandler keybindHandler = new KeybindHandler(config, hud);

        TaskContext context = new TaskContext(
                config,
                inventoryManager,
                craftingManager,
                automationEngine,
                worldScanner,
                pathfinder,
                safetyManager,
                stateMachine,
                hud,
                keybindHandler,
                progressPersistence,
                configManager);

        TaskScheduler scheduler = new TaskScheduler(progressPersistence, context);
        context.setScheduler(scheduler);

        scheduler.registerTask(new GatherWoodTask());
        scheduler.registerTask(new CraftBasicToolsTask());
        scheduler.registerTask(new BranchMineForDiamondsTask());
        scheduler.registerTask(new EquipDiamondSetTask());

        scheduler.start();

        while (!scheduler.isComplete()) {
            scheduler.tick();
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        configManager.save(config);
        progressPersistence.saveCompletedTasks(scheduler.getCompletedTaskNames(), scheduler.getCurrentTaskName().orElse(""));
    }

    public static void main(String[] args) {
        new AutoPlayerMod().start();
    }
}
