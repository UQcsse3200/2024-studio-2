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
  private GridPoint2 mapSize;

  public static final int CHUNK_SIZE = 16;
  private static final int TUFT_TILE_COUNT = 1;
  private static final int ROCK_TILE_COUNT = 1;

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
  public TerrainComponent createTerrain(TerrainType terrainType, GridPoint2 playerPosition, GridPoint2 mapSize) {
    this.mapSize = mapSize;
    float tileWorldSize = 1.f;

    ResourceService resourceService = ServiceLocator.getResourceService();
    TextureRegion grass, grassTuft, rocks;

    // get asset
    grass = new TextureRegion(resourceService.getAsset("images/gt.png", Texture.class));
    grassTuft = new TextureRegion(resourceService.getAsset("images/grass_2.png", Texture.class));
    rocks = new TextureRegion(resourceService.getAsset("images/grass_3.png", Texture.class));

    GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
    // ------------------
    //TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass, grassTuft, rocks);

    TiledMap tiledMap = new TiledMap();
    TerrainTile grassTile = new TerrainTile(grass);
    TerrainTile grassTuftTile = new TerrainTile(grassTuft);
    TerrainTile rockTile = new TerrainTile(rocks);
    TiledMapTileLayer layer = new TiledMapTileLayer(this.mapSize.x, this.mapSize.y, tilePixelSize.x, tilePixelSize.y);

    // Create base grass
    //fillTiles(layer, mapSize, grassTile);

    // Add some grass and rocks
    //fillTilesAtRandom(layer, mapSize, grassTuftTile, TUFT_TILE_COUNT);
    //fillTilesAtRandom(layer, mapSize, rockTile, ROCK_TILE_COUNT);

    System.out.println("------");
    System.out.println(layer.getWidth());
    System.out.println(layer.getHeight());
    layer.setName("forest");
    tiledMap.getLayers().add(layer);
    //return tiledMap;
    // ---------------------

    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
    //return createForestDemoTerrain(1.f, grass, grassTuft, rocks);
    //return createDynamicTerrain(1f, grass, grassTuft, rocks, playerPosition);
  }

  //public void fillChunk(TiledMapTileLayer layer, GridPoint2 position) {
  //  for (int x = 0; x < mapSize.x; x++) {
  //    for (int y = 0; y < mapSize.y; y++) {
  //      Cell cell = new Cell();
  //      cell.setTile(tile);
  //      layer.setCell(x, y, cell);
  //    }
  //  }
  //}


  private TerrainComponent createDynamicTerrain(
      float tileWorldSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks, GridPoint2 playerPosition) {

    TiledMap tiledMap = new TiledMap();

    // Calculate player's chunk position
    GridPoint2 playerChunk = new GridPoint2((int) Math.floor((float) playerPosition.x / CHUNK_SIZE), 
                                            (int) Math.floor((float) playerPosition.y / CHUNK_SIZE));

    // Load the 5x5 (3x3 expanded by 1 towards each direction) grid of chunks around the player (to preload chunks one chunk away)
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
        if (dx <= 1 && dy <= 1 && dx >= -1 && dy >= -1) {
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

  //--------------------------------------------------------
  //private TerrainComponent createForestDemoTerrain(
  //    float tileWorldSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
  //  GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
  //  System.out.println("Tile size: " + tilePixelSize.x + " " + tilePixelSize.y);
  //  TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass, grassTuft, rocks);
  //  TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
  //  return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  //}

  private TiledMap createForestDemoTiles(
      GridPoint2 tileSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile grassTile = new TerrainTile(grass);
    TerrainTile grassTuftTile = new TerrainTile(grassTuft);
    TerrainTile rockTile = new TerrainTile(rocks);
    TiledMapTileLayer layer = new TiledMapTileLayer(this.mapSize.x, this.mapSize.y, tileSize.x, tileSize.y);

    // Create base grass
    fillTiles(layer, mapSize, grassTile);

    // Add some grass and rocks
    //fillTilesAtRandom(layer, mapSize, grassTuftTile, TUFT_TILE_COUNT);
    //fillTilesAtRandom(layer, mapSize, rockTile, ROCK_TILE_COUNT);

    System.out.println("------");
    System.out.println(layer.getWidth());
    System.out.println(layer.getHeight());
    tiledMap.getLayers().add(layer);
    return tiledMap;
  }
  //--------------------------------------------

  private TiledMapTileLayer createForestDemoChunk(
    int chunkX, int chunkY, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
    GridPoint2 tileSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
    TiledMapTileLayer layer = new TiledMapTileLayer(CHUNK_SIZE, CHUNK_SIZE, tileSize.x, tileSize.y);
    TerrainTile grassTile = new TerrainTile(grass);
    TerrainTile grassTuftTile = new TerrainTile(grassTuft);
    TerrainTile rockTile = new TerrainTile(rocks);

    // Create base grass for the chunk
    fillTiles(layer, this.mapSize, grassTile);

    // Add some grass and rocks within the chunk
    fillTilesAtRandom(layer, CHUNK_SIZE, grassTuftTile, TUFT_TILE_COUNT);
    fillTilesAtRandom(layer, CHUNK_SIZE, rockTile, ROCK_TILE_COUNT);

    // Offset the chunk's position on the map, correcting the Y-axis direction
    layer.setOffsetX(chunkX * CHUNK_SIZE * tileSize.x);
    layer.setOffsetY(-chunkY * CHUNK_SIZE * tileSize.y);  // Notice the negative sign here

    return layer;
  }

  private static void fillTilesAtRandom(
      TiledMapTileLayer layer, int mapSize, TerrainTile tile, int amount) {
    GridPoint2 min = new GridPoint2(0, 0);
    GridPoint2 max = new GridPoint2(mapSize - 1, mapSize - 1);

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(min, max);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }

  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    Cell cell = new Cell();
    cell.setTile(tile);
    for (int x = 0; x < 16; ++x)
      layer.setCell(x, 0, cell);
    //layer.setCell(18, 1, cell);
    //layer.setCell(18, 18, cell);
    //for (int x = 0; x < mapSize.x; x++) {
    //  for (int y = 0; y < mapSize.y; y++) {
    //    Cell cell = new Cell();
    //    cell.setTile(tile);
    //    layer.setCell(x, y, cell);
    //  }
    //}
  }

  public enum TerrainType {
    FOREST_DEMO,
    FOREST_DEMO_ISO,
    FOREST_DEMO_HEX
  }
}
