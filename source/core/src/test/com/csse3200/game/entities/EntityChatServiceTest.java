package com.csse3200.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.DialogueBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class EntityChatServiceTest {
    private EntityChatService entityChatService;

    private final String[][] testText = {
            { "Welcome to Animal Kingdom!", "I am Lenny the Lion.", "/cWhich tip do you wanna hear about?/s01What do potions do???/s02How to beat the final boss/s03Nothing. Bye" },
            { "Potions heals you by (n) HP!", "I hope this helped." },
            { "Final boss?? That Kangaroo??", "idk" },
            { "Good luck!" }
    };

    @BeforeEach
    void beforeEach() {
        EntityService entityService = new EntityService();
        GameTime gameTime = new GameTime();
        ResourceService resourceService = new ResourceService();
        RenderService renderService = mock(RenderService.class);
        when(renderService.getStage()).thenReturn(mock(Stage.class));

        // Register services with ServiceLocator
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerRenderService(renderService);
        entityChatService = new EntityChatService();
        ServiceLocator.registerEntityChatService(entityChatService);

        Stage stage = ServiceLocator.getRenderService().getStage();

        // Mock the behavior of RenderService to return the Stage instance
        when(renderService.getStage()).thenReturn(stage);

        // Load all resources
        resourceService.loadAll();

        Gdx.input.setInputProcessor(stage);
    }

    @Test
    void hideChatBox() {
        Assertions.assertNull(entityChatService.getCurrentOverlay());
        entityChatService.updateText(testText);
        Assertions.assertTrue(entityChatService.getCurrentOverlay().getLabel().isVisible());
        Assertions.assertTrue(entityChatService.getCurrentOverlay().getForwardButton().isVisible());
        Assertions.assertTrue(entityChatService.getCurrentOverlay().getBackwardButton().isVisible());
        entityChatService.hideCurrentOverlay();
        Assertions.assertFalse(entityChatService.getCurrentOverlay().getLabel().isVisible());

    }

    @Test
    void shouldCreateEntityChat() {
        Assertions.assertNull(entityChatService.getCurrentOverlay());
        entityChatService.updateText(testText);
        Assertions.assertArrayEquals(testText, entityChatService.getHints());
    }

    @Test
    void shouldRemoveEntityChat() {
        entityChatService.updateText(testText);
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        entityChatService.disposeCurrentOverlay();
        Assertions.assertNull(entityChatService.getCurrentOverlay());
    }

    @Test
    void illegalDispose() {
        entityChatService.disposeCurrentOverlay();
        Assertions.assertNull(entityChatService.getCurrentOverlay());
    }

    @Test
    void shouldUpdateEntityChat() {
        entityChatService.updateText(testText);
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());

        DialogueBox chatOverlay = entityChatService.getCurrentOverlay();

        String page1 = chatOverlay.getLabel().getText().toString();
        Assertions.assertEquals("Welcome to Animal Kingdom!", page1);
        String hint1 = chatOverlay.getHints()[chatOverlay.getCurrentHint()][0];
        Assertions.assertEquals("Welcome to Animal Kingdom!", hint1);
    }

    @Test
    void buttonsExist() {
        entityChatService.updateText(testText);
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        Assertions.assertNotNull(entityChatService.getCurrentOverlay().getForwardButton());
        Assertions.assertNotNull(entityChatService.getCurrentOverlay().getBackwardButton());
    }
}
