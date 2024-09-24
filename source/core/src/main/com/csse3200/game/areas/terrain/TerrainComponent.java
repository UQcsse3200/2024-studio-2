package com.csse3200.game.areas.terrain;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.MapHandler.MapType;
import com.csse3200.game.areas.ForestGameAreaConfigs.ForestTileConfig;
import com.csse3200.game.areas.ForestGameAreaConfigs.ForestMapTiles;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

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
  private MapType mapType;

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
    this.mapType = mapType;

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

  public enum TerrainOrientation {
    ORTHOGONAL,
    ISOMETRIC,
    HEXAGONAL
  }

  /**
   * TerrainResource class to store all possible tiles and their edge tiles.
   */
  public class TerrainResource {
    private ArrayList<Tile> tiles;

    // total number of tiles
    public static int TILE_SIZE = 0;

    public TerrainResource(MapType mapType) {
      ResourceService resourceService = ServiceLocator.getResourceService();
      tiles = new ArrayList<Tile>();
      switch(mapType) {
        case FOREST:
          ForestMapTiles tileConfig;
          tileConfig = FileLoader.readClass(ForestMapTiles.class, "configs/ForestGameAreaConfigs/forestTiles.json");
          System.out.println("Tile Config: " + tileConfig);

         for (ForestTileConfig tile : tileConfig.forestMapTiles) {
            // edge: TOP, RIGHT, BOTTOM, LEFT
            // A: sand, B: grass, C: water
            // =======================
            tiles.add(new Tile(tile.id, new TextureRegion(resourceService.getAsset(tile.fp, Texture.class)), tile.edges));
            TILE_SIZE = tiles.size();
          }
          break;
        case WATER:
          break;
        case COMBAT:
          break;
        default:
          throw new IllegalArgumentException("Map type not supported: " + mapType);
      }

      this.setPossibleTiles();
    }

    /**
     * Set all possible tiles for each tile for all directions.
     */
    public void setPossibleTiles() {
      for (int i = 0; i < this.tiles.size(); i++) {
        setPossibleUp(this.tiles.get(i));
        setPossibleRight(this.tiles.get(i));
        setPossibleDown(this.tiles.get(i));
        setPossibleLeft(this.tiles.get(i));
      }
    }

    /**
     * Set possible tiles for up direction.
     * 
     * @param tile The tile to set possible tiles
     */
    public void setPossibleUp(Tile tile) {
      BitSet up = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(2).equals(tile.getEdgeTiles().get(0))) {
          up.set(i, true);
        }
      }
      tile.setPossibleUp(up);
    }

    /**
     * Set possible tiles for right direction.
     * 
     * @param tile The tile to set possible tiles
     */
    public void setPossibleRight(Tile tile) {
      BitSet right = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(3).equals(tile.getEdgeTiles().get(1))) {
          right.set(i, true);
        }
      }
      tile.setPossibleRight(right);
    }

    /**
     * Set possible tiles for down direction.
     * 
     * @param tile The tile to set possible tiles
     */
    public void setPossibleDown(Tile tile) {
      BitSet down = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(0).equals(tile.getEdgeTiles().get(2))) {
          down.set(i, true);
        }
      }
      tile.setPossibleDown(down);
    }

    /**
     * Set possible tiles for left direction.
     * 
     * @param tile The tile to set possible tiles
     */
    public void setPossibleLeft(Tile tile) {
      BitSet left = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(1).equals(tile.getEdgeTiles().get(3))) {
          left.set(i, true);
        }
      }
      tile.setPossibleLeft(left);
    }

    /**
     * Get a tile by name.
     * 
     * @param name The name of the tile
     * @return The tile with the given name
     */
    public Tile getTilebyName(String name) {
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).name == name) {
          return this.tiles.get(i);
        }
      }
      return null;
    }

    /**
     * Get a tile by index.
     * 
     * @param index The index of the tile
     * @return The tile with the given index
     */
    public Tile getTilebyIndex(int index) {
      return this.tiles.get(index);
    }

    /**
     * Get all tiles.
     * 
     * @return All loaded tiles
     */
    public ArrayList<Tile> getAllTiles() {
      return this.tiles;
    }

    /**
     * Randomly pick a tile from the list of tiles.
     * 
     * @return A random tile
     */
    public Tile random_pick() {
      return this.tiles.get((int) (Math.random() * this.tiles.size()));
    }
  }

  /**
   * Tile class to store tile data.
   * A tile has a name, texture, and edge tiles.
   */
  public class Tile {
    // data for wave function collapse
    private TextureRegion texture;
    private ArrayList<String> edgeTiles;
    private String name;

    // all possible tiles
    private BitSet up = new BitSet(TerrainResource.TILE_SIZE);
    private BitSet right = new BitSet(TerrainResource.TILE_SIZE);
    private BitSet down = new BitSet(TerrainResource.TILE_SIZE);
    private BitSet left = new BitSet(TerrainResource.TILE_SIZE);

    public boolean collapsed = false;

    public Tile(String name, TextureRegion texture, ArrayList<String> edgeTiles) {
      this.name = name;
      this.texture = texture;
      this.edgeTiles = edgeTiles;
    }

    /**
     * Get the name of the tile.
     * 
     * @return The name of the tile
     */
    public String getName() {
      return name;
    }

    /**
     * Get the texture of the tile.
     * 
     * @return The texture of the tile
     */
    public TextureRegion getTexture() {
      return texture;
    }

    /**
     * Get the edge tiles of the tile.
     * 
     * @return The edge tiles of the tile
     */
    public ArrayList<String> getEdgeTiles() {
      return edgeTiles;
    }

    /**
     * Get the up edge tiles of the tile.
     * 
     * @return The up edge tiles of the tile
     */
    public BitSet getUp() {
      return up;
    }

    /**
     * Get the right edge tiles of the tile.
     * 
     * @return The right edge tiles of the tile
     */
    public BitSet getRight() {
      return right;
    }

    /**
     * Get the down edge tiles of the tile.
     * 
     * @return The down edge tiles of the tile
     */
    public BitSet getDown() {
      return down;
    }

    /**
     * Get the left edge tiles of the tile.
     * 
     * @return The left edge tiles of the tile
     */
    public BitSet getLeft() {
      return left;
    }

    /**
     * Set the possible up edge tiles of the tile.
     * 
     * @param up All up edge tiles of the tile
     */
    public void setPossibleUp(BitSet up) {
      this.up = up;
    }

    /**
     * Set the possible right edge tiles of the tile.
     * 
     * @param right All right edge tiles of the tile
     */
    public void setPossibleRight(BitSet right) {
      this.right = right;
    }

    /**
     * Set the possible down edge tiles of the tile.
     * 
     * @param down All down edge tiles of the tile
     */
    public void setPossibleDown(BitSet down) {
      this.down = down;
    }

    /**
     * Set the possible left edge tiles of the tile.
     * 
     * @param left All left edge tiles of the tile
     */
    public void setPossibleLeft(BitSet left) {
      this.left = left;
    }
  }

}
