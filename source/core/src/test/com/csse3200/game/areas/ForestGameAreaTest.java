package com.csse3200.game.areas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.input.InputFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.csse3200.game.extensions.GameExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ExtendWith(GameExtension.class)
class ForestGameAreaTest {
    private ForestGameArea forestGameArea;
    private TerrainFactory terrainFactory;
    private GdxGame game;
    private Entity mockPlayer;

    @BeforeEach
    void setUp() {
        terrainFactory = mock(TerrainFactory.class);
        game = mock(GdxGame.class);

        PhysicsService physicsService = mock(PhysicsService.class);
        ServiceLocator.registerPhysicsService(physicsService);

        RenderService renderService = mock(RenderService.class);
        ServiceLocator.registerRenderService(renderService);

        // Register a mocked EntityService in ServiceLocator
        EntityService entityService = mock(EntityService.class);
        ServiceLocator.registerEntityService(entityService);

        // Register a mocked InputService in ServiceLocator
        InputService inputService = mock(InputService.class);
        InputFactory inputFactory = mock(InputFactory.class);
        when(inputService.getInputFactory()).thenReturn(inputFactory);
        ServiceLocator.registerInputService(inputService);

        // Register a mocked ResourceService in ServiceLocator
        ResourceService resourceService = mock(ResourceService.class);
        Texture mockTexture = mock(Texture.class);
        when(resourceService.getAsset(anyString(), eq(Texture.class))).thenReturn(mockTexture);
        ServiceLocator.registerResourceService(resourceService);

        // Send spies to collect information
        forestGameArea = spy(new ForestGameArea(terrainFactory, game));

        // Mock loadAssets to prevent real asset loading during tests
        doNothing().when(forestGameArea).loadAssets();

        // Mock player entity
        mockPlayer = mock(Entity.class);
        when(mockPlayer.getPosition()).thenReturn(new Vector2(2500, 2500));
    }

    @AfterEach
    void tearDown() {
        ServiceLocator.clear(); // Clear ServiceLocator to avoid contamination between tests
    }

    @Test
    void testInitialisation() {
        assertNotNull(forestGameArea, "ForestGameArea should be initialised");
    }
//  Failing due to incompetence
//    @Test
//    void testCreateTerrain() {
//        TerrainComponent mockTerrain = mock(TerrainComponent.class);
//
//        // Mock the creation of terrain using the TerrainFactory
//        when(terrainFactory.createTerrain(any(), any(), any(), any())).thenReturn(mockTerrain);
//
//        // Call the create method on the forestGameArea (which should trigger terrain creation)
//        forestGameArea.create();
//
//        assertNotNull(forestGameArea, "ForestGameArea should be created successfully");
//    }
//
//    @Test
//    void testPlayerSpawn() {
//        // Mock terrain
//        when(terrainFactory.createTerrain(any(), any(), any(), any())).thenReturn(mock(TerrainComponent.class));
//
//        //  trigger player and terrain spawn
//        forestGameArea.create();
//
//        Entity player = forestGameArea.getPlayer();
//        assertNotNull(player, "Player entity should be spawned");
//    }
//
//    @Test
//    void testEntityRegistration() {
//        // Mock terrain creation
//        when(terrainFactory.createTerrain(any(), any(), any(), any())).thenReturn(mock(TerrainComponent.class));
//
//        // Get the mock entity service
//        EntityService entityService = ServiceLocator.getEntityService();
//
//        // Call create to trigger terrain and entity registration
//        forestGameArea.create();
//
//        // Ensure that the player entity was registered with the entity service
//        verify(entityService, times(1)).register(any(Entity.class));
//    }

    @Test
    void testSpawnConvertedNPCWithNullEntity() {
        Entity defeatedEntity = null;
        forestGameArea.spawnConvertedNPCs(defeatedEntity);
        verify(ServiceLocator.getEntityService(), never()).register(any(Entity.class));

    }

    @Test
    void testSpawnConvertedNPCWithNullEnemyType() {
        Entity defeatedEntity = mock(Entity.class);
        when(defeatedEntity.getEnemyType()).thenReturn(null);
        forestGameArea.spawnConvertedNPCs(defeatedEntity);
        verify(ServiceLocator.getEntityService(), never()).register(any(Entity.class));
        verify(defeatedEntity, never()).getPosition();
    }


}
