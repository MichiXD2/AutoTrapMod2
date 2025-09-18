package com.example.autoplayer.task;

import com.example.autoplayer.logic.AutomationEngine;
import com.example.autoplayer.logic.InventoryManager;

import java.util.Map;

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

    private static final String PICKAXE = "diamond_pickaxe";

    private static final Map<String, Integer> DIAMOND_COSTS = Map.ofEntries(
            Map.entry("diamond_helmet", 5),
            Map.entry("diamond_chestplate", 8),
            Map.entry("diamond_leggings", 7),
            Map.entry("diamond_boots", 4),
            Map.entry(PICKAXE, 3)
    );

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

        int outstandingDiamondCost = getOutstandingDiamondCost(inventory);
        if (outstandingDiamondCost > 0 && inventory.getCount("diamond") < outstandingDiamondCost) {
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
        if (!inventory.hasItem(PICKAXE, 1)) {
            updateStatus("Crafting diamond pickaxe");
            automation.ensureItem(PICKAXE, 1);
            return;
        }
        if (!inventory.isEquipped(PICKAXE)) {
            updateStatus("Equipping diamond pickaxe");
            inventory.equip(PICKAXE);
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
        return inventory.isEquipped(PICKAXE);
    }

    private int getOutstandingDiamondCost(InventoryManager inventory) {
        int cost = 0;
        for (String piece : ARMOR_SET) {
            if (!isCraftedOrEquipped(inventory, piece)) {
                cost += DIAMOND_COSTS.getOrDefault(piece, 0);
            }
        }
        if (!isCraftedOrEquipped(inventory, PICKAXE)) {
            cost += DIAMOND_COSTS.getOrDefault(PICKAXE, 0);
        }
        return cost;
    }

    private boolean isCraftedOrEquipped(InventoryManager inventory, String itemId) {
        return inventory.hasItem(itemId, 1) || inventory.isEquipped(itemId);
    }

    @Override
    public boolean isComplete(TaskContext context) {
        return hasAllArmor(context.getInventoryManager());
    }
}
