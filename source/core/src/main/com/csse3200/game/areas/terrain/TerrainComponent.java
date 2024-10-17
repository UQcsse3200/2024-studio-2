package com.csse3200.game.areas.terrain;
import java.util.*;

import com.csse3200.game.areas.terrain.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.MapHandler.MapType;
import com.csse3200.game.rendering.RenderComponent;

/**
 * Render a tiled terrain for a given tiled map and orientation. A terrain is a
 * map of tiles that
 * shows the 'ground' in the game. Enabling/disabling this component will
 * show/hide the terrain.
 */
public class TerrainComponent extends RenderComponent {
  private static final Logger logger = LoggerFactory.getLogger(TerrainComponent.class);
  public static final int CHUNK_SIZE = 16;

  private static final int TERRAIN_LAYER = 0;
  private TiledMap tiledMap;
  private TiledMapRenderer tiledMapRenderer;
  private OrthographicCamera camera;
  private TerrainOrientation orientation;
  private float tileSize;
  
  // TODO: THESE ARE TEMPORARY PLACEHOLDERS FOR THE TILES - IN FUTURE THEY NEED TO BE CONVERTED
  //  TO TILED MAP SETS I WOULD IMAGINE (MAYBE NOT THO, WHO KNOWS)!
  private Set<GridPoint2> activeChunks = new HashSet<>();
  private Set<GridPoint2> previouslyActive = new HashSet<>();
  private Set<GridPoint2> newChunks = new HashSet<>();
  private Set<GridPoint2> oldChunks = new HashSet<>();
  private Map<GridPoint2, TerrainChunk> loadedChunks = new HashMap<>();
  private TerrainResource terrainResource;

  private TiledMapRenderer renderer;

  // Constructor and other methods...

  // Package-private or protected method for testing
  TiledMapRenderer getRenderer() {
    return renderer;
  }

  public TerrainComponent(
      OrthographicCamera camera,
      TiledMap map,
      TiledMapRenderer renderer,
      TerrainOrientation orientation,
      float tileSize,
      MapType mapType) {
    this.camera = camera;
    this.tiledMap = map;
    this.orientation = orientation;
    this.tileSize = tileSize;
    this.tiledMapRenderer = renderer;
    this.terrainResource = new TerrainResource(mapType);
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

  /**
   * Fill a chunk with tiles if it is not already loaded.
   *
   * @param chunkPos The position of the chunk to fill
   */
  public void fillChunk(GridPoint2 chunkPos) {
    // Check if the chunk is within the bounds of the map
    if (loadedChunks.containsKey(chunkPos))
      return;

    TerrainChunk chunk = new TerrainChunk(chunkPos, tiledMap);

    if ((chunkPos.x < 0 || chunkPos.y < 0) ||
            (chunkPos.x >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth() ||
                    chunkPos.y >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight()))
      return;

    chunk.generateTiles(chunkPos, loadedChunks, terrainResource);
    loadedChunks.put(chunkPos, chunk);
  }

  /**
   * Load all chunks around the given chunk position.
   *
   * @param chunkPos The position of the chunk to load around
   */
  public void loadChunks(GridPoint2 chunkPos) {
    loadChunks(chunkPos, 3);
  }

  /**
   * Load all chunks in a given radius (a square - not circle) around the given
   * chunk position.
   *
   * @param chunkPos The position of the chunk to load around
   * @param r        The number of chunks away to spawn
   */
  public void loadChunks(GridPoint2 chunkPos, int r) {
    // Reset active chunk status
    previouslyActive.clear();
    previouslyActive.addAll(activeChunks);
    activeChunks.clear();

    // Iterate over all chunks in a square of radius r around the player and spawn
    // them in.
    int[] moves = java.util.stream.IntStream.rangeClosed(-r, r).toArray();
    for (int dx : moves) {
      for (int dy : moves) {
        GridPoint2 pos = new GridPoint2(chunkPos.x + dx, chunkPos.y + dy);
        logger.debug("Loading Chunk at {}, {}", pos.x, pos.y);
        fillChunk(pos);
        activeChunks.add(pos);
      }
    }

    updateChunkStatus();
  }

  private void updateChunkStatus() {
    newChunks.clear();
    newChunks.addAll(activeChunks);
    newChunks.removeAll(previouslyActive);

    oldChunks.clear();
    oldChunks.addAll(previouslyActive);
    oldChunks.removeAll(activeChunks);
  }

  public TerrainChunk getChunk(GridPoint2 chunkPos) {
    return loadedChunks.get(chunkPos);
  }

  public Set<GridPoint2> getNewChunks() {
    return newChunks;
  }

  public Set<GridPoint2> getOldChunks() {
    return oldChunks;
  }

  public Set<GridPoint2> getActiveChunks() {
    return activeChunks;
  }

  /**
   * Get the size of all tiles in the terrain.
   *
   * @return The size of all tiles in the terrain
   */
  public float getTileSize() {
    return tileSize;
  }

  /**
   * Get the bounds of the terrain map.
   * 
   * @param layer the bounds of the layer
   * @return The bounds of the terrain map
   */
  public GridPoint2 getMapBounds(int layer) {
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layer);
    return new GridPoint2(terrainLayer.getWidth(), terrainLayer.getHeight());
  }

  /**
   * Get current map
   * 
   * @return The current map
   */
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

  public TiledMap getTiledMap()
  {

      return null;
  }
}
