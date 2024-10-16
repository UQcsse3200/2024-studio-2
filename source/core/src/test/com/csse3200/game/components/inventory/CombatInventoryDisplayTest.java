package com.csse3200.game.components.inventory;

import com.csse3200.game.GdxGame;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;


import static org.mockito.Mockito.*;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;

@ExtendWith(GameExtension.class)
class CombatInventoryDisplayTest {
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


}