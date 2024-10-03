//package com.csse3200.game.areas;
//
//import com.badlogic.gdx.math.Vector2;
//import com.csse3200.game.entities.Entity;
//import com.csse3200.game.entities.EntityService;
//import com.csse3200.game.extensions.GameExtension;
//import com.csse3200.game.rendering.RenderService;
//import com.csse3200.game.services.ServiceLocator;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(GameExtension.class)
//class MiniMapDisplayTest {
//
//    @Test
//    void shouldUpdateMinimapWithPlayerAndEnemies() {
//        // Mock the GameArea and Entity classes
//        GameArea mockGameArea = mock(GameArea.class);
//        Entity mockPlayer = mock(Entity.class);
//        Entity mockEnemy1 = mock(Entity.class);
//        Entity mockEnemy2 = mock(Entity.class);
//
//        // Set up the mocked player and enemy positions
//        when(mockPlayer.getPosition()).thenReturn(new Vector2(2500, 2500));
//        when(mockEnemy1.getPosition()).thenReturn(new Vector2(2600, 2600));
//        when(mockEnemy2.getPosition()).thenReturn(new Vector2(2400, 2400));
//
//        // Mock the enemies list returned from GameArea
//        when(mockGameArea.getPlayer()).thenReturn(mockPlayer);
//        when(mockGameArea.getEnemies()).thenReturn(List.of(mockEnemy1, mockEnemy2));
//
//        // Register EntityService and RenderService in ServiceLocator
//        ServiceLocator.registerEntityService(new EntityService());
//        RenderService mockRenderService = mock(RenderService.class);
//        ServiceLocator.registerRenderService(mockRenderService); // Register a mock RenderService
//
//        // Create an instance of MiniMapDisplay
//        MiniMapDisplay miniMapDisplay = new MiniMapDisplay(mockGameArea);
//        miniMapDisplay.create();  // Call the create method to initialize the display
//
//        assertNotNull(miniMapDisplay.getStage(), "Stage should be initialized");
//
//        miniMapDisplay.update();
//
//        // Verify that the blue dot (player) is updated correctly
//        verify(miniMapDisplay.blueDotPointImage).setPosition(
//                eq(2500f), eq(2500f)  // Check the positions as they are set
//        );
//
//        // Verify that the enemy dots are updated correctly
//        verify(miniMapDisplay.redDotPointImages.get(0)).setPosition(
//                eq(2600f), eq(2600f)  // Check the  positions as they are set
//        );
//        verify(miniMapDisplay.redDotPointImages.get(1)).setPosition(
//                eq(2400f), eq(2400f)  // Check the positions as they are set
//        );
//
//
//        miniMapDisplay.dispose();
//    }
//}
//
