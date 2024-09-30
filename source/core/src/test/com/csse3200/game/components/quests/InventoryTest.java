package com.csse3200.game.components.quests;

import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.potions.DefensePotion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryCollectionTest {
    @Test
    void itemCollectionSuccessful() {
        Inventory inventory = new Inventory(10);
        inventory.add(new DefensePotion(1));
        assertFalse(inventory.itemCollectionSuccessful());
        inventory.questItemListen("Defense Potion", 1, "collectPotions");
        assertTrue(inventory.itemCollectionSuccessful());
    }
}