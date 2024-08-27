//package com.csse3200.game.components.player;
//
//import com.csse3200.game.entities.Entity;
//import com.csse3200.game.events.EventHandler;
//import com.csse3200.game.extensions.GameExtension;
//import com.csse3200.game.input.InputComponent;
//import com.csse3200.game.input.InputService;
//import com.csse3200.game.inventory.items.AbstractItem;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.api.BeforeEach;
//import static org.mockito.Mockito.*;
//import com.csse3200.game.rendering.RenderService;
//import com.csse3200.game.services.ResourceService;
//import com.csse3200.game.services.ServiceLocator;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.Gdx;
//import org.mockito.Mock;
//
//@ExtendWith(GameExtension.class)
//class PlayerInventoryDisplayTest {
//    PlayerInventoryDisplay display;
//    Entity entity;
//    @Mock AbstractItem mockItem;
//    EventHandler handler;
//
//    @BeforeEach
//    void beforeEach() {
//        ResourceService resourceService = new ResourceService();
//        RenderService renderService = mock(RenderService.class);
//        when(renderService.getStage()).thenReturn(mock(Stage.class));
//        InputService inputService = new InputService();
//
//        // Register services with ServiceLocator
//        ServiceLocator.registerResourceService(resourceService);
//        ServiceLocator.registerRenderService(renderService);
//        ServiceLocator.registerInputService(inputService);
//        Stage stage = ServiceLocator.getRenderService().getStage();
//
//        // Mock the behavior of RenderService to return the Stage instance
//        when(renderService.getStage()).thenReturn(stage);
//
//        // Load all resources
//        resourceService.loadAll();
//
//        InputComponent inputComponent =
//                ServiceLocator.getInputService().getInputFactory().createForPlayer();
//        display = new PlayerInventoryDisplay(9, 3);
//        entity = new Entity();
//        entity.addComponent(display);
//        entity.addComponent(inputComponent);
//        Gdx.input.setInputProcessor(stage);
//
//
//    }
//
//    @Test
//    void shouldToggleInventory() {
//        // Ensure the inventory is initially not added
//        Assertions.assertFalse(display.isToggled());
//
//        // Trigger the "toggleInventory" event
//        entity.getEvents().trigger("toggleInventory");
//        Assertions.assertTrue(display.isToggled());
//
//        // Trigger the "toggleInventory" event again
//        entity.getEvents().trigger("toggleInventory");
//        Assertions.assertFalse(display.isToggled());
//    }
//
//    @Test
//    void shouldAddItemToInventory() {
//        // Ensure the inventory is initially empty
//        Assertions.assertEquals(0, display.getInventory().getSize());
//
//        // Trigger the "addItem" event
//        entity.getEvents().trigger("addItem", mockItem);
//        Assertions.assertEquals(1, display.getInventory().getSize());
//
//        // Check if the item is added to the correct slot in the UI
//        Assertions.assertNotNull(display.getInventory().getAt(0));
//    }
//}