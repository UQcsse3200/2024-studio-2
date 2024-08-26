package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.food.AbstractFood;
import com.csse3200.game.inventory.items.food.Foods;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ChatOverlay;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class ItemProximityTaskTest {
    private Entity target;
    private AbstractItem item;
    private ItemProximityTask task;
    private ChatOverlay itemOverlay;

    @BeforeEach
    void beforeEach() {
        target = mock(Entity.class);
        item = mock(AbstractItem.class);
        itemOverlay = mock(ChatOverlay.class);
        when(target.getPosition()).thenReturn(new Vector2(0f, 0f));

        task = new ItemProximityTask(target, 1, 2.0f, item);
    }

//    @Test
//    void shouldCreateItemOverlayWhenNear() {
//        task.create(() -> target);
//        task.start();
//
//        // Mock the player near the item
//        when(target.getPosition().dst(any(Vector2.class))).thenReturn(2.0f);
//
//        task.update();
//
//        // Check that the overlay was created
//        assertNotNull(task.itemOverlay);
//        verify(target.getEvents(), never()).trigger(anyString(), any());
//    }
//
//    @Test
//    void shouldDisposeItemOverlayWhenFar() {
//        task.create(() -> target);
//        task.start();
//
//        // Mock the player near the item initially
//        when(target.getPosition().dst(any(Vector2.class))).thenReturn(2.0f);
//        task.update();
//
//        assertTrue(task.itemOverlay != null);
//
//        // Now mock the player moving away
//        when(target.getPosition().dst(any(Vector2.class))).thenReturn(2.0f);
//        task.update();
//
//        // Check that the overlay was disposed
//        assertTrue(task.itemOverlay == null);
//        verify(itemOverlay).dispose();
//    }
}
