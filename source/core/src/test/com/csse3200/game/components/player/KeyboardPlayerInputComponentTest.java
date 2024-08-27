package com.csse3200.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class KeyboardPlayerInputComponentTest {

    private KeyboardPlayerInputComponent inputComponent;
    private Entity mockEntity;
    private EventHandler mockEventHandler;

    @BeforeEach
    void setUp() {
        // Create mock entity and event handler
        mockEntity = Mockito.mock(Entity.class);
        mockEventHandler = Mockito.mock(EventHandler.class);
        when(mockEntity.getEvents()).thenReturn(mockEventHandler);

        // Initialize the input component and set its entity
        inputComponent = new KeyboardPlayerInputComponent();
        inputComponent.setEntity(mockEntity);
    }

    @Test
    void testKeyUpTriggerToggleInventoryEvent() {
        // Simulate pressing the 'E' key
        inputComponent.keyUp(Keys.E);

        // Verify that the "toggleInventory" event was triggered
        verify(mockEventHandler).trigger("toggleInventory");
    }

    @Test
    void testKeyUpTriggerPickUpItemEvent() {
        // Simulate pressing the 'P' key
        inputComponent.keyUp(Keys.P);

        // Verify that the "pickUpItem" event was triggered
        verify(mockEventHandler).trigger("pickUpItem");
    }

    @Test
    void testKeyDownOtherKeys() {
        // Simulate pressing other keys (not 'E' or 'P')
        inputComponent.keyDown(Keys.W);
        inputComponent.keyDown(Keys.SPACE);

        // Verify that the "toggleInventory" and "pickUpItem" events were NOT triggered
        verify(mockEventHandler, never()).trigger("toggleInventory");
        verify(mockEventHandler, never()).trigger("pickUpItem");
    }
}