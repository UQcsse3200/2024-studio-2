package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.files.FileLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class PauseTaskTest {
    ResourceService resourceService;
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        EventService eventService = new EventService();
        ServiceLocator.registerEventService(eventService);
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        resourceService.loadSounds(new String[]{"sounds/QuestComplete.wav"});
        while (!resourceService.loadForMillis(10)) {
            continue;
        }
    }

    @AfterEach
    void afterEach() {
        resourceService.unloadAssets(new String[]{"sounds/QuestComplete.wav"});
    }

    @Test
    void shouldMoveTowardsTarget() {
        Entity target = new Entity();
                target.addComponent(new QuestManager(target));
        target.setPosition(2f, 2f);
        NPCConfigs configs =
                FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

        AITaskComponent ai = new AITaskComponent()
                .addTask(new PauseTask(target, 10, 10, 5))
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f));

        Entity entity = new Entity()
                .addComponent(ai)
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ConfigComponent<>(configs.lion));

        entity.create();
        entity.setPosition(0f, 0f);

        float initialDistance = entity.getPosition().dst(target.getPosition());

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        float newDistance = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance <= initialDistance);
    }

    @Test
    void shouldChaseOnlyWhenInDistance() {
        Entity target = new Entity();
        target.addComponent(new QuestManager(target));
        target.setPosition(2f, 2f);

        NPCConfigs configs = FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");
        PauseTask pauseTask = new PauseTask(target, 10, 10, 5);

        AITaskComponent ai = new AITaskComponent()
                .addTask(pauseTask)
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f));

        Entity entity = new Entity()
                .addComponent(ai)
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ConfigComponent<>(configs.lion));

        entity.create();

        // Test: When out of view distance, pauseTask should have negative priority
        target.setPosition(0f, 12f);
        entity.update(); // Ensure update methods are called
        assertTrue(pauseTask.getPriority() < 0, "Priority should be negative when out of view distance");

        // Test: When within view distance but outside max pause distance, pauseTask should have high priority
        target.setPosition(0f, 4f);
        entity.update();
        assertEquals(10, pauseTask.getPriority(), "Priority should be high when within view distance but outside pause distance");

        // Test: When within max pause distance, pauseTask should have high priority and should be active
        target.setPosition(0f, 2f);
        pauseTask.start(); // Start the PauseTask
        entity.update();
        assertEquals(10, pauseTask.getPriority(), "Priority should be high when within pause distance");

        // Test: After moving out of view distance, pauseTask should have negative priority
        target.setPosition(0f, 12f);
        entity.update();
        assertTrue(pauseTask.getPriority() < 0, "Priority should be negative after moving out of view distance");
    }
}
