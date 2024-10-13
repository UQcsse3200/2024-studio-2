package com.csse3200.game.minigames.maze.areas;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.minigames.maze.areas.terrain.MazeTerrainFactory;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static com.csse3200.game.entities.factories.RenderFactory.createCamera;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class MazeGameAreaTest {
    MazeGameArea gameArea;

    @Mock
    RayHandler rayHandler;
    @BeforeEach
    void setUp() throws IllegalAccessException {
        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);

        ServiceLocator.registerResourceService(new ResourceService());

        ServiceLocator.getResourceService().loadTextures(new String[]{"images/heart.png"});

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        Entity camera = createCamera();
        ServiceLocator.getEntityService().register(camera);
        CameraComponent camComponent = camera.getComponent(CameraComponent.class);

        ServiceLocator.getRenderService().setStage(mock(Stage.class));

        ServiceLocator.registerInputService(new InputService());

        LightingEngine engine = new LightingEngine(rayHandler, camComponent.getCamera());
        LightingService mockLightingService = mock(LightingService.class);
        when(mockLightingService.getLighting()).thenReturn(engine);
        ServiceLocator.registerLightingService(mockLightingService);

        Field field = ReflectionUtils
                .findFields(RayHandler.class, f -> f.getName().equals("lightList"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .getFirst();

        field.setAccessible(true);
        field.set(rayHandler, new Array<>());

        field = ReflectionUtils
                .findFields(RayHandler.class, f -> f.getName().equals("disabledLights"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .getFirst();

        field.setAccessible(true);
        field.set(rayHandler, new Array<>());

        TerrainComponent terrainComp = mock(TerrainComponent.class);
        when(terrainComp.tileToWorldPosition(any())).thenAnswer(
                invocation -> new Vector2(
                        ((GridPoint2) invocation.getArguments()[0]).x,
                        ((GridPoint2) invocation.getArguments()[0]).y));
        when(terrainComp.getTileSize()).thenCallRealMethod();

        MazeTerrainFactory terrainFactory = mock(MazeTerrainFactory.class);
        when(terrainFactory.getCameraComponent()).thenReturn(camComponent);
        when(terrainFactory.createTerrain()).thenReturn(terrainComp);

        gameArea = spy(new MazeGameArea(terrainFactory));
        gameArea.create();
    }

    @Test
    public void testCreate() {
        assertEquals(MazeGameArea.NUM_JELLYFISH * 2, gameArea.getEnemies(Entity.EnemyType.MAZE_JELLYFISH).size());
        assertEquals(MazeGameArea.NUM_ANGLERS, gameArea.getEnemies(Entity.EnemyType.MAZE_ANGLER).size());
        assertEquals(MazeGameArea.NUM_EELS, gameArea.getEnemies(Entity.EnemyType.MAZE_EEL).size());
        assertEquals(MazeGameArea.NUM_OCTOPI, gameArea.getEnemies(Entity.EnemyType.MAZE_OCTOPUS).size());
        assertNotEquals(null, gameArea.getPlayer());
        assertEquals(MazeGameArea.NUM_JELLYFISH * 2
                + MazeGameArea.NUM_ANGLERS
                + MazeGameArea.NUM_EELS
                + MazeGameArea.NUM_OCTOPI, gameArea.getEnemies().size());
    }

    @Test
    public void testUnload() {
        gameArea.unloadAssets();
        assertFalse(
                ServiceLocator.getResourceService().containsAsset(
                        "images/minigames/angler.atlas", TextureAtlas.class));
    }

    @Test
    public void testDispose() {
        gameArea.dispose();
        assertFalse(
                ServiceLocator.getResourceService().containsAsset(
                        "images/minigames/angler.atlas", TextureAtlas.class));
    }

    @Test
    public void testGetBosses() {
        assertThrows(UnsupportedOperationException.class, () -> gameArea.getBosses());
    }

    @Test
    public void testGetFriendlyNPCs() {
        assertThrows(UnsupportedOperationException.class, () -> gameArea.getFriendlyNPCs());
    }

    @Test
    public void testGetMinigameNPCs() {
        assertThrows(UnsupportedOperationException.class, () -> gameArea.getMinigameNPCs());
    }
}
