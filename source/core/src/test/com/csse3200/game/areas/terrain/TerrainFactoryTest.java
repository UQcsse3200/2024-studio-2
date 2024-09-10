//package com.csse3200.game.areas.terrain;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.maps.tiled.TiledMap;
//import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
//import com.badlogic.gdx.math.GridPoint2;
//import com.csse3200.game.components.CameraComponent;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class TerrainFactoryTest {
//
//    private CameraComponent cameraComponent;
//    private OrthographicCamera camera;
//    private TerrainFactory terrainFactory;
//
//    @BeforeEach
//    public void setUp() {
//        cameraComponent = mock(CameraComponent.class);
//        camera = mock(OrthographicCamera.class);
//        when(cameraComponent.getCamera()).thenReturn(camera);
//
//        terrainFactory = new TerrainFactory(cameraComponent);
//    }
//
//    @Test
//    public void testCreateTerrain() {
//        GridPoint2 playerPosition = new GridPoint2(0, 0);
//        GridPoint2 mapSize = new GridPoint2(100, 100);
//
//        TerrainComponent terrainComponent = terrainFactory.createTerrain(
//                TerrainFactory.TerrainType.FOREST_DEMO, playerPosition, mapSize);
//
//        assertNotNull(terrainComponent);
//        assertEquals(camera, terrainComponent.getCamera());
//        assertNotNull(terrainComponent.getTiledMap());
//        assertNotNull(terrainComponent.getRenderer());
//        assertEquals(TerrainComponent.TerrainOrientation.ORTHOGONAL, terrainComponent.getOrientation());
//    }
//}
