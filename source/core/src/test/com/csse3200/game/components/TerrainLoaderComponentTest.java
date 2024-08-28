package com.csse3200.game.components;

import static org.mockito.Mockito.*;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainLoaderComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;

public class TerrainLoaderComponentTest {

    // Mock dependencies
    private TerrainComponent mockTerrainComponent;
    private EntityService mockEntityService;
    private Entity player;
    private TerrainLoaderComponent terrainLoader;

    @Before
    public void setUp() {
        // Mock necessary classes
        mockTerrainComponent = mock(TerrainComponent.class);
        mockEntityService = mock(EntityService.class);
        player = mock(Entity.class);

        // Initialise the TerrainLoaderComponent with the mocked terrain
        terrainLoader = new TerrainLoaderComponent(mockTerrainComponent);
        // Set the player entity for the loader
        terrainLoader.setEntity(player);

        // Register the mock EntityService
        ServiceLocator.registerEntityService(mockEntityService);
    }

    @Test
    public void testInitialTerrainLoading() {
        // Assume the player starts at position (0, 0)
        when(player.getPosition()).thenReturn(new Vector2(0, 0));

        // Trigger the initial terrain loading
        terrainLoader.create();

        // Verify that the terrain is loaded for the initial chunk (0, 0)
        verify(mockTerrainComponent, times(1)).loadChunks(eq(new GridPoint2(0, 0)));
    }

    @Test
    public void testChunkLoadingOnPlayerMovement() {
        // Simulate initial position (0, 0)
        when(player.getPosition()).thenReturn(new Vector2(0, 0));
        terrainLoader.create(); // Load the initial chunk

        // Simulate moving the player to a new chunk position
        when(player.getPosition()).thenReturn(new Vector2(4 * TerrainFactory.CHUNK_SIZE, 4 * TerrainFactory.CHUNK_SIZE));
        terrainLoader.update(); // Update terrain loader to reflect movement

        // Verify that the new chunk (4, 4) is loaded
        verify(mockTerrainComponent, times(1)).loadChunks(eq(new GridPoint2(4, 4)));
    }

    @Test
    public void testMultipleChunksLoadedOnPlayerMovement() {
        // Simulate starting at position (0, 0)
        when(player.getPosition()).thenReturn(new Vector2(0, 0));
        terrainLoader.create(); // Load the initial chunk

        // Simulate moving to a further position
        when(player.getPosition()).thenReturn(new Vector2(8 * TerrainFactory.CHUNK_SIZE, 8 * TerrainFactory.CHUNK_SIZE));
        terrainLoader.update(); // Update to reflect the new chunk

        // Verify that the new chunk (8, 8) is loaded
        verify(mockTerrainComponent, times(1)).loadChunks(eq(new GridPoint2(8, 8)));
    }
}
