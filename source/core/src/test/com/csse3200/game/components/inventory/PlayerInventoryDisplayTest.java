package com.csse3200.game.components.inventory;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.inventory.PlayerInventoryDisplay;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.food.Foods;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import com.csse3200.game.events.EventHandler;



public class PlayerInventoryDisplayTest {
    private GdxGame mockGame;
    private Entity player;
    public Entity entity;
    private ItemUsageContext context;

    @Test
    public void setUp() {
//        mockGame = mock(GdxGame.class);
//        MockitoAnnotations.openMocks(this);
        CombatStatsComponent stat = new CombatStatsComponent(50, 100, 50, 0, 0, 0,0, true, false, 1);
        Foods.Apple apple = new Foods.Apple(1);
        // Create a sample player to test:
        player = new Entity().addComponent(stat);
        context = new ItemUsageContext(player);
        Inventory inventory = new Inventory(9);
        inventory.add(apple);
        KeyboardPlayerInputComponent key = mock(KeyboardPlayerInputComponent.class);
        // 33 is E key code
        when(key.keyUp(33)).thenReturn(true);


//        entity.getEvents().trigger("toggleInventory");

        PlayerInventoryDisplay playerInventoryDisplay = new PlayerInventoryDisplay(inventory, 7, 1);
//        Assertions.assertTrue(playerInventoryDisplay.getToggle());
//        when(playerInventoryDisplay.getToggle()).thenReturn(true);
//        verify(playerInventoryDisplay).getToggle();
//        assertEquals(true, playerInventoryDisplay.getToggle());


//        PlayerInventoryDisplay playerInventoryDisplay = new PlayerInventoryDisplay(inventory, 5, 3); // Example constructor
//        playerInventoryDisplay.create();

//        playerInventoryDisplay.setStage(stage);
    }

    // Initialise stats component with hunger half full:


//    @Test
//    void testToggleDisplayOn() {
    // Mock that inventory display is not initially in stage
//        when(stage.getActors().contains(any(), eq(true))).thenReturn(false);

//     Call toggle display
//        playerInventoryDisplay.toggleDisplay();

    // Verify that inventory is generated
//        verify(stage).addActor(any(Window.class)); // Should add inventoryDisplay
//    }

//    @Test
//    void testToggleDisplayOff() {
//        // Mock that inventory display is in stage
//        when(stage.getActors().contains(any(), eq(true))).thenReturn(true);
//
//        // Call toggle display
//        playerInventoryDisplay.toggleDisplay();
//
//        // Verify that inventory is removed
//        verify(stage).getActors();
//        verify(stage.getActors()).removeValue(any(), eq(true)); // Should remove inventoryDisplay
//    }
//
//    @Test
//    void testAddItem() {
//        // Mock item addition
//        AbstractItem mockItem = mock(AbstractItem.class);
//        when(inventory.add(mockItem)).thenReturn(true);
//
//        // Call addItem
//        playerInventoryDisplay.addItem(mockItem);
//
//        // Verify inventory updated
//        verify(inventory).add(mockItem);
//        // Verify inventory display refreshed
//        verify(stage, atLeastOnce()).getActors();
//    }
}
