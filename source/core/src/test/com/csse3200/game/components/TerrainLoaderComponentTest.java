package com.csse3200.game.components;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;

public class TerrainLoaderComponentTest {

    // Mock objects to simulate dependencies
    private TerrainFactory mockTerrainFactory;
    private EntityService mockEntityService;
    private Entity player;
    private TerrainLoaderComponent terrainLoader;
    private TerrainComponent mockTerrainComponent;

    @Before
    public void setUp() {
        // Create mock instances of the necessary classes
        mockTerrainFactory = mock(TerrainFactory.class);
        mockEntityService = mock(EntityService.class);
        player = mock(Entity.class);

        // Create an instance of the TerrainLoaderComponent with the mocked factory
        terrainLoader = new TerrainLoaderComponent(mockTerrainFactory);
        terrainLoader.setEntity(player); // Associate the terrain loader with the player entity

        // Mock a valid TerrainComponent to return when terrain is created
        mockTerrainComponent = mock(TerrainComponent.class);

        // Register the mock EntityService with the ServiceLocator
        ServiceLocator.registerEntityService(mockEntityService);

        // Set up the behaviour for the mockTerrainFactory to return the mock terrain component
        when(mockTerrainFactory.createTerrain(any(), any())).thenReturn(mockTerrainComponent);
    }

    @Test
    public void testInitialTerrainLoading() {
        // Assume the player starts at world position (0, 0)
        when(player.getPosition()).thenReturn(new Vector2(0, 0));

        // Call the create method, which should load the initial terrain
        terrainLoader.create();

        // Verify that the terrain is created at the player's starting position (0, 0)
        verify(mockTerrainFactory, times(1)).createTerrain(any(), eq(new GridPoint2(0, 0)));
    }

    @Test
    public void testChunkLoadingOnPlayerMovement() {
        // Assume the player starts at position (0, 0)
        when(player.getPosition()).thenReturn(new Vector2(0, 0));
        terrainLoader.create(); // Load the initial chunk

        // Simulate moving the player to position (64, 64)
        when(player.getPosition()).thenReturn(new Vector2(64, 64));
        terrainLoader.update(); // Update the terrain loader, which should load the new chunk

        // Verify that a new terrain chunk is loaded at position (64, 64)
        verify(mockTerrainFactory, times(1)).createTerrain(any(), eq(new GridPoint2(64, 64)));
    }

    /*
    // This test is currently commented out due to an issue with the expected behaviour
    @Test
    public void testChunkLoadingWhenMovingBackAndForth() {
        // Simulate starting at position (0, 0)
        when(player.getPosition()).thenReturn(new Vector2(0, 0));
        terrainLoader.create();

        // Move to a new position (64, 64)
        when(player.getPosition()).thenReturn(new Vector2(64, 64));
        terrainLoader.update();

        // Move back to the original position (0, 0)
        when(player.getPosition()).thenReturn(new Vector2(0, 0));
        terrainLoader.update();

        // Verify that the initial chunk (0, 0) is only loaded once
        verify(mockTerrainFactory, times(1)).createTerrain(any(), eq(new GridPoint2(0, 0)));
    }

    // This test is currently commented out due to an issue with the expected behaviour
    @Test
    public void testNoDuplicateChunkLoading() {
        // Assume the player starts at position (0, 0)
        when(player.getPosition()).thenReturn(new Vector2(0, 0));
        terrainLoader.create();

        // Move to a new position (64, 64)
        when(player.getPosition()).thenReturn(new Vector2(64, 64));
        terrainLoader.update();

        // Move back to the original position (0, 0)
        when(player.getPosition()).thenReturn(new Vector2(0, 0));
        terrainLoader.update();

        // Verify that the chunk (0, 0) is not loaded again after the initial load
        verify(mockTerrainFactory, times(1)).createTerrain(any(), eq(new GridPoint2(0, 0)));
    }
    */

    @Test
    public void testMultipleChunksLoadedOnPlayerMovement() {
        // Simulate starting at position (0, 0)
        when(player.getPosition()).thenReturn(new Vector2(0, 0));
        terrainLoader.create();

        // Simulate moving the player to position (128, 128)
        when(player.getPosition()).thenReturn(new Vector2(128, 128));
        terrainLoader.update();

        // Verify that a new chunk is loaded at position (128, 128)
        verify(mockTerrainFactory, times(1)).createTerrain(any(), eq(new GridPoint2(128, 128)));
    }
}
