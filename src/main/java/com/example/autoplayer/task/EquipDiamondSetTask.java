package com.example.autoplayer.task;

import com.example.autoplayer.logic.AutomationEngine;
import com.example.autoplayer.logic.InventoryManager;

/**
 * Finalises progression by crafting and equipping a full set of diamond gear.
 */
public class EquipDiamondSetTask extends AbstractAutoPlayerTask {

    private static final String[] ARMOR_SET = {
            "diamond_helmet",
            "diamond_chestplate",
            "diamond_leggings",
            "diamond_boots"
    };

    @Override
    public String getName() {
        return "Equip Diamond Set";
    }

    @Override
    protected void doOnStart(TaskContext context) {
        updateStatus("Preparing to craft diamond gear");
    }

    @Override
    public void tick(TaskContext context) {
        InventoryManager inventory = context.getInventoryManager();
        AutomationEngine automation = context.getAutomationEngine();

        if (inventory.getCount("diamond") < 27 && !hasAllArmor(inventory)) {
            updateStatus("Waiting for diamonds from mining");
            return;
        }
        if (!inventory.hasItem("stick", 8)) {
            updateStatus("Stocking sticks for handle components");
            automation.ensureItem("stick", 8);
            return;
        }
        for (String piece : ARMOR_SET) {
            if (!inventory.hasItem(piece, 1)) {
                updateStatus("Crafting " + piece);
                automation.ensureItem(piece, 1);
                return;
            }
            if (!inventory.isEquipped(piece)) {
                updateStatus("Equipping " + piece);
                inventory.equip(piece);
                return;
            }
        }
        if (!inventory.hasItem("diamond_pickaxe", 1)) {
            updateStatus("Crafting diamond pickaxe");
            automation.ensureItem("diamond_pickaxe", 1);
            return;
        }
        if (!inventory.isEquipped("diamond_pickaxe")) {
            updateStatus("Equipping diamond pickaxe");
            inventory.equip("diamond_pickaxe");
            return;
        }
        updateStatus("Diamond champion ready");
    }

    private boolean hasAllArmor(InventoryManager inventory) {
        for (String piece : ARMOR_SET) {
            if (!inventory.isEquipped(piece)) {
                return false;
            }
        }
        return inventory.isEquipped("diamond_pickaxe");
    }

    @Override
    public boolean isComplete(TaskContext context) {
        return hasAllArmor(context.getInventoryManager());
    }
}
