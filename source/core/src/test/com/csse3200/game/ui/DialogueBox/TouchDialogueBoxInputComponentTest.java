package com.csse3200.game.ui.DialogueBox;

import com.badlogic.gdx.Input;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.DialogueBox.TouchDialogueBoxInputComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class TouchDialogueBoxInputComponentTest {
    private TouchDialogueBoxInputComponent inputComponent;
    private DialogueBoxService dialogueBoxService;
    private DialogueBox mockOverlay;

    @BeforeEach
    void setUp() {
        // Initialize the input component
        inputComponent = new TouchDialogueBoxInputComponent();

        // Create mocks
        dialogueBoxService = mock(DialogueBoxService.class);
        mockOverlay = mock(DialogueBox.class);

        // Setup mock behavior
        when(dialogueBoxService.getIsVisible()).thenReturn(true);
        when(dialogueBoxService.getCurrentOverlay()).thenReturn(mockOverlay);

        // Register the mock DialogueBoxService with ServiceLocator
        ServiceLocator.registerDialogueBoxService(dialogueBoxService);
    }

    @Test
    void testKeyDownRightArrow() {
        // Act
        inputComponent.keyDown(Input.Keys.RIGHT);

        // Assert
        verify(dialogueBoxService).getCurrentOverlay();
        verify(mockOverlay).handleForwardButtonClick();
    }

    @Test
    void testKeyDownLeftArrow() {
        // Act
        inputComponent.keyDown(Input.Keys.LEFT);

        // Assert
        verify(dialogueBoxService).getCurrentOverlay();
        verify(mockOverlay).handleBackwardButtonClick();
    }

    @Test
    void testKeyUp() {
        // Arrange
        inputComponent.keyDown(Input.Keys.RIGHT);
        // Act
        boolean result = inputComponent.keyUp(Input.Keys.RIGHT);

        // Assert
        assertTrue(result, "Key release should be processed.");
    }

    @Test
    void testKeyDownWhenDisabled() {
        // Arrange
        inputComponent.setEnabled(false); // Assuming there is a method to disable the component

        // Act
        boolean result = inputComponent.keyDown(Input.Keys.RIGHT);

        // Assert
        assertTrue(!result, "Input should not be processed when disabled.");
    }
}
