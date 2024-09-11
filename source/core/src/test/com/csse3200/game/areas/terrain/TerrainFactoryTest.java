//package com.csse3200.game.areas.terrain;
//
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.maps.tiled.TiledMap;
//import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
//import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
//import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
//import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
//import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
//import com.badlogic.gdx.math.GridPoint2;
//import com.csse3200.game.components.CameraComponent;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//public class TerrainFactoryTest {
//
//    private TerrainFactory terrainFactory;
//    private CameraComponent cameraComponent;
//    private OrthographicCamera camera;
//
//    @Before
//    public void setUp() {
//        // Mock the camera component and orthographic camera
//        cameraComponent = Mockito.mock(CameraComponent.class);
//        camera = Mockito.mock(OrthographicCamera.class);
//
//        // Set up the camera mock to return the orthographic camera
//        Mockito.when(cameraComponent.getCamera()).thenReturn(camera);
//
//        // Initialize the terrain factory with the mock camera component and orthogonal orientation
//        terrainFactory = new TerrainFactory(cameraComponent);
//    }
//
//
//
//    @Test
//    public void testCreateTerrainOrthogonal() {
//        // Define a map size and player position
//        GridPoint2 mapSize = new GridPoint2(16, 16);
//        GridPoint2 playerPosition = new GridPoint2(0, 0);
//
//        // Call createTerrain for ORTHOGONAL terrain type
//        TerrainComponent terrainComponent = terrainFactory.createTerrain(
//                TerrainFactory.TerrainType.FOREST_DEMO,
//                playerPosition,
//                mapSize
//        );
//
//        // Verify that the renderer is of the correct type
//        assertTrue(terrainComponent.getRenderer() instanceof OrthogonalTiledMapRenderer);
//    }
//
//    @Test
//    public void testCreateTerrainIsometric() {
//        // Define a map size and player position
//        GridPoint2 mapSize = new GridPoint2(16, 16);
//        GridPoint2 playerPosition = new GridPoint2(0, 0);
//
//        // Mocking the TiledMap and its layer to verify interaction
//        TiledMap mockTiledMap = mock(TiledMap.class);
//        TiledMapTileLayer mockLayer = mock(TiledMapTileLayer.class);
//
//        // Call createTerrain for ORTHOGONAL terrain type
//        TerrainComponent terrainComponent = terrainFactory.createTerrain
//                (
//                TerrainFactory.TerrainType.FOREST_DEMO,
//                playerPosition,
//                mapSize
//        );
//
//        // Verify that the TiledMap was created and layers were added
//        verify(mockTiledMap).getLayers().add(any(TiledMapTileLayer.class));
//
//        // Verify the correct layer sizes
//        verify(mockLayer).getWidth();
//        verify(mockLayer).getHeight();
//    }
//
//    @Test
//    public void testCreateTerrainHexagonal() {
//        // Initialize the factory for HEXAGONAL orientation
//        terrainFactory = new TerrainFactory(cameraComponent, TerrainComponent.TerrainOrientation.HEXAGONAL);
//
//        // Define a map size and player position
//        GridPoint2 mapSize = new GridPoint2(16, 16);
//        GridPoint2 playerPosition = new GridPoint2(0, 0);
//
//        // Call createTerrain for HEXAGONAL terrain type
//        TerrainComponent terrainComponent = terrainFactory.createTerrain
//                (
//                TerrainFactory.TerrainType.FOREST_DEMO_HEX,
//                playerPosition,
//                mapSize
//        );
//
//        // Assert that the TerrainComponent is not null
//        assertNotNull(terrainComponent);
//
//        // Verify the TiledMap and its layer are properly set
//        TiledMap tiledMap = terrainComponent.getTiledMap();
//        assertNotNull(tiledMap);
//        assertEquals(1, tiledMap.getLayers().getCount());
//
//        // Verify the layer size
//        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
//        assertEquals(mapSize.x, tileLayer.getWidth());
//        assertEquals(mapSize.y, tileLayer.getHeight());
//
//        // Check that the renderer is a HexagonalTiledMapRenderer
//        TiledMapRenderer renderer = terrainComponent.getRenderer();
//        assertTrue(renderer instanceof HexagonalTiledMapRenderer);
//    }
//}
