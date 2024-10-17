package com.csse3200.game.entities;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.dialoguebox.DialogueBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;


@ExtendWith(GameExtension.class)
class DialogueBoxServiceTest {
    private DialogueBoxService entityChatService;
    Stage stage;
    /**
     * Sets up the necessary services needed for the tests.
     * Also creates the NPCs being tested both friendly and converted.
     */
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

        stage = ServiceLocator.getRenderService().getStage();

        // Mock the behavior of RenderService to return the Stage instance
        when(renderService.getStage()).thenReturn(stage);
        entityChatService = new DialogueBoxService(stage);
        ServiceLocator.registerDialogueBoxService(entityChatService);
    }

    /**
     * Tests that the dialogue box is visible when it exists.
     */
    @Test
    void shouldReturnCorrectVisibilityBoolean() {
        DialogueBox mockCurrentOverlay = mock(DialogueBox.class);

        when(mockCurrentOverlay.getIsVisible()).thenReturn(true);
        Assertions.assertTrue(mockCurrentOverlay.getIsVisible());
        when(mockCurrentOverlay.getIsVisible()).thenReturn(false);
        Assertions.assertFalse(mockCurrentOverlay.getIsVisible());
    }

    /**
     * Tests that the NPC is highlighted when interacting with the player.
     */
    @Test
    void shouldHighlightEntitySprite() {
        String[][] text = {{"Test 1"}, {"Test 2"}};

        Entity mockEntity = mock(Entity.class);
        AnimationRenderComponent mockAnimator = mock(AnimationRenderComponent.class);
        when(mockEntity.getComponent(AnimationRenderComponent.class)).thenReturn(mockAnimator);

        entityChatService.updateText(text, mockEntity, DialogueBoxService.DialoguePriority.NONEDEFAULT);

        verify(mockAnimator).startAnimation("selected");
        Assertions.assertArrayEquals(text, entityChatService.getHints());

    }

    /**
     * Tests that the NPC is no longer highlighted when the player is not interacting with them.
     */
    @Test
    void shouldUnhighlightEntitySprite() {
        String[][] oldText = {{"Old Test"}};
        String[][] newText = {{"New Test"}};

        Entity mockEntity = mock(Entity.class);
        AnimationRenderComponent mockAnimator = mock(AnimationRenderComponent.class);
        when(mockEntity.getComponent(AnimationRenderComponent.class)).thenReturn(mockAnimator);

        entityChatService.updateText(oldText, mockEntity, DialogueBoxService.DialoguePriority.NONEDEFAULT);

        entityChatService.updateText(newText, DialogueBoxService.DialoguePriority.NONEDEFAULT);

        verify(mockAnimator).startAnimation("float");
    }

    /**
     * Tests that the NPC does not become unhighlighted when there are no other entities around.
     */
    @Test
    void shouldNotStartFloatAnimationIfNoPreviousEntity() {
        String[][] oldText = {{"Old Test"}};
        String[][] newText = {{"New Test"}};
        Entity mockEntity = mock(Entity.class);
        AnimationRenderComponent mockAnimator = mock(AnimationRenderComponent.class);
        when(mockEntity.getComponent(AnimationRenderComponent.class)).thenReturn(mockAnimator);

        entityChatService.updateText(oldText, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        entityChatService.updateText(newText, DialogueBoxService.DialoguePriority.NONEDEFAULT);

        verify(mockAnimator, never()).startAnimation("float");
    }

    /**
     * Tests that the NPC should be unhighlighted and the new NPC will be highlighted.
     * This imitates the end of interaction with one and the start of another interaction with another NPC.
     */
    @Test
    void shouldUnhighlightEntitySpriteAndHighlightNewEntity() {
        String[][] oldText = {{"Test 1"}, {"Test 2"}};
        String[][] newText = {{"Test 3"}, {"Test 4"}};

        Entity previousEntity = mock(Entity.class);
        AnimationRenderComponent previousAnimator = mock(AnimationRenderComponent.class);
        when(previousEntity.getComponent(AnimationRenderComponent.class)).thenReturn(previousAnimator);
        entityChatService.updateText(oldText, previousEntity, DialogueBoxService.DialoguePriority.NONEDEFAULT);

        Entity newEntity = mock(Entity.class);
        AnimationRenderComponent newAnimator = mock(AnimationRenderComponent.class);
        when(newEntity.getComponent(AnimationRenderComponent.class)).thenReturn(newAnimator);

        entityChatService.updateText(newText, newEntity, DialogueBoxService.DialoguePriority.NONEDEFAULT);

        verify(previousAnimator).startAnimation("float");
        verify(newAnimator).startAnimation("selected");
        Assertions.assertArrayEquals(newText, entityChatService.getHints());
    }

    /**
     * Tests that the animation for the NPCs start when interacting with the player, given there is an animation.
     */
    @Test
    void shouldStartAnimationWhenAnimatorIsNotNull() {
        AnimationRenderComponent mockAnimator = mock(AnimationRenderComponent.class);
        Entity mockEntity = mock(Entity.class);
        when(mockEntity.getComponent(AnimationRenderComponent.class)).thenReturn(mockAnimator);

        entityChatService.updateText(new String[][] {{"Test 1"}}, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        entityChatService.updateText(new String[][] {{"Test 2"}}, mockEntity, DialogueBoxService.DialoguePriority.NONEDEFAULT);

        verify(mockAnimator).startAnimation("selected");
    }

    /**
     * Tests that the dialogue box is hidden when entityChatService.hideCurrentOverlay() is called.
     */
    @Test
    void hideChatBox() {
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        Assertions.assertFalse(entityChatService.getCurrentOverlay().getLabel().isVisible());
        entityChatService.updateText(new String[][] {{"1", "2"}}, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        Assertions.assertTrue(entityChatService.getCurrentOverlay().getLabel().isVisible());
        Assertions.assertTrue(entityChatService.getCurrentOverlay().getForwardButton().isVisible());
        Assertions.assertFalse(entityChatService.getCurrentOverlay().getBackwardButton().isVisible());
        entityChatService.getCurrentOverlay().handleForwardButtonClick();
        Assertions.assertTrue(entityChatService.getCurrentOverlay().getForwardButton().isVisible());
        Assertions.assertTrue(entityChatService.getCurrentOverlay().getBackwardButton().isVisible());
        entityChatService.hideCurrentOverlay();
        Assertions.assertFalse(entityChatService.getCurrentOverlay().getLabel().isVisible());
    }

    /**
     * Tests that the dialogue box is created when an NPC is interacting with the player.
     */
    @Test
    void shouldCreateEntityChat() {
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        entityChatService.updateText(new String[][] {{"1", "2"}}, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        Assertions.assertArrayEquals(new String[][] {{"1", "2"}}, entityChatService.getHints());
    }

    /**
     * Tests that the buttons to cycle the hints exist.
     */
    @Test
    void buttonsExist() {
        entityChatService.updateText(new String[][] {{"This is a test 1 String", "This is a test 2 String"}}, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        Assertions.assertNotNull(entityChatService.getCurrentOverlay().getForwardButton());
        Assertions.assertNotNull(entityChatService.getCurrentOverlay().getBackwardButton());
    }

    /**
     * Tests that the buttons work aas intended, cycling through the hints that the NPC has.
     */
    @Test
    void testButtonPresses() {
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        entityChatService.updateText(new String[][]{{"1", "2"}}, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        entityChatService.getCurrentOverlay().handleForwardButtonClick();
        Assertions.assertEquals("2", entityChatService.getCurrentOverlay().getLabel().getText().toString());
        entityChatService.getCurrentOverlay().handleBackwardButtonClick();
        Assertions.assertEquals("1", entityChatService.getCurrentOverlay().getLabel().getText().toString());
    }

    /**
     * Tests that the dialogue cycles through different lists when a different option buttion is pressed.
     */
    @Test
    void testOptionButtonPresses() {
        String[][] dialogueOptions = new String[][] {{"/cOptions Dialogue/s01option1/s02option2/s03option3"},
                {"1"}, {"2"}, {"3"}, {"4"}, {"5"}};
        entityChatService.updateText(dialogueOptions, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        entityChatService.getCurrentOverlay().handleOptionButtonClick(0);
        Assertions.assertEquals("1", entityChatService.getCurrentOverlay().getLabel().getText().toString());
        entityChatService.updateText(dialogueOptions, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        entityChatService.getCurrentOverlay().handleOptionButtonClick(1);
        Assertions.assertEquals("2", entityChatService.getCurrentOverlay().getLabel().getText().toString());
        entityChatService.updateText(dialogueOptions, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        entityChatService.getCurrentOverlay().handleOptionButtonClick(2);
        Assertions.assertEquals("3", entityChatService.getCurrentOverlay().getLabel().getText().toString());
        entityChatService.updateText(dialogueOptions, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        entityChatService.getCurrentOverlay().handleOptionButtonClick(3);
        Assertions.assertEquals("4", entityChatService.getCurrentOverlay().getLabel().getText().toString());
        entityChatService.updateText(dialogueOptions, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        entityChatService.getCurrentOverlay().handleOptionButtonClick(4);
        Assertions.assertEquals("5", entityChatService.getCurrentOverlay().getLabel().getText().toString());
    }

    /**
     * Tests that the disposal of a dialogue overlay will remove it entirely.
     */
    @Test
    void shouldRemoveEntityChat() {
        entityChatService.updateText(new String[][] {{"1", "2"}}, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());
        entityChatService.disposeCurrentOverlay();
        Assertions.assertNull(entityChatService.getCurrentOverlay());
    }

    /**
     * Tests that trying to dispose of an overlay that's already disposed will do nothing.
     */
    @Test
    void illegalDispose() {
        entityChatService.disposeCurrentOverlay();
        Assertions.assertNull(entityChatService.getCurrentOverlay());
    }

    /**
     * Tests that the texts should be updated accordingly and the text shown matches.
     */
    @Test
    void shouldUpdateEntityChat() {
        entityChatService.updateText(new String[][] {{"This is a test 1 String", "This is a test 2 String"}}, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        Assertions.assertNotNull(entityChatService.getCurrentOverlay());

        DialogueBox chatOverlay = entityChatService.getCurrentOverlay();

        String page1 = chatOverlay.getLabel().getText().toString();
        String hint1 = chatOverlay.getHints()[chatOverlay.getCurrentHintLine()][chatOverlay.getCurrentHint()];
        Assertions.assertEquals("This is a test 1 String", page1);
        Assertions.assertEquals("This is a test 1 String", hint1);
    }
}
