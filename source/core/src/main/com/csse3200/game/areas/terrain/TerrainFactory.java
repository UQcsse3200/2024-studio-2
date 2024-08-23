package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import java.util.HashMap;
import java.util.Map;

/** Factory for creating game terrains. */
public class TerrainFactory {
  public static final GridPoint2 CHUNK_SIZE = new GridPoint2(2,2); // Minimum chunk size: 2x2
  private static final int TUFT_TILE_COUNT = 30;
  private static final int ROCK_TILE_COUNT = 30;

  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;
  private final Map<GridPoint2, TiledMapTileLayer> loadedChunks = new HashMap<>();

  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this(cameraComponent, TerrainOrientation.ORTHOGONAL);
  }

  /**
   * Create a terrain factory
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   * @param orientation orientation to render terrain at
   */
  public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
    this.orientation = orientation;
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory.
   * This method loads or generates the terrain chunks around the player's current position.
   *
   * @param terrainType Terrain to create
   * @param playerPosition The current position of the player in the world
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TerrainType terrainType, GridPoint2 playerPosition) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    TextureRegion grass, grassTuft, rocks;

    switch (terrainType) {
      case FOREST_DEMO:
        grass = new TextureRegion(resourceService.getAsset("images/grass_1.png", Texture.class));
        grassTuft = new TextureRegion(resourceService.getAsset("images/grass_2.png", Texture.class));
        rocks = new TextureRegion(resourceService.getAsset("images/grass_3.png", Texture.class));
        break;
      case FOREST_DEMO_ISO:
        grass = new TextureRegion(resourceService.getAsset("images/iso_grass_1.png", Texture.class));
        grassTuft = new TextureRegion(resourceService.getAsset("images/iso_grass_2.png", Texture.class));
        rocks = new TextureRegion(resourceService.getAsset("images/iso_grass_3.png", Texture.class));
        break;
      case FOREST_DEMO_HEX:
        grass = new TextureRegion(resourceService.getAsset("images/hex_grass_1.png", Texture.class));
        grassTuft = new TextureRegion(resourceService.getAsset("images/hex_grass_2.png", Texture.class));
        rocks = new TextureRegion(resourceService.getAsset("images/hex_grass_3.png", Texture.class));
        break;
      default:
        return null;
    }

    return createDynamicTerrain(1f, grass, grassTuft, rocks, playerPosition);
  }

  private TerrainComponent createDynamicTerrain(
      float tileWorldSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks, GridPoint2 playerPosition) {

    TiledMap tiledMap = new TiledMap();

    // Calculate player's chunk position
    GridPoint2 playerChunk = new GridPoint2((int) Math.floor((float) playerPosition.x / CHUNK_SIZE.x), 
                                            (int) Math.floor((float) playerPosition.y / CHUNK_SIZE.y));

    // Load the 5x5 grid of chunks around the player (to preload chunks one chunk away)
    for (int dx = -2; dx <= 2; dx++) {
      for (int dy = -2; dy <= 2; dy++) {
        GridPoint2 chunkPos = new GridPoint2(playerChunk.x + dx, playerChunk.y + dy);

        if (!loadedChunks.containsKey(chunkPos)) {
          TiledMapTileLayer chunkLayer = createForestDemoChunk(chunkPos.x, chunkPos.y, grass, grassTuft, rocks);
          loadedChunks.put(chunkPos, chunkLayer);
          System.out.println("Loading new chunk at " + chunkPos);
        } else {
          System.out.println("Already loaded chunk at " + chunkPos);
        }

        // Only add the chunk to the tiledMap if it is within the 3x3 grid centered on the player
        if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
          tiledMap.getLayers().add(loadedChunks.get(chunkPos));
        }
      }
    }

    // Adjust the scale factor to properly size the tiles
    float tileScale = tileWorldSize / grass.getRegionWidth();
    TiledMapRenderer renderer = createRenderer(tiledMap, tileScale);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    switch (orientation) {
      case ORTHOGONAL:
        return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
      case ISOMETRIC:
        return new IsometricTiledMapRenderer(tiledMap, tileScale);
      case HEXAGONAL:
        return new HexagonalTiledMapRenderer(tiledMap, tileScale);
      default:
        return null;
    }
  }

  private TiledMapTileLayer createForestDemoChunk(
    int chunkX, int chunkY, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
    GridPoint2 tileSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
    TiledMapTileLayer layer = new TiledMapTileLayer(CHUNK_SIZE.x, CHUNK_SIZE.y, tileSize.x, tileSize.y);
    TerrainTile grassTile = new TerrainTile(grass);
    TerrainTile grassTuftTile = new TerrainTile(grassTuft);
    TerrainTile rockTile = new TerrainTile(rocks);

    // Create base grass for the chunk
    fillTiles(layer, CHUNK_SIZE, grassTile);

    // Add some grass and rocks within the chunk
    fillTilesAtRandom(layer, CHUNK_SIZE, grassTuftTile, TUFT_TILE_COUNT);
    fillTilesAtRandom(layer, CHUNK_SIZE, rockTile, ROCK_TILE_COUNT);

    // Offset the chunk's position on the map, correcting the Y-axis direction
    layer.setOffsetX(chunkX * CHUNK_SIZE.x * tileSize.x);
    layer.setOffsetY(-chunkY * CHUNK_SIZE.y * tileSize.y);  // Notice the negative sign here

    return layer;
  }

  private static void fillTilesAtRandom(
      TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile, int amount) {
    GridPoint2 min = new GridPoint2(0, 0);
    GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(min, max);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }

  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }

  public enum TerrainType {
    FOREST_DEMO,
    FOREST_DEMO_ISO,
    FOREST_DEMO_HEX
  }
}
