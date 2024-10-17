package com.csse3200.game.areas.terrain.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    private Tile tile;

    @BeforeEach
    public void setUp() {
        TextureRegion texture = new TextureRegion();
        List<String> edgeTiles = new ArrayList<>();
        edgeTiles.add("edge1");
        edgeTiles.add("edge2");
        tile = new Tile("TestTile", texture, edgeTiles, "CentreTile");
    }

    @Test
    public void testGetName() {
        assertEquals("TestTile", tile.getName());
    }

    @Test
    public void testGetTexture() {
        assertNotNull(tile.getTexture());
    }

    @Test
    public void testGetEdgeTiles() {
        assertEquals(2, tile.getEdgeTiles().size());
    }

    @Test
    public void testGetCentre() {
        assertEquals("CentreTile", tile.getCentre());
    }
}

