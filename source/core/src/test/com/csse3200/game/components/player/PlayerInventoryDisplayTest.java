//package com.csse3200.game.components.player;
//
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.csse3200.game.components.player.PlayerInventoryDisplay;
//import com.csse3200.game.entities.Entity;
//import com.csse3200.game.entities.EntityService;
//import com.csse3200.game.extensions.GameExtension;
//import com.csse3200.game.inventory.items.AbstractItem;
//import com.csse3200.game.rendering.RenderService;
//import com.csse3200.game.services.ServiceLocator;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(GameExtension.class)
//@ExtendWith(MockitoExtension.class)
//class PlayerInventoryDisplayTest {
//    @Mock Stage stage;
//    @Mock RenderService renderService;
//    @Mock AbstractItem mockItem;
//
//    private PlayerInventoryDisplay display;
//    private Entity entity;
//
//    @BeforeEach
//    void setUp() {
//        // Register services
//        ServiceLocator.registerEntityService(new EntityService());
//        renderService.setStage(stage);
//        when(renderService.getStage()).thenReturn(stage);
//        ServiceLocator.registerRenderService(renderService);
//
//        // Create and configure PlayerInventoryDisplay
//        display = new PlayerInventoryDisplay(9, 3);
//        display.setStage(stage);
//        entity = new Entity();
//        entity.addComponent(display);
//
//        // Trigger the create method
//        display.create();
//    }
//
//    @AfterEach
//    void tearDown() {
//        ServiceLocator.clear();
//    }
//
//    @Test
//    void shouldToggleInventory() {
//        // Ensure the inventory is initially not added
//        assertFalse(display.isToggled());
//
//        // Trigger the "toggleInventory" event
//        entity.getEvents().trigger("toggleInventory");
//        assertTrue(display.isToggled());
//
//        // Trigger the "toggleInventory" event again
//        entity.getEvents().trigger("toggleInventory");
//        assertFalse(display.isToggled());
//    }
//
//    @Test
//    void shouldAddItemToInventory() {
//        // Ensure the inventory is initially empty
//        assertEquals(0, display.getInventory().getSize());
//
//        // Trigger the "addItem" event
//        entity.getEvents().trigger("addItem", mockItem);
//        assertEquals(1, display.getInventory().getSize());
//
//        // Check if the item is added to the correct slot in the UI
//        assertNotNull(display.getInventory().getAt(0));
//    }
//}