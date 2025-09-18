package com.example.autoplayer.task;

import com.example.autoplayer.logic.AutomationEngine;
import com.example.autoplayer.logic.CraftingManager;
import com.example.autoplayer.logic.InventoryManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquipDiamondSetTaskTest {

    @Test
    void craftsFullDiamondLoadoutWithoutStalling() {
        InventoryManager inventory = new InventoryManager();
        inventory.addItem("diamond", 27);
        inventory.addItem("stick", 10);

        CraftingManager craftingManager = new CraftingManager();
        AutomationEngine automationEngine = new AutomationEngine(inventory, craftingManager);
        TaskContext context = new TaskContext(null, inventory, craftingManager, automationEngine,
                null, null, null, null, null, null, null, null);

        EquipDiamondSetTask task = new EquipDiamondSetTask();
        task.onStart(context);

        List<String> statuses = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            task.tick(context);
            String status = task.getStatus(context).orElse("");
            statuses.add(status);
            if (task.isComplete(context) && "Diamond champion ready".equals(status)) {
                break;
            }
        }

        assertTrue(task.isComplete(context), "Task should complete once all items are equipped");
        assertEquals("Diamond champion ready", task.getStatus(context).orElse(""));
        assertFalse(statuses.contains("Waiting for diamonds from mining"),
                "Task should not stall waiting for diamonds after crafting has begun");

        for (String piece : List.of("diamond_helmet", "diamond_chestplate", "diamond_leggings", "diamond_boots")) {
            assertTrue(inventory.isEquipped(piece), () -> piece + " should be equipped");
        }
        assertTrue(inventory.isEquipped("diamond_pickaxe"), "Pickaxe should be equipped");
    }

    @Test
    void waitsUntilEnoughDiamondsRemainForPickaxe() {
        InventoryManager inventory = new InventoryManager();
        inventory.addItem("stick", 10);
        for (String piece : List.of("diamond_helmet", "diamond_chestplate", "diamond_leggings", "diamond_boots")) {
            inventory.addItem(piece, 1);
            inventory.equip(piece);
        }
        inventory.addItem("diamond", 2);

        CraftingManager craftingManager = new CraftingManager();
        AutomationEngine automationEngine = new AutomationEngine(inventory, craftingManager);
        TaskContext context = new TaskContext(null, inventory, craftingManager, automationEngine,
                null, null, null, null, null, null, null, null);

        EquipDiamondSetTask task = new EquipDiamondSetTask();
        task.onStart(context);

        task.tick(context);
        assertEquals("Waiting for diamonds from mining", task.getStatus(context).orElse(""));
        assertFalse(task.isComplete(context));

        inventory.addItem("diamond", 1);

        List<String> statuses = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            task.tick(context);
            String status = task.getStatus(context).orElse("");
            statuses.add(status);
            if (task.isComplete(context) && "Diamond champion ready".equals(status)) {
                break;
            }
        }

        assertTrue(statuses.contains("Crafting diamond pickaxe"));
        assertTrue(statuses.contains("Equipping diamond pickaxe"));
        assertEquals("Diamond champion ready", task.getStatus(context).orElse(""));
        assertTrue(task.isComplete(context));
    }
}
