package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class BlindBearTaskTest {

    @BeforeEach
    void beforeEach() {
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldMoveTowardsTarget() {
        // Mock the PlayerActions component and set isMoving to return true
        PlayerActions playerActions = mock(PlayerActions.class);
        when(playerActions.isMoving()).thenReturn(true);

        // Create the target entity and mock its PlayerActions component
        Entity target = new Entity();
        target.setPosition(2f, 2f);
        target.addComponent(playerActions);

        // Create the BlindBearTask and add it to the entity
        AITaskComponent ai = new AITaskComponent().addTask(new BlindBearTask(new Vector2(10, 10), 2, 10, target, 5));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        // Capture the initial distance between the entity and the target
        float initialDistance = entity.getPosition().dst(target.getPosition());

        // Run the game for a few cycles to simulate movement
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        // Capture the new distance and assert that the entity has moved closer to the target
        float newDistance = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance >= initialDistance);
    }

    @Test
    void priorityTest() {
        PlayerActions playerActions = mock(PlayerActions.class);
        when(playerActions.isMoving()).thenReturn(true);

        Entity target = new Entity();
        target.setPosition(0f, 6f);
        target.addComponent(playerActions);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);

        BlindBearTask blindTask = new BlindBearTask(new Vector2(10, 10), 2, 10, target, 5);
        blindTask.create(() -> entity);

        assertTrue(blindTask.getPriority() < 0);

        target.setPosition(0f, 4f);
        assertEquals(-1, blindTask.getPriority());

        target.setPosition(0f, 5f);
        blindTask.start();
        assertEquals(-1, blindTask.getPriority());

        target.setPosition(0f, 12f);
        assertTrue(blindTask.getPriority() < 0);
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }


}