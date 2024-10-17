package com.csse3200.game.components.player;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.badlogic.gdx.Input;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.utils.math.Vector2Utils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class TouchPlayerInputComponentTest {

    private TouchPlayerInputComponent inputComponent;
    private Entity mockEntity;
    private EventHandler mockEventHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockEntity = mock(Entity.class);
        mockEventHandler = mock(EventHandler.class);

        when(mockEntity.getEvents()).thenReturn(mockEventHandler);

        inputComponent = new TouchPlayerInputComponent();
        inputComponent.entity = mockEntity;
        inputComponent.setEnabled(true);
    }

    @Test
    public void testKeyDownUpArrowTriggerWalkEvent() {
        assertTrue(inputComponent.keyDown(Input.Keys.UP));
        verify(mockEventHandler).trigger("walk", Vector2Utils.UP);
    }

    @Test
    public void testKeyDownLeftArrowTriggerWalkEvent() {
        assertTrue(inputComponent.keyDown(Input.Keys.LEFT));
        verify(mockEventHandler).trigger("walk", Vector2Utils.LEFT);
    }

    @Test
    public void testKeyUpStopsWalkWhenNoDirectionPressed() {
        inputComponent.keyDown(Input.Keys.UP);
        assertTrue(inputComponent.keyUp(Input.Keys.UP));
        verify(mockEventHandler).trigger("walkStop");
    }

    @Test
    public void testKeyDownWithComponentDisabledDoesNothing() {
        inputComponent.setEnabled(false);
        assertFalse(inputComponent.keyDown(Input.Keys.UP));
        verify(mockEventHandler, never()).trigger(anyString(), any());
    }

    @Test
    public void testTouchDownTriggersAttack() {
        assertTrue(inputComponent.touchDown(0, 0, 0, Input.Buttons.LEFT));
        verify(mockEventHandler).trigger("attack");
    }
}
