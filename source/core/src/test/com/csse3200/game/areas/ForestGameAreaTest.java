package com.csse3200.game.areas;

import static org.mockito.Mockito.*;

import com.badlogic.gdx.audio.Music;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ForestGameAreaTest {

    private ForestGameArea forestGameArea;
    private ResourceService mockResourceService;

    @BeforeEach
    void setUp() {
        // Mock dependencies for TerrainFactory and GdxGame
        TerrainFactory mockTerrainFactory = mock(TerrainFactory.class);
        GdxGame mockGdxGame = mock(GdxGame.class);

        // Initialise ForestGameArea with mocked dependencies
        forestGameArea = new ForestGameArea(mockTerrainFactory, mockGdxGame);

        // Mock the ResourceService and register it with ServiceLocator
        mockResourceService = mock(ResourceService.class);
        ServiceLocator.registerResourceService(mockResourceService);
    }

    @Test
    void testForestGameAreaInitialisation() {
        // Verify that the ForestGameArea is initialised successfully
        assertNotNull(forestGameArea, "ForestGameArea should be initialised.");
    }

    @Test
    void testLoadAssets() {
        // Mock the behaviour of loadForMillis to return immediately
        when(mockResourceService.loadForMillis(anyInt())).thenReturn(true);

        // Mock loading textures, sounds, and music
        doNothing().when(mockResourceService).loadTextures(any());
        doNothing().when(mockResourceService).loadSounds(any());
        doNothing().when(mockResourceService).loadMusic(any());

        // Invoke loadAssets method
        forestGameArea.loadAssets();

        // Verify that resources are loaded the expected number of times
        verify(mockResourceService, times(1)).loadTextures(any());
        verify(mockResourceService, times(7)).loadSounds(any());
        verify(mockResourceService, times(1)).loadMusic(any());
    }

    @Test
    void testPlayMusic() {
        // Mock the music asset
        Music mockMusic = mock(Music.class);
        when(mockResourceService.getAsset(anyString(), eq(Music.class))).thenReturn(mockMusic);

        // Invoke playMusic method
        forestGameArea.playMusic();

        // Verify that the music plays and loops at the correct volume
        verify(mockMusic, times(1)).play();
        verify(mockMusic, times(1)).setLooping(true);
        verify(mockMusic, times(1)).setVolume(0.5f);
    }

    @Test
    void testPauseMusic() {
        // Mock the music asset
        Music mockMusic = mock(Music.class);
        when(mockResourceService.getAsset(anyString(), eq(Music.class))).thenReturn(mockMusic);

        // Invoke pauseMusic method
        forestGameArea.pauseMusic();

        // Verify that the music is paused
        verify(mockMusic, times(1)).pause();
    }

    // Test currently commented out due to freezing issue
    // This test checks the creation of the terrain in ForestGameArea
    // The test mocks the TerrainFactory and TerrainComponent to verify terrain creation logic
//    @Test
//    void testTerrainCreationWithLogging() {
//        // Mock the TerrainFactory and TerrainComponent
//        TerrainFactory mockTerrainFactory = mock(TerrainFactory.class);
//        TerrainComponent mockTerrainComponent = mock(TerrainComponent.class);
//
//        // When createTerrain is called, return the mock TerrainComponent
//        when(mockTerrainFactory.createTerrain(any(), any(), any())).thenReturn(mockTerrainComponent);
//
//        // Initialise ForestGameArea with mocked dependencies
//        forestGameArea = new ForestGameArea(mockTerrainFactory, mock(GdxGame.class));
//
//        // Add print statements to isolate any freezing issues
//        System.out.println("Before forestGameArea.create()");
//
//        // Invoke the create method to initialise terrain
//        forestGameArea.create();
//
//        System.out.println("After forestGameArea.create()");
//
//        // Verify that createTerrain was invoked once
//        verify(mockTerrainFactory, times(1)).createTerrain(any(), any(), any());
//
//        System.out.println("Verified terrain creation");
//    }

    @Test
    void testUnloadAssets() {
        // Invoke unloadAssets method to release resources
        forestGameArea.unloadAssets();

        // Verify that unloadAssets is called for each resource type
        verify(mockResourceService, atLeastOnce()).unloadAssets(any(String[].class));
    }
}
