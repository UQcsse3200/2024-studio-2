package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.potions.HealingPotion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;


@ExtendWith(GameExtension.class)
class ItemProximityTaskTest {

    @BeforeEach
    void beforeEach() {
        EntityService entityService = new EntityService();
        GameTime gameTime = new GameTime();
        ResourceService resourceService = new ResourceService();
        RenderService renderService = mock(RenderService.class);
        when(renderService.getStage()).thenReturn(mock(Stage.class));

        // Register services with ServiceLocator
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerRenderService(renderService);

        Stage stage = ServiceLocator.getRenderService().getStage();
        DialogueBoxService entityChatService = new DialogueBoxService(stage);
        ServiceLocator.registerDialogueBoxService(entityChatService);

        // Mock the behavior of RenderService to return the Stage instance
        when(renderService.getStage()).thenReturn(stage);

        // Load all resources
        resourceService.loadAll();

        Gdx.input.setInputProcessor(stage);
    }

    @Test
    void testInitialisation() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);
        HealingPotion item = new HealingPotion(1);

        ItemProximityTask task = new ItemProximityTask(target, 1, 0.1f, item);
        AITaskComponent component = new AITaskComponent().addTask(task);

        Entity itemEntity = new Entity().addComponent(component);
        itemEntity.setPosition(2f, 2f);

        // Expect priority 1 when player is close
        Assertions.assertEquals(1, task.getPriority());

        // Expect priority to be 0 when far away
        target.setPosition(20f, 20f);
        Assertions.assertEquals(0, task.getPriority());

    }

    @Test
    void shouldCreateItemOverlayWhenNear() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);
        HealingPotion item = new HealingPotion(1);

        ItemProximityTask task = new ItemProximityTask(target, 1, 2.0f, item);
        AITaskComponent component = new AITaskComponent().addTask(task);

        Entity itemEntity = new Entity().addComponent(component);
        itemEntity.setPosition(2f, 2f);

        task.start();
        task.update();



        // Check that the overlay was created
        Assertions.assertTrue(ServiceLocator.getDialogueBoxService().getCurrentOverlay().getLabel().isVisible());
    }

    @Test
    void shouldNotCreateItemOverlayWhenFar() {
        Entity target = new Entity();
        target.setPosition(1f, 1f);
        HealingPotion item = new HealingPotion(1);

        ItemProximityTask task = new ItemProximityTask(target, 1, 0.01f, item);
        AITaskComponent component = new AITaskComponent().addTask(task);

        Entity itemEntity = new Entity().addComponent(component);
        itemEntity.setPosition(5f, 5f); // Put item far away from target

        task.start();
        task.update();

        // Check that the overlay was created
        Assertions.assertFalse(ServiceLocator.getDialogueBoxService().getCurrentOverlay().getLabel().isVisible());
    }
}
