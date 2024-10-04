package com.csse3200.game.areas;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

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
