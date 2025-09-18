package com.example.autoplayer.task;

import com.example.autoplayer.ai.Pathfinder;
import com.example.autoplayer.ai.Vec3i;
import com.example.autoplayer.logic.InventoryManager;
import com.example.autoplayer.world.ResourceLocation;

import java.util.List;

/**
 * Gathers logs and crafts the basic planks and sticks required for early progression.
 */
public class GatherWoodTask extends AbstractAutoPlayerTask {

    private static final int TARGET_LOGS = 16;
    private ResourceLocation currentTree;

    @Override
    public String getName() {
        return "Gather Wood";
    }

    @Override
    protected void doOnStart(TaskContext context) {
        updateStatus("Scanning for trees");
    }

    @Override
    public void tick(TaskContext context) {
        InventoryManager inventory = context.getInventoryManager();
        if (inventory.getCount("log") >= TARGET_LOGS) {
            updateStatus("Target met");
            return;
        }
        if (context.getSafetyManager().isEngaged()) {
            updateStatus("Waiting for safety");
            return;
        }
        if (currentTree == null) {
            currentTree = context.getWorldScanner().scanForResource("log");
            updateStatus("Tree located at " + currentTree.getPosition());
            Pathfinder pathfinder = context.getPathfinder();
            List<Vec3i> path = pathfinder.findPath(context.getWorldScanner().getPlayerPosition(), currentTree.getPosition(), context.getWorldScanner());
            pathfinder.followPath(path, context);
            return;
        }
        updateStatus("Chopping logs");
        inventory.addItem("log", currentTree.getRichness());
        currentTree = null;
    }

    @Override
    public boolean isComplete(TaskContext context) {
        return context.getInventoryManager().getCount("log") >= TARGET_LOGS;
    }
}
