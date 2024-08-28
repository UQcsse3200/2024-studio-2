package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ForestGameAreaTest {

    private ForestGameArea forestGameArea;

    @BeforeEach
    void setUp() {
        TerrainFactory mockTerrainFactory = mock(TerrainFactory.class);
        CameraComponent mockCameraComponent = mock(CameraComponent.class);

        forestGameArea = new ForestGameArea(mockTerrainFactory, mockCameraComponent);
    }

    @Test
    void testForestGameAreaInitialization() {
        assertNotNull(forestGameArea, "I love the forest");
    }
}
