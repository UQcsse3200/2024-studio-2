package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.combat.CombatArea;
import com.csse3200.game.areas.terrain.CombatTerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CombatAreaTest {

    private CombatArea combatArea;
    private Entity player;
    private Entity enemy;
    private GdxGame game;
    private CombatTerrainFactory terrainFactory;
    private ResourceService resourceService;
    private MockedStatic<ServiceLocator> serviceLocatorMock;

    @BeforeEach
    void setUp() {
        player = mock(Entity.class);
        enemy = mock(Entity.class);
        game = mock(GdxGame.class);
        terrainFactory = mock(CombatTerrainFactory.class);
        resourceService = mock(ResourceService.class);

        // Mock static ServiceLocator calls
        serviceLocatorMock = mockStatic(ServiceLocator.class);
        serviceLocatorMock.when(ServiceLocator::getResourceService).thenReturn(resourceService);

        combatArea = new CombatArea(player, enemy, game, terrainFactory);
    }

    @AfterEach
    void tearDown() {
        // Close the static mock to prevent registration issues in subsequent tests
        serviceLocatorMock.close();
    }

    /**
     * Assert the combat game area is not null and being initialised successfully
     */
    @Test
    void testForestGameAreaInitialization() {
        // Check that the combatArea is successfully created
        assertNotNull(combatArea, "CombatArea not initialised.");
    }

    /**
     * Test the combat area handles the music function correctly
     */
    @Test
    void playMusic() {
        Music mockMusic = mock(Music.class);
        when(ServiceLocator.getResourceService().getAsset(any(String.class), eq(Music.class))).thenReturn(mockMusic);

        combatArea.playMusic();

        verify(mockMusic).setLooping(true);
        verify(mockMusic).play();
    }
}