package com.csse3200.game.ui.dialoguebox;

import com.badlogic.gdx.Input;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class KeyboardDialogueBoxInputComponentTest {
    private KeyboardDialogueBoxInputComponent inputComponent;
    private DialogueBoxService dialogueBoxService;
    private DialogueBox mockOverlay;

    @BeforeEach
    void setUp() {
        inputComponent = new KeyboardDialogueBoxInputComponent();
        dialogueBoxService = mock(DialogueBoxService.class);
        mockOverlay = mock(DialogueBox.class);
        when(dialogueBoxService.getIsVisible()).thenReturn(true);
        when(dialogueBoxService.getCurrentOverlay()).thenReturn(mockOverlay);
        ServiceLocator.registerDialogueBoxService(dialogueBoxService);
    }

    @Test
    void testKeyDownRightArrow() {
        inputComponent.keyDown(Input.Keys.RIGHT);
        verify(dialogueBoxService).getCurrentOverlay();
        verify(mockOverlay).handleForwardButtonClick();
    }

    @Test
    void testKeyDownLeftArrow() {
        inputComponent.keyDown(Input.Keys.LEFT);
        verify(dialogueBoxService).getCurrentOverlay();
        verify(mockOverlay).handleBackwardButtonClick();
    }

    @Test
    void testKeyUp() {
        inputComponent.keyDown(Input.Keys.RIGHT);
        boolean result = inputComponent.keyUp(Input.Keys.RIGHT);
        assertTrue(result, "Key release should be processed.");
    }

    @Test
    void testKeyDownWhenDisabled() {
        inputComponent.setEnabled(false);
        boolean result = inputComponent.keyDown(Input.Keys.RIGHT);
        assertTrue(!result, "Input should not be processed when disabled.");
    }
}
