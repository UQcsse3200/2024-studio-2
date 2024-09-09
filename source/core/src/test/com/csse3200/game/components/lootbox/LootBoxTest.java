package com.csse3200.game.components.lootbox;

import com.csse3200.game.components.player.PlayerInventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.lootbox.LootBox;
import com.csse3200.game.inventory.items.lootbox.configs.EarlyGameLootTable;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.input.InputService;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class LootBoxTest {
    private Entity player;

    @BeforeEach
    void beforeEach() {
        // Set up services
        ResourceService resourceService = new ResourceService();
        RenderService renderService = mock(RenderService.class);
        when(renderService.getStage()).thenReturn(mock(Stage.class));
        InputService inputService = new InputService();

        // Register services with ServiceLocator
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerInputService(inputService);

        // Mock PlayerInventoryDisplay
        PlayerInventoryDisplay mockInventoryDisplay = mock(PlayerInventoryDisplay.class);

        // Initialize the player entity and add the mocked inventory display
        player = new Entity();
        player.addComponent(mockInventoryDisplay);

        // Load all resources
        resourceService.loadAll();
    }

    @Test
    void testLootBoxInitialization() {
        // Create a loot table and a loot box
        EarlyGameLootTable lootTable = new EarlyGameLootTable();
        LootBox lootBox = new LootBox(lootTable, 3, player);

        // Check that the loot box initializes correctly
        assertNotNull(lootBox);
    }

    @Test
    void testLootBoxOpenReturnsCorrectNumberOfItems() {
        // Create a loot table and a loot box
        EarlyGameLootTable lootTable = new EarlyGameLootTable();
        LootBox lootBox = new LootBox(lootTable, 3, player);

        // Use the open method
        List<AbstractItem> items = lootBox.open();

        // Verify that three items are returned
        assertNotNull(items);
        assertEquals(3, items.size(), "The loot box should return exactly 3 items.");
    }
}