package com.csse3200.game.components.player;

import com.csse3200.game.components.inventory.PlayerInventoryDisplay;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.inventory.Inventory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;

@ExtendWith(GameExtension.class)
class PlayerInventoryDisplayTest {
    @BeforeEach
    void beforeEach() {
        ResourceService resourceService = new ResourceService();
        RenderService renderService = mock(RenderService.class);
        when(renderService.getStage()).thenReturn(mock(Stage.class));
        InputService inputService = new InputService();

        // Register services with ServiceLocator
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerInputService(inputService);

        // Load all resources
        resourceService.loadAll();

        Gdx.input.setInputProcessor(renderService.getStage());
    }

    @Test
    void testInitialisation() {
        Inventory inv1 = new Inventory(9);
        Inventory inv2 = new Inventory(10);

        // Should throw error since 7 does not divide 9
        assertThrows(IllegalArgumentException.class,
                () -> new PlayerInventoryDisplay(inv1, 7, 1));
        assertThrows(IllegalArgumentException.class, () -> {
            new PlayerInventoryDisplay(inv2, 0, 1);
        });

        // Shouldn't throw error since 3 divides 12
        new PlayerInventoryDisplay(new Inventory(9), 3, 3);
    }
}