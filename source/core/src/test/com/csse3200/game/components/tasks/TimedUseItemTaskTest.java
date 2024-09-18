package com.csse3200.game.components.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerItemInUseDisplay;
import com.csse3200.game.entities.DialogueBoxService;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.TimedUseItem;
import com.csse3200.game.inventory.items.potions.DefensePotion;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TimedUseItemTaskTest {
    private Entity mockTarget;
    private DefensePotion mockPotion;
    private ItemUsageContext mockContext;
    private PlayerItemInUseDisplay mockItemDisplay;
    private GameTime mockGameTime;
    private TimedUseItemTask task;

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

        // Mock the behavior of RenderService to return the Stage instance
        when(renderService.getStage()).thenReturn(stage);

        // Load all resources
        resourceService.loadAll();

        Gdx.input.setInputProcessor(stage);
    }

    @Test
    public void testActivePotion()

//    @Test
//    public void testActivatePotionSetsDefenseExpired() {
//        // Arrange
//        when(mockPotion.isExpired(mockContext)).thenReturn(true);
//        when(mockGameTime.getTime()).thenReturn(1000L);
//
//        // Act
//        task.start();
//        task.update();  // Simulate the update where the potion is used
//
//        // Assert
//        verify(mockItemDisplay).setDefenseExpired(true);
//        verify(mockPotion).update(mockContext);
//    }
//
//    @Test
//    public void testPotionExpirationResetsStartTime() {
//        // Arrange
//        when(mockPotion.isExpired(mockContext)).thenReturn(true);
//        when(mockGameTime.getTime()).thenReturn(0L, 120001L);  // Simulate time passing
//
//        task.start();  // Starts the task and sets the listener
//        task.update();  // Simulates using the potion and starting the timer
//
//        // Act - Simulate potion expiration
//        task.update();
//
//        // Assert
//        verify(mockPotion).update(mockContext);
//        assertEquals(-1, task.getStartTime());  // Ensure startTime is reset
//        verify(mockItemDisplay).setDefenseExpired(false);
//        //verify(mockItemDisplay).createIndicationBox();
//    }
}
