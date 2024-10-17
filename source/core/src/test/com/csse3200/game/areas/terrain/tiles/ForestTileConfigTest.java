package com.csse3200.game.areas.terrain.tiles;

import com.csse3200.game.areas.terrain.tiles.TileConfig;
import com.csse3200.game.areas.terrain.tiles.ForestTileConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ForestTileConfigTest {

    @Test
    void testForestMapTilesInitialization() {
        // ensure the number of forestMapTiles is not null and greater than 0
        assertNotNull(ForestTileConfig.getForestMapTiles(), "forestMapTiles should not be null");
        assertTrue(ForestTileConfig.getForestMapTiles().length > 0, "forestMapTiles should contain at least one tile configuration");

        // check the property of each tile
        for (TileConfig tile : ForestTileConfig.getForestMapTiles()) {
            assertNotNull(tile.id, "Tile ID should not be null");
            assertNotNull(tile.fp, "File path should not be null");
            assertNotNull(tile.edges, "Edges should not be null");
            assertNotNull(tile.centre, "Centre should not be null");
        }
    }
}
