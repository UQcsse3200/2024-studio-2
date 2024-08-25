package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import java.util.HashMap;
import java.util.Map;

public class TerrainChunk {
  public static final int CHUNK_SIZE = 16;

  private static final int TERRAIN_LAYER = 0;
  private GridPoint2 position;
  private TiledMap tiledMap;
  private TiledMapTileLayer layer;
  private boolean loaded = false;

  private TextureRegion grass, grassTuft, rocks;

  TerrainChunk(GridPoint2 position, TiledMap map) {
    this.position = position;
    this.tiledMap = map;
    this.layer = (TiledMapTileLayer) map.getLayers().get(TERRAIN_LAYER);
  }


  public void generateTiles(GridPoint2 chunkPos, Map<GridPoint2, TerrainChunk> loadedChunks) {
  }

}


