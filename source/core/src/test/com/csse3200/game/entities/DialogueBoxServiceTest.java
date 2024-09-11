package com.csse3200.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.DialogueBox;
import com.csse3200.game.entities.DialogueBoxService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(GameExtension.class)
class DialogueBoxServiceTest {
    private DialogueBoxService entityChatService;
    private static final Skin SKIN = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
    
    Stage stage;
    @BeforeEach
    void beforeEach() {
        EntityService entityService = new EntityService();
        GameTime gameTime = new GameTime();
        ResourceService resourceService = new ResourceService();
        resourceService.loadAll();
        
        RenderService renderService = mock(RenderService.class);
        when(renderService.getStage()).thenReturn(mock(Stage.class));
        InputService inputService = new InputService();
        ServiceLocator.registerInputService(inputService);
        
        // Register services with ServiceLocator
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerRenderService(renderService);
        
        Stage stage = ServiceLocator.getRenderService().getStage();
        // Mock the behavior of RenderService to return the Stage instance
        when(renderService.getStage()).thenReturn(stage);
        entityChatService = new DialogueBoxService(stage);
        ServiceLocator.registerDialogueBoxService(entityChatService);
        this.stage = stage;
    }
    
    @Test
    void hideChatBox() {
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        Assertions.assertFalse(entityChatService.getCurrentOverlay().getLabel().isVisible());
        entityChatService.updateText(new String[][] {{"1", "2"}});
        Assertions.assertTrue(entityChatService.getCurrentOverlay().getLabel().isVisible());
        Assertions.assertTrue(entityChatService.getCurrentOverlay().getForwardButton().isVisible());
        Assertions.assertTrue(entityChatService.getCurrentOverlay().getBackwardButton().isVisible());
        entityChatService.hideCurrentOverlay();
        Assertions.assertFalse(entityChatService.getCurrentOverlay().getLabel().isVisible());
    }
    
    @Test
    void shouldCreateEntityChat() {
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        entityChatService.updateText(new String[][] {{"1", "2"}});
        Assertions.assertArrayEquals(new String[][] {{"1", "2"}}, entityChatService.getHints());
    }
    
    @Test
    void testButtonPresses() {
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                entityChatService.updateText(new String[][]{{"1", "2"}});
            } else {
                entityChatService.updateText(new String[]{"1", "2"});
            }
            entityChatService.getCurrentOverlay().handleForwardButtonClick();
            Assertions.assertEquals("2", entityChatService.getCurrentOverlay().getLabel().getText().toString());
            entityChatService.getCurrentOverlay().handleForwardButtonClick();
            Assertions.assertEquals("1", entityChatService.getCurrentOverlay().getLabel().getText().toString());
            entityChatService.getCurrentOverlay().handleBackwardButtonClick();
            Assertions.assertEquals("2", entityChatService.getCurrentOverlay().getLabel().getText().toString());
            entityChatService.getCurrentOverlay().handleBackwardButtonClick();
            Assertions.assertEquals("1", entityChatService.getCurrentOverlay().getLabel().getText().toString());
        }
    }

    @Test
    void testOptionButtonPresses() {
        String[][] dialogueOptions = new String[][] {{"/cOptions Dialogue/s01option1/s02option2/s03option3"},
                {"1"}, {"2"}, {"3"}, {"4"}, {"5"}};
        entityChatService.updateText(dialogueOptions);
        entityChatService.getCurrentOverlay().handleOptionButtonClick(0);
        Assertions.assertEquals("1", entityChatService.getCurrentOverlay().getLabel().getText().toString());
        entityChatService.updateText(dialogueOptions);
        entityChatService.getCurrentOverlay().handleOptionButtonClick(1);
        Assertions.assertEquals("2", entityChatService.getCurrentOverlay().getLabel().getText().toString());
        entityChatService.updateText(dialogueOptions);
        entityChatService.getCurrentOverlay().handleOptionButtonClick(2);
        Assertions.assertEquals("3", entityChatService.getCurrentOverlay().getLabel().getText().toString());
        entityChatService.updateText(dialogueOptions);
        entityChatService.getCurrentOverlay().handleOptionButtonClick(3);
        Assertions.assertEquals("4", entityChatService.getCurrentOverlay().getLabel().getText().toString());
        entityChatService.updateText(dialogueOptions);
        entityChatService.getCurrentOverlay().handleOptionButtonClick(4);
        Assertions.assertEquals("5", entityChatService.getCurrentOverlay().getLabel().getText().toString());
    }
    
    @Test
    void shouldRemoveEntityChat() {
        entityChatService.updateText(new String[][] {{"1", "2"}});
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
        entityChatService.updateText(new String[][] {{"This is a test 1 String", "This is a test 2 String"}});
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        
        DialogueBox chatOverlay = entityChatService.getCurrentOverlay();
        
        String page1 = chatOverlay.getLabel().getText().toString();
        String hint1 = chatOverlay.getHints()[chatOverlay.getCurrentHintLine()][chatOverlay.getCurrentHint()];
        Assertions.assertEquals("This is a test 1 String", page1);
        Assertions.assertEquals("This is a test 1 String", hint1);
    }
    
    @Test
    void buttonsExist() {
        entityChatService.updateText(new String[][] {{"This is a test 1 String", "This is a test 2 String"}});
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        Assertions.assertNotNull(entityChatService.getCurrentOverlay().getForwardButton());
        Assertions.assertNotNull(entityChatService.getCurrentOverlay().getBackwardButton());
    }
}