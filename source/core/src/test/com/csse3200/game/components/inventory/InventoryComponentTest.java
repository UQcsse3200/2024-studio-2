package com.csse3200.game.components.inventory;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.gamestate.GameState;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csse3200.game.inventory.items.AbstractItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {
    @Test
    void testAll() {
        InventoryComponent component = new InventoryComponent(5);
        assertEquals(5, component.getInventory().getCapacity());

        GameState.inventory.inventoryContent = new AbstractItem[10];
        component.loadInventoryFromSave();
        assertEquals(10, component.getInventory().getCapacity());
    }
}
