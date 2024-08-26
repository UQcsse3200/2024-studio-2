package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.areas.terrain.TerrainChunk;

import java.util.HashMap;
import java.util.Map;

/**
 * Render a tiled terrain for a given tiled map and orientation. A terrain is a
 * map of tiles that
 * shows the 'ground' in the game. Enabling/disabling this component will
 * show/hide the terrain.
 */
public class TerrainComponent extends RenderComponent {
  public static final int CHUNK_SIZE = 16;

  private static final int TERRAIN_LAYER = 0;
  private TiledMap tiledMap;
  private TiledMapRenderer tiledMapRenderer;
  private OrthographicCamera camera;
  private TerrainOrientation orientation;
  private float tileSize;

  // private TextureRegion grass, grassTuft, rocks;
  private Map<GridPoint2, TerrainChunk> loadedChunks = new HashMap<>();
  private TerrainResource terrainResource;

  public TerrainComponent(
      OrthographicCamera camera,
      TiledMap map,
      TiledMapRenderer renderer,
      TerrainOrientation orientation,
      float tileSize) {
    this.camera = camera;
    this.tiledMap = map;
    this.orientation = orientation;
    this.tileSize = tileSize;
    this.tiledMapRenderer = renderer;

    this.terrainResource = new TerrainResource();
  }

  public Vector2 tileToWorldPosition(GridPoint2 tilePos) {
    return tileToWorldPosition(tilePos.x, tilePos.y);
  }

  public Vector2 tileToWorldPosition(int x, int y) {
    switch (orientation) {
      case HEXAGONAL:
        float hexLength = tileSize / 2;
        float yOffset = (x % 2 == 0) ? 0.5f * tileSize : 0f;
        return new Vector2(x * (tileSize + hexLength) / 2, y + yOffset);
      case ISOMETRIC:
        return new Vector2((x + y) * tileSize / 2, (y - x) * tileSize / 2);
      case ORTHOGONAL:
        return new Vector2(x * tileSize, y * tileSize);
      default:
        return null;
    }
  }

  public void getChunks() {
  }

  public void fillChunk(GridPoint2 chunkPos) {

    // Check if the chunk is within the bounds of the map
    if ((chunkPos.x < 0 || chunkPos.y < 0) ||
        (chunkPos.x >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth() ||
            chunkPos.y >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight()))
      return;

    // Check if the chunk is already loaded
    if (!loadedChunks.containsKey(chunkPos)) {
      TerrainChunk chunk = new TerrainChunk(chunkPos, tiledMap);
      TerrainTile grassTile = new TerrainTile(this.terrainResource.getGrass());
      // TerrainTile grassTuftTile = new TerrainTile(grassTuft);
      // TerrainTile rockTile = new TerrainTile(rocks);

      for (int x = chunkPos.x * CHUNK_SIZE; x < (chunkPos.x + 1) * CHUNK_SIZE; x++) {
        for (int y = chunkPos.y * CHUNK_SIZE; y < (chunkPos.y + 1) * CHUNK_SIZE; y++) {
          Cell cell = new Cell();
          cell.setTile(grassTile);
          ((TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER)).setCell(x, y, cell);
        }
      }

      loadedChunks.put(chunkPos, chunk);
      System.out.println("Loading new chunk at " + chunkPos);
    } else {
      System.out.println("Already loaded chunk at " + chunkPos);
    }

  }

  /*
   * Generate a grid of 3x3 chunks with the player being in the center chunk.
   */
  public void loadChunks(GridPoint2 chunkPos) {
    for (int x = chunkPos.x - 1; x <= chunkPos.x + 1; x++) {
      for (int y = chunkPos.y - 1; y <= chunkPos.y + 1; y++) {
        fillChunk(new GridPoint2(x, y));
      }
    }
  }

  public float getTileSize() {
    return tileSize;
  }

  public GridPoint2 getMapBounds(int layer) {
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layer);
    return new GridPoint2(terrainLayer.getWidth(), terrainLayer.getHeight());
  }

  public TiledMap getMap() {
    return tiledMap;
  }

  @Override
  public void draw(SpriteBatch batch) {
    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render();
  }

  @Override
  public void dispose() {
    tiledMap.dispose();
    super.dispose();
  }

  @Override
  public float getZIndex() {
    return 0f;
  }

  @Override
  public int getLayer() {
    return TERRAIN_LAYER;
  }

  public enum TerrainOrientation {
    ORTHOGONAL,
    ISOMETRIC,
    HEXAGONAL
  }

  public class TerrainResource {
    public TextureRegion grass;
    public TextureRegion grassTuft;
    public TextureRegion rocks;

    public TerrainResource() {
      ResourceService resourceService = ServiceLocator.getResourceService();

      // get asset
      this.grass = new TextureRegion(resourceService.getAsset("images/gt.png", Texture.class));
      this.grassTuft = new TextureRegion(resourceService.getAsset("images/grass_2.png", Texture.class));
      this.rocks = new TextureRegion(resourceService.getAsset("images/grass_3.png", Texture.class));
    }

    private TextureRegion getGrass() {
      return this.grass;
    }

    private TextureRegion getGrassTuft() {
      return this.grassTuft;
    }

    private TextureRegion getRocks() {
      return this.rocks;
    }

  }
}
