package com.csse3200.game.components.inventory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;

import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class PlayerInventoryDisplayTest {
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

        Stage stage = ServiceLocator.getRenderService().getStage();
        when(renderService.getStage()).thenReturn(stage);

        // Mock PlayerInventoryDisplay
        PlayerInventoryDisplay mockInventoryDisplay = mock(PlayerInventoryDisplay.class);

        // Initialize the player entity and add the mocked inventory display
        player = new Entity();
        player.addComponent(mockInventoryDisplay);

        // Load all resources
        resourceService.loadAll();
    }

    @Test
    void testCreate() {
        player.create();
    }
}
