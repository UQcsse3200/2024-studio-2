package com.csse3200.game.areas.terrain.tiles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ForestTileConfigTest {

    @Test
    void testForestMapTilesInitialization() {
        // 确保forestMapTiles数组不为null且长度大于0
        assertNotNull(ForestTileConfig.forestMapTiles, "forestMapTiles should not be null");
        assertTrue(ForestTileConfig.forestMapTiles.length > 0, "forestMapTiles should contain at least one tile configuration");

        // 检查每个瓦片的基本属性
        for (ForestTileConfig.TileConfig tile : ForestTileConfig.forestMapTiles) {
            assertNotNull(tile.id, "Tile ID should not be null");
            assertNotNull(tile.fp, "File path should not be null");
            assertNotNull(tile.edges, "Edges should not be null");
            assertNotNull(tile.centre, "Centre should not be null");
        }
    }
}
