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

    private TerrainFactory mockTerrainFactory;
    private EntityService mockEntityService;
    private Entity player;
    private TerrainLoaderComponent terrainLoader;
    private TerrainComponent mockTerrainComponent;

    @Before
    public void setUp() {
        mockTerrainFactory = mock(TerrainFactory.class);
        mockEntityService = mock(EntityService.class);
        player = mock(Entity.class);
        terrainLoader = new TerrainLoaderComponent(mockTerrainFactory);
        terrainLoader.setEntity(player);

        // Mock a valid TerrainComponent
        mockTerrainComponent = mock(TerrainComponent.class);

        // Set up the ServiceLocator to return the mocked EntityService
        ServiceLocator.registerEntityService(mockEntityService);

        // When createTerrain is called, return the mockTerrainComponent
        when(mockTerrainFactory.createTerrain(any(), any())).thenReturn(mockTerrainComponent);
    }

    @Test
    public void testInitialTerrainLoading() {
        when(player.getPosition()).thenReturn(new Vector2(0, 0));
        terrainLoader.create();

        // Verify initial terrain loading
        verify(mockTerrainFactory, times(1)).createTerrain(any(), eq(new GridPoint2(0, 0)));
    }


}
