package com.csse3200.game.components.combat;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.CombatArea;
import com.csse3200.game.areas.terrain.CombatTerrainFactory;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class CombatStatsDisplayTest {
    private CombatArea combatArea;
    private Entity player;
    private Entity enemy;
    private GdxGame game;
    private CombatTerrainFactory terrainFactory;
    private ResourceService resourceService;
    private MockedStatic<ServiceLocator> serviceLocatorMock;
    CombatStatsComponent mockPlayerStats;
    CombatStatsComponent mockEnemyStats;
    CombatStatsDisplay mockDisplay;

    @BeforeEach
    void setUp() {
        GdxGame game = mock(GdxGame.class);
        // Mocking Gdx environment
        Gdx.files = mock(Files.class);
        Gdx.graphics = mock(com.badlogic.gdx.Graphics.class);
        Gdx.gl = mock(com.badlogic.gdx.graphics.GL20.class);
        Gdx.gl20 = mock(com.badlogic.gdx.graphics.GL20.class);
        SpriteBatch batch = mock(SpriteBatch.class);

        // Mock the skin to avoid asset loading issues
        FileHandle fileHandle = mock(FileHandle.class);
        when(fileHandle.nameWithoutExtension()).thenReturn("mockSkin");
        Skin mockSkin = mock(Skin.class);

        // Mock the atlasFile
        FileHandle mockedAtlasFile = mock(FileHandle.class);
        Mockito.when(Gdx.files.internal("flat-earth/skin/flat-earth-ui.atlas")).thenReturn(mockedAtlasFile);
        Mockito.when(mockedAtlasFile.exists()).thenReturn(true);

        ResourceService resourceService = new ResourceService();
        RenderService renderService = mock(RenderService.class);
        when(renderService.getStage()).thenReturn(mock(Stage.class));
        InputService inputService = new InputService();

        // Register services with ServiceLocator
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerInputService(inputService);

        // Load all resources
        resourceService.loadAll();

        Gdx.input.setInputProcessor(renderService.getStage());

        mockPlayerStats = new CombatStatsComponent (100,100,100,100,100,
                100,100, true);
        mockEnemyStats = new CombatStatsComponent (10,0,1,1,1,
                0,100, true);
        mockDisplay = new CombatStatsDisplay(mockPlayerStats, mockEnemyStats);
    }

    @Test
        void testInitialisationOfStats() throws NoSuchFieldException, IllegalAccessException {
        // Mock player and enemy entities
        Entity mockPlayer = mock(Entity.class);
        Entity mockEnemy = mock(Entity.class);

        // Set up CombatStatsComponent for player and enemy
        CombatStatsComponent playerStats = new CombatStatsComponent(100, 100, 100, 100, 100, 100, 100, true);
        CombatStatsComponent enemyStats = new CombatStatsComponent(50, 50, 50, 50, 50, 50, 50, true);

        // Attach combat stats to player and enemy
        when(mockPlayer.getComponent(CombatStatsComponent.class)).thenReturn(playerStats);
        when(mockEnemy.getComponent(CombatStatsComponent.class)).thenReturn(enemyStats);

        // Create the CombatStatsDisplay as done in the CombatScreen
        CombatStatsDisplay combatStatsDisplay = new CombatStatsDisplay(playerStats, enemyStats);

        // Assertions: Check if the stats display was initialized correctly
        assertNotNull(combatStatsDisplay);
        assertEquals(playerStats, combatStatsDisplay.getPlayerStats());
        assertEquals(enemyStats, combatStatsDisplay.getEnemyStats());
    }

    @Test
    void create() {
    }

    @Test
    void initBarAnimations() {
    }

    @Test
    void setNewFrame() {
    }

    @Test
    void updateHealthUI() {
    }

    @Test
    void updatePlayerExperienceUI() {
    }

    @Test
    void updatePlayerHungerUI() {
    }

    @Test
    void draw() {
    }

    @Test
    void dispose() {
    }
}
