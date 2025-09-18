package com.example.autoplayer.task;

import com.example.autoplayer.ai.Pathfinder;
import com.example.autoplayer.ai.StateMachine;
import com.example.autoplayer.config.AutoPlayerConfig;
import com.example.autoplayer.config.ProgressPersistence;
import com.example.autoplayer.logic.AutomationEngine;
import com.example.autoplayer.logic.CraftingManager;
import com.example.autoplayer.logic.InventoryManager;
import com.example.autoplayer.safety.SafetyManager;
import com.example.autoplayer.ui.AutoPlayerHud;
import com.example.autoplayer.ui.KeybindHandler;
import com.example.autoplayer.world.WorldScanner;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskSchedulerTest {

    @Test
    void automationPauseStopsStateMachineProgressUntilReenabled() {
        AutoPlayerConfig config = new AutoPlayerConfig();
        InventoryManager inventoryManager = new InventoryManager();
        CraftingManager craftingManager = new CraftingManager();
        AutomationEngine automationEngine = new AutomationEngine(inventoryManager, craftingManager);
        WorldScanner worldScanner = new WorldScanner(config.getScanRadius());
        Pathfinder pathfinder = new Pathfinder();
        AutoPlayerHud hud = new AutoPlayerHud();
        SafetyManager safetyManager = new NoOpSafetyManager(worldScanner);
        StateMachine stateMachine = new StateMachine();
        KeybindHandler keybindHandler = new KeybindHandler(config, hud);
        ProgressPersistence progressPersistence = new InMemoryProgressPersistence();

        TaskContext context = new TaskContext(config, inventoryManager, craftingManager, automationEngine,
                worldScanner, pathfinder, safetyManager, stateMachine, hud, keybindHandler, progressPersistence, null);
        TaskScheduler scheduler = new TaskScheduler(progressPersistence, context);
        context.setScheduler(scheduler);

        BranchMineForDiamondsTask miningTask = new BranchMineForDiamondsTask();
        scheduler.registerTask(miningTask);
        scheduler.start();

        // Warm up the scheduler so the branch mining task is actively running.
        for (int i = 0; i < 3; i++) {
            scheduler.tick();
        }

        int diamondsBeforePause = inventoryManager.getCount("diamond");
        assertTrue(diamondsBeforePause > 0, "Expected branch mining to gather diamonds before pause");
        String statusBeforePause = miningTask.getStatus(context).orElse("");
        String stateBeforePause = stateMachine.getActiveStateName();

        config.setEnabled(false);
        for (int i = 0; i < 5; i++) {
            scheduler.tick();
        }

        assertEquals(diamondsBeforePause, inventoryManager.getCount("diamond"),
                "Inventory should remain unchanged while automation is disabled");
        assertEquals(statusBeforePause, miningTask.getStatus(context).orElse(""),
                "Task status should not change while automation is disabled");
        assertEquals(stateBeforePause, stateMachine.getActiveStateName(),
                "State machine should not advance while automation is disabled");

        config.setEnabled(true);
        boolean statusChanged = false;
        for (int i = 0; i < 10; i++) {
            scheduler.tick();
            if (!statusBeforePause.equals(miningTask.getStatus(context).orElse(""))) {
                statusChanged = true;
                break;
            }
        }

        assertTrue(inventoryManager.getCount("diamond") > diamondsBeforePause,
                "Inventory should resume accumulating diamonds once automation is re-enabled");
        assertTrue(statusChanged, "Task status should eventually change after automation resumes");
    }

    private static class InMemoryProgressPersistence extends ProgressPersistence {
        private Set<String> completed = new HashSet<>();
        private String currentTask = "";

        @Override
        public Set<String> loadCompletedTasks() {
            return new HashSet<>(completed);
        }

        @Override
        public String loadCurrentTask() {
            return currentTask;
        }

        @Override
        public void saveCompletedTasks(Set<String> completedTasks, String currentTask) {
            this.completed = new HashSet<>(completedTasks);
            this.currentTask = currentTask;
        }
    }

    private static class NoOpSafetyManager extends SafetyManager {
        public NoOpSafetyManager(WorldScanner worldScanner) {
            super(worldScanner);
        }

        @Override
        public void tick(TaskContext context) {
            // Skip hazard detection for deterministic testing.
        }

        @Override
        public boolean isEngaged() {
            return false;
        }
    }
}
