package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/** Factory for creating game terrain for the maze mini-game. */
public class MazeMinigameTerrainFactory extends TerrainFactory {
  public static final GridPoint2 MAP_SIZE = new GridPoint2(12, 12);
  public static final float TILE_SIZE = 1;

  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be ortographic.
   */
  public MazeMinigameTerrainFactory(CameraComponent cameraComponent) {
    super(cameraComponent);
  }

  /**
   * Create terrain for the maze mini-game. There is only one Terrain type, the maze background
   * water.
   *
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain() {
    ResourceService resourceService = ServiceLocator.getResourceService();
    TextureRegion water =
            new TextureRegion(resourceService.getAsset("images/minigames/water.png", Texture.class));
    return createMazeTerrain(TILE_SIZE, water);
  }

  private TerrainComponent createMazeTerrain(
          float tileWorldSize, TextureRegion water) {
    GridPoint2 tilePixelSize = new GridPoint2(water.getRegionWidth(), water.getRegionHeight());
    TiledMap tiledMap = createWaterTiles(tilePixelSize, water);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, TerrainOrientation.ORTHOGONAL, tileWorldSize, MapHandler.MapType.MAZE_MINIGAME);
  }

  private TiledMap createWaterTiles(
          GridPoint2 tileSize, TextureRegion water) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile waterTile = new TerrainTile(water);
    TiledMapTileLayer layer = new TiledMapTileLayer(
            MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // Create base water
    fillTiles(layer, MAP_SIZE, waterTile);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }
}
