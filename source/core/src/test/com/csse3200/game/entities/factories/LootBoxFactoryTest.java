package com.csse3200.game.entities.factories;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.entities.factories.LootBoxFactoryTest;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.lootbox.UniversalLootBox;
import com.csse3200.game.inventory.items.lootbox.configs.BaseLootTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class LootBoxFactoryTest {

    @Mock
    Entity player;

    @Mock
    BaseLootTable mockLootTable;

    LootBoxFactory factory;

    @BeforeAll
    static void setUpGraphicsMock() {
        // Mock Gdx graphics for headless environment
        Gdx.graphics = mock(com.badlogic.gdx.Graphics.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(0.016f); // Set default delta time
    }

    @BeforeEach
    void setUp() {
        // Initialize LootBoxFactory
        factory = new LootBoxFactory();
    }

    @Test
    void testCreateEarlyGameLootBox() throws ClassNotFoundException {
        // Assuming the JSON config returns the correct loot box type for "EarlyGameLootBox"
        // You might mock the behavior of reading JSON if needed, or use a real JSON string for the test.

        UniversalLootBox lootBox = factory.createLootBox("EarlyGameLootBox", player);

        // Verify that the created loot box has the correct properties
        assertNotNull(lootBox);
        assertEquals("This is an EarlyGameLootBox, Open it Up for Goodies!!!!", lootBox.getDescription());
        assertEquals("images/chests/ordinary-chest.png", lootBox.getTexturePath());
        assertEquals(player, lootBox.getPlayer());
    }

    @Test
    void testCreateLootBoxWithInvalidName() throws ClassNotFoundException {
        // Test for handling an invalid loot box name
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.createLootBox("InvalidName", player);
        });

        assertEquals("Loot box not found: InvalidName", exception.getMessage());
    }
}