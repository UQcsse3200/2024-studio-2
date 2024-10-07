package com.csse3200.game.areas.terrain;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import com.badlogic.gdx.maps.MapLayers;

public class TerrainChunkTest {

    private TerrainChunk terrainChunk;
    private TiledMap tiledMap;
    private TerrainComponent.TerrainResource terrainResource;

    @Before
    public void setUp() {
        // Create simulated TiledMap and TerrainResource objects
        tiledMap = Mockito.mock(TiledMap.class);
        terrainResource = Mockito.mock(TerrainComponent.TerrainResource.class);

        // Create a mock MapLayers object and set it to the return value of TiledMap
        MapLayers mapLayers = Mockito.mock(MapLayers.class);
        TiledMapTileLayer tileLayer = Mockito.mock(TiledMapTileLayer.class);
        Mockito.when(mapLayers.get(0)).thenReturn(tileLayer);
        Mockito.when(tiledMap.getLayers()).thenReturn(mapLayers);

        // initialise TerrainChunk
        terrainChunk = new TerrainChunk(new GridPoint2(0, 0), tiledMap);
    }

    @Test
    public void testInitialization() {
        // Check that the grid is initialized
        assertNotNull(terrainChunk.grid);
        assertEquals(0, terrainChunk.grid.size);

        // Check that the tile is initialized
        assertNotNull(terrainChunk.getTileTypeCount());
    }

    //@Test
    //public void testGenerateTiles() {
    //
    //    Map<GridPoint2, TerrainChunk> loadedChunks = new HashMap<>();
    //
    //
    //    TerrainComponent.Tile tile = Mockito.mock(TerrainComponent.Tile.class);
    //    Mockito.when(terrainResource.getTilebyIndex(Mockito.anyInt())).thenReturn(tile);
    //
    //    // Execute the generateTiles method
    //    terrainChunk.generateTiles(new GridPoint2(0, 0), loadedChunks, terrainResource);
    //
    //    // verify that tiledMap's setCell method is called
    //    TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
    //    Mockito.verify(tileLayer, Mockito.atLeastOnce()).setCell(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(TerrainChunk.CCell.class));
    //}
}
