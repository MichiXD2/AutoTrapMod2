package com.example.autoplayer.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Lightweight model of the player inventory that the automation logic can manipulate.
 */
public class InventoryManager {

    private final Map<String, Integer> items = new HashMap<>();
    private final Set<String> equippedItems = new HashSet<>();

    public InventoryManager() {
        items.put("torch", 16);
    }

    public Map<String, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public Set<String> getEquippedItems() {
        return Collections.unmodifiableSet(equippedItems);
    }

    public int getCount(String itemId) {
        return items.getOrDefault(itemId, 0);
    }

    public void addItem(String itemId, int amount) {
        items.merge(itemId, amount, Integer::sum);
    }

    public boolean removeItem(String itemId, int amount) {
        int current = items.getOrDefault(itemId, 0);
        if (current < amount) {
            return false;
        }
        if (current == amount) {
            items.remove(itemId);
        } else {
            items.put(itemId, current - amount);
        }
        return true;
    }

    public boolean hasItem(String itemId, int amount) {
        return getCount(itemId) >= amount;
    }

    public boolean hasAll(Map<String, Integer> requirements) {
        for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
            if (!hasItem(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public void equip(String itemId) {
        if (items.containsKey(itemId)) {
            equippedItems.add(itemId);
        }
    }

    public boolean isEquipped(String itemId) {
        return equippedItems.contains(itemId);
    }
}
