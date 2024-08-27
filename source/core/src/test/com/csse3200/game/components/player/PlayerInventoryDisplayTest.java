package com.csse3200.game.components.player;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
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
        Stage stage = ServiceLocator.getRenderService().getStage();

        // Mock the behavior of RenderService to return the Stage instance
        when(renderService.getStage()).thenReturn(stage);

        // Load all resources
        resourceService.loadAll();

        Gdx.input.setInputProcessor(stage);
    }

    @Test
    void testInitialisation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new PlayerInventoryDisplay(9, 7);
        });

        new PlayerInventoryDisplay(9, 3);
    }
}