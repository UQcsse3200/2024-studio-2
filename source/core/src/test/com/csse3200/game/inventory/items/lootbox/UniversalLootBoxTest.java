package com.csse3200.game.inventory.items.lootbox;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.csse3200.game.components.inventory.InventoryComponent;
import com.csse3200.game.components.inventory.PlayerInventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.lootbox.UniversalLootBox;
import com.csse3200.game.inventory.items.lootbox.configs.BaseLootTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class UniversalLootBoxTest {

    @Mock
    BaseLootTable lootTable;

    @Mock
    Entity player;

    @Mock
    AbstractItem mockItem1, mockItem2;

    @Mock
    InventoryComponent inventoryComponent;  // Mock InventoryComponent

    @Mock
    Inventory inventory;  // Mock Inventory

    @Mock
    PlayerInventoryDisplay playerInventoryDisplay;  // Mock PlayerInventoryDisplay

    @Mock
    EventHandler eventHandler;  // Mock EventHandler

    UniversalLootBox lootBox;

    @BeforeEach
    void setUp() {
        // Initialize the UniversalLootBox and inject the dependencies
        lootBox = new UniversalLootBox(lootTable, 3, player, "Test description", "test/path/to/image.png", 1001);

        // Mock the player’s components
        when(player.getComponent(InventoryComponent.class)).thenReturn(inventoryComponent);
        when(player.getComponent(PlayerInventoryDisplay.class)).thenReturn(playerInventoryDisplay);
        // Mock InventoryComponent's getInventory to return the mocked Inventory
        when(inventoryComponent.getInventory()).thenReturn(inventory);
        // Mock InventoryComponent's getEntity to return the mocked player entity
        when(inventoryComponent.getEntity()).thenReturn(player);
        // Mock PlayerInventoryDisplay's getToggle to return a valid boolean
        when(playerInventoryDisplay.getToggle()).thenReturn(true);
        // Mock the event handler for the player's entity
        when(player.getEvents()).thenReturn(eventHandler);
        when(lootTable.getRandomItems(3)).thenReturn(Arrays.asList(mockItem1, mockItem2));
    }


    @Test
    void testUseLootBox() {
        // Mock loot table returning two items

        // Call useItem (you can pass null for context or use a valid mocked context)
        lootBox.useItem(null);

        // Verify that deleteItem is called on the inventory
        verify(inventory).deleteItem(1001);

        // Verify that PlayerInventoryDisplay's getToggle() is called
        verify(playerInventoryDisplay).getToggle();

        // Verify that events are triggered properly
        verify(player, times(4)).getEvents();
        verify(eventHandler, atLeastOnce()).trigger(anyString(), any());
    }
}