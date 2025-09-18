package com.example.autoplayer.logic;

import java.util.Map;

/**
 * Coordinates inventory and crafting actions to satisfy higher level goals.
 */
public class AutomationEngine {

    private final InventoryManager inventoryManager;
    private final CraftingManager craftingManager;

    public AutomationEngine(InventoryManager inventoryManager, CraftingManager craftingManager) {
        this.inventoryManager = inventoryManager;
        this.craftingManager = craftingManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public boolean ensureItem(String itemId, int amount) {
        if (inventoryManager.getCount(itemId) >= amount) {
            return true;
        }
        return attemptCraft(itemId, amount);
    }

    private boolean attemptCraft(String itemId, int amount) {
        String recipeName = switch (itemId) {
            case "plank" -> "planks";
            case "stick" -> "sticks";
            case "crafting_table" -> "crafting_table";
            case "wooden_pickaxe" -> "wooden_pickaxe";
            case "stone_pickaxe" -> "stone_pickaxe";
            case "iron_pickaxe" -> "iron_pickaxe";
            case "diamond_pickaxe" -> "diamond_pickaxe";
            case "diamond_helmet" -> "diamond_helmet";
            case "diamond_chestplate" -> "diamond_chestplate";
            case "diamond_leggings" -> "diamond_leggings";
            case "diamond_boots" -> "diamond_boots";
            default -> null;
        };
        if (recipeName == null) {
            return false;
        }
        int crafted = 0;
        while (crafted < amount) {
            if (!craftingManager.craft(recipeName, inventoryManager)) {
                break;
            }
            crafted += switch (itemId) {
                case "plank" -> 4;
                case "stick" -> 4;
                default -> 1;
            };
        }
        return inventoryManager.getCount(itemId) >= amount;
    }

    public boolean consumeItems(Map<String, Integer> requirements) {
        if (!inventoryManager.hasAll(requirements)) {
            return false;
        }
        for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
            inventoryManager.removeItem(entry.getKey(), entry.getValue());
        }
        return true;
    }
}
