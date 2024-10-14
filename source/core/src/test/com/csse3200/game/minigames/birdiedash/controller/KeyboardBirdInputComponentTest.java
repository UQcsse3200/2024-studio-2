package com.csse3200.game.minigames.birdiedash.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.badlogic.gdx.Input.Keys;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class KeyboardBirdInputComponentTest {

    private KeyboardBirdInputComponent inputComponent;
    private Entity mockEntity;
    private EventHandler mockEvents;

    @BeforeEach
    public void setUp() {
        inputComponent = new KeyboardBirdInputComponent();

        // Mock the entity and its event handler
        mockEntity = mock(Entity.class);
        mockEvents = mock(EventHandler.class);

        // Set the entity and its event handler for the component
        when(mockEntity.getEvents()).thenReturn(mockEvents);
        inputComponent.setEntity(mockEntity);
    }

    @Test
    public void testKeyDown_flapEventTriggered() {
        // Test for SPACE key press
        boolean result = inputComponent.keyDown(Keys.SPACE);
        assertTrue(result);
        verify(mockEvents, times(1)).trigger("flap");

        // Test for W key press
        result = inputComponent.keyDown(Keys.W);
        assertTrue(result);
        verify(mockEvents, times(2)).trigger("flap"); // Should be triggered again

        // Test for UP key press
        result = inputComponent.keyDown(Keys.UP);
        assertTrue(result);
        verify(mockEvents, times(3)).trigger("flap");
    }

    @Test
    public void testKeyDown_noEventTriggered() {
        // Test for a key that should not trigger any event
        boolean result = inputComponent.keyDown(Keys.A);
        assertFalse(result);
        verify(mockEvents, never()).trigger("flap");
    }

    @Test
    public void testKeyDown_componentDisabled() {
        // Disable the component
        inputComponent.setEnabled(false);

        // Test for SPACE key press when the component is disabled
        boolean result = inputComponent.keyDown(Keys.SPACE);
        assertFalse(result);
        verify(mockEvents, never()).trigger("flap");
    }
}

