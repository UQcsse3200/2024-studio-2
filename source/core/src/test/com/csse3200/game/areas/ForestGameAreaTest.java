package com.csse3200.game.areas;

import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ForestGameAreaTest {

    private ForestGameArea forestGameArea;

    @BeforeEach
    void setUp() {
        TerrainFactory mockTerrainFactory = mock(TerrainFactory.class);
        GdxGame mockGdxGame = mock(GdxGame.class);

        // Initialise ForestGameArea with mocked dependencies
        forestGameArea = new ForestGameArea(mockTerrainFactory, mockGdxGame);
    }

    @Test
    void testForestGameAreaInitialization() {
        // Check that the forestGameArea is successfully created
        assertNotNull(forestGameArea, "ForestGameArea should be initialised.");
    }
}
