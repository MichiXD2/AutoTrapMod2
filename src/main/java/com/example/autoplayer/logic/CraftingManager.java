package com.example.autoplayer.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles crafting operations and recipe management.
 */
public class CraftingManager {

    private final Map<String, Map<String, Integer>> recipes = new HashMap<>();

    public CraftingManager() {
        registerRecipe("planks", Map.of("log", 1), Map.of("plank", 4));
        registerRecipe("sticks", Map.of("plank", 2), Map.of("stick", 4));
        registerRecipe("crafting_table", Map.of("plank", 4), Map.of("crafting_table", 1));
        registerRecipe("wooden_pickaxe", Map.of("plank", 3, "stick", 2), Map.of("wooden_pickaxe", 1));
        registerRecipe("stone_pickaxe", Map.of("cobblestone", 3, "stick", 2), Map.of("stone_pickaxe", 1));
        registerRecipe("iron_pickaxe", Map.of("iron_ingot", 3, "stick", 2), Map.of("iron_pickaxe", 1));
        registerRecipe("diamond_pickaxe", Map.of("diamond", 3, "stick", 2), Map.of("diamond_pickaxe", 1));
        registerRecipe("diamond_helmet", Map.of("diamond", 5), Map.of("diamond_helmet", 1));
        registerRecipe("diamond_chestplate", Map.of("diamond", 8), Map.of("diamond_chestplate", 1));
        registerRecipe("diamond_leggings", Map.of("diamond", 7), Map.of("diamond_leggings", 1));
        registerRecipe("diamond_boots", Map.of("diamond", 4), Map.of("diamond_boots", 1));
    }

    private void registerRecipe(String name, Map<String, Integer> input, Map<String, Integer> output) {
        recipes.put(name + "#input", Map.copyOf(input));
        recipes.put(name + "#output", Map.copyOf(output));
    }

    public boolean craft(String recipeName, InventoryManager inventoryManager) {
        Map<String, Integer> input = recipes.get(recipeName + "#input");
        Map<String, Integer> output = recipes.get(recipeName + "#output");
        if (input == null || output == null) {
            return false;
        }
        if (!inventoryManager.hasAll(input)) {
            return false;
        }
        for (Map.Entry<String, Integer> entry : input.entrySet()) {
            inventoryManager.removeItem(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Integer> entry : output.entrySet()) {
            inventoryManager.addItem(entry.getKey(), entry.getValue());
        }
        return true;
    }
}
