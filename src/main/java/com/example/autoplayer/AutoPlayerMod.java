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
    private AutoPlayerConfig config;
    private TaskContext context;
    private TaskScheduler scheduler;
    private boolean initialized;

    public AutoPlayerMod() {
        this.configManager = new ConfigManager();
        this.progressPersistence = new ProgressPersistence();
    }

    public void start() {
        initialize();
    }

    public void initialize() {
        if (initialized) {
            return;
        }
        this.config = configManager.load();
        WorldScanner worldScanner = new WorldScanner(config.getScanRadius());
        InventoryManager inventoryManager = new InventoryManager();
        CraftingManager craftingManager = new CraftingManager();
        AutomationEngine automationEngine = new AutomationEngine(inventoryManager, craftingManager);
        Pathfinder pathfinder = new Pathfinder();
        StateMachine stateMachine = new StateMachine();
        SafetyManager safetyManager = new SafetyManager(worldScanner);
        AutoPlayerHud hud = new AutoPlayerHud();
        KeybindHandler keybindHandler = new KeybindHandler(config, hud);

        this.context = new TaskContext(
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

        this.scheduler = new TaskScheduler(progressPersistence, context);
        context.setScheduler(scheduler);

        scheduler.registerTask(new GatherWoodTask());
        scheduler.registerTask(new CraftBasicToolsTask());
        scheduler.registerTask(new BranchMineForDiamondsTask());
        scheduler.registerTask(new EquipDiamondSetTask());

        scheduler.start();
        initialized = true;
    }

    public void tick() {
        if (!initialized) {
            return;
        }
        scheduler.tick();
    }

    public void renderHud() {
        if (!initialized) {
            return;
        }
        if (!context.getConfig().isEnabled()) {
            context.getHud().render(context, null);
            return;
        }
        context.getHud().render(context, scheduler.getCurrentTask().orElse(null));
    }

    public void toggleAutomation() {
        if (!initialized) {
            return;
        }
        context.getKeybindHandler().toggleAutomation();
    }

    public void saveState() {
        if (!initialized) {
            return;
        }
        configManager.save(config);
        progressPersistence.saveCompletedTasks(
                scheduler.getCompletedTaskNames(),
                scheduler.getCurrentTaskName().orElse(""));
    }

    public boolean isInitialized() {
        return initialized;
    }
}
