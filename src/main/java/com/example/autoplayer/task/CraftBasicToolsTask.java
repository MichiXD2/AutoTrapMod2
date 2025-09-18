package com.example.autoplayer.task;

import com.example.autoplayer.logic.AutomationEngine;
import com.example.autoplayer.logic.InventoryManager;

/**
 * Crafts the basic tools needed before progressing into mining.
 */
public class CraftBasicToolsTask extends AbstractAutoPlayerTask {

    @Override
    public String getName() {
        return "Craft Basic Tools";
    }

    @Override
    protected void doOnStart(TaskContext context) {
        updateStatus("Preparing crafting resources");
    }

    @Override
    public void tick(TaskContext context) {
        InventoryManager inventory = context.getInventoryManager();
        AutomationEngine automation = context.getAutomationEngine();

        if (!inventory.hasItem("plank", 32)) {
            updateStatus("Converting logs to planks");
            automation.ensureItem("plank", 32);
            return;
        }
        if (!inventory.hasItem("stick", 16)) {
            updateStatus("Crafting sticks");
            automation.ensureItem("stick", 16);
            return;
        }
        if (!inventory.hasItem("crafting_table", 1)) {
            updateStatus("Crafting crafting table");
            automation.ensureItem("crafting_table", 1);
            return;
        }
        if (!inventory.hasItem("cobblestone", 20)) {
            updateStatus("Gathering cobblestone");
            inventory.addItem("cobblestone", 20);
            return;
        }
        if (!inventory.hasItem("stone_pickaxe", 1)) {
            updateStatus("Crafting stone pickaxe");
            automation.ensureItem("stone_pickaxe", 1);
            return;
        }
        updateStatus("Basic kit ready");
    }

    @Override
    public boolean isComplete(TaskContext context) {
        InventoryManager inventory = context.getInventoryManager();
        return inventory.hasItem("stone_pickaxe", 1) && inventory.hasItem("crafting_table", 1);
    }
}
