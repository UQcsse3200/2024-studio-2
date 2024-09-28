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
import com.csse3200.game.areas.FogGameAreaConfigs.FogMapTiles;
import com.csse3200.game.areas.FogGameAreaConfigs.FogTileConfig;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.areas.MapHandler.MapType;
import com.csse3200.game.areas.ForestGameAreaConfigs.ForestTileConfig;
import com.csse3200.game.areas.ForestGameAreaConfigs.ForestMapTiles;
import com.csse3200.game.areas.OceanGameAreaConfigs.OceanMapTiles;
import com.csse3200.game.areas.OceanGameAreaConfigs.OceanTileConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.*;
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
  private static final int FOG_LAYER = 0;
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

  private int unlockedArea = 1;

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
    //fillChunk(chunkPos);

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
    private ArrayList<Tile> forestTiles;
    private ArrayList<Tile> waterTiles;
    private ArrayList<Tile> airTiles;
    private ArrayList<Tile> fogTiles;
    // total number of tiles
    public static int FOREST_SIZE = 0;
    public static int WATER_SIZE = 0;
    public static int AIR_SIZE = 0;
    public static int FOG_SIZE = 0;
    private boolean unlockedWater;

    public TerrainResource(MapType mapType) {
      ResourceService resourceService = ServiceLocator.getResourceService();
      forestTiles = new ArrayList<Tile>();
      waterTiles = new ArrayList<Tile>();
      airTiles = new ArrayList<Tile>();
      fogTiles = new ArrayList<Tile>();
      this.unlockedWater = false;
      switch(mapType) {
        case FOREST:
          // load forest tiles
          ForestMapTiles tileConfig;
          tileConfig = FileLoader.readClass(ForestMapTiles.class, "configs/ForestGameAreaConfigs/forestTiles.json");
          for (ForestTileConfig tile : tileConfig.forestMapTiles) {
            // edge: TOP, RIGHT, BOTTOM, LEFT
            // A: sand, B: grass, C: water
            // =======================
            forestTiles.add(new Tile(tile.id, 
                new TextureRegion(resourceService.getAsset(tile.fp, Texture.class)), 
                tile.edges, 
                tile.centre));
          }
          FOREST_SIZE = forestTiles.size();

          // load water tiles
          OceanMapTiles oceanTileConfig;
          FogMapTiles fogTileConfig;
          oceanTileConfig = FileLoader.readClass(OceanMapTiles.class, "configs/OceanGameAreaConfigs/waterTiles.json");
          fogTileConfig = FileLoader.readClass(FogMapTiles.class, "configs/FogGameAreaConfigs/fogTiles.json");

          //if (this.unlockedWater) {
            for (OceanTileConfig tile : oceanTileConfig.waterMapTiles) {
              waterTiles.add(new Tile(tile.id,
                      new TextureRegion(resourceService.getAsset(tile.fp, Texture.class)),
                      tile.edges,
                      tile.centre));
            }
            WATER_SIZE = waterTiles.size();
//          } else {
//            for (FogTileConfig tile : fogTileConfig.fogTiles) {
//              fogTiles.add(new Tile(tile.id,
//                      new TextureRegion(resourceService.getAsset(tile.fp, Texture.class)),
//                      tile.edges,
//                      tile.centre));
//            }
//            FOG_SIZE = fogTiles.size();
//          }

          // load air tiles
          //AirMapTiles airTileConfig;
          //airTileConfig = FileLoader.readClass(AirMapTiles.class, "configs/AirGameAreaConfigs/airTiles.json");
          //for (AirTileConfig tile : airTileConfig.airMapTiles) {
          //  // edge: TOP, RIGHT, BOTTOM, LEFT
          //  airTiles.add(new Tile(tile.id, new TextureRegion(resourceService.getAsset(tile.fp, Texture.class)), tile.edges, tile.centre));
          //  tiles.add(new Tile(tile.id, 
          //  new TextureRegion(resourceService.getAsset(tile.fp, Texture.class)), 
          //  tile.edges, 
          //  tile.centre));
          //}
          //AIR_SIZE = airTiles.size();
          break;
        case COMBAT:
          break;
        default:
          throw new IllegalArgumentException("Map type not supported: " + mapType);
      }

      this.setPossibleTiles();
    }

    /**
     * checks if the water map is unlcked yet
     * @return true iff the map is unlocked
     */
    public boolean hasUnlockedWaterMap() {
      return this.unlockedWater;
    }

    /**
     * sets the state of unlocked water map
     * @param unlockedWater the state of unlocked map
     */
    public void setUnlockedWater(boolean unlockedWater) {
      this.unlockedWater = unlockedWater;
    }

    public ArrayList<Tile> getMapTiles(MapType mapType) {
      switch(mapType) {
        case FOREST:
          return forestTiles;
        case WATER:
          return waterTiles;
        case AIR:
          return airTiles;
        case COMBAT:
          return null;
        default:
          throw new IllegalArgumentException("No such map type:" + mapType);
      }
    }

    /**
     * Set all possible tiles for each tile for all directions.
     */
    public void setPossibleTiles() {
      for (int i = 0; i < this.forestTiles.size(); i++) {
        setPossibleUp(this.forestTiles.get(i), this.forestTiles);
        setPossibleRight(this.forestTiles.get(i), this.forestTiles);
        setPossibleDown(this.forestTiles.get(i), this.forestTiles);
        setPossibleLeft(this.forestTiles.get(i), this.forestTiles);
      }

      for (int i = 0; i < this.waterTiles.size(); i++) {
        setPossibleUp(this.waterTiles.get(i), this.waterTiles);
        setPossibleRight(this.waterTiles.get(i), this.waterTiles);
        setPossibleDown(this.waterTiles.get(i), this.waterTiles);
        setPossibleLeft(this.waterTiles.get(i), this.waterTiles);
      }

      //for (int i = 0; i < this.airTiles.size(); i++) {
      //  setPossibleUp(this.airTiles.get(i), this.airTiles);
      //  setPossibleRight(this.airTiles.get(i), this.airTiles);
      //  setPossibleDown(this.airTiles.get(i), this.airTiles);
      //  setPossibleLeft(this.airTiles.get(i), this.airTiles);
      //}
    }

    /**
     * Set possible tiles for up direction.
     * 
     * @param tile The tile to set possible tiles
     */
    public void setPossibleUp(Tile tile, ArrayList<Tile> areaTiles) {
      BitSet up = new BitSet(areaTiles.size());
      for (int i = 0; i < areaTiles.size(); i++) {
        if (areaTiles.get(i).getEdgeTiles().get(2).equals(tile.getEdgeTiles().get(0))) {
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
    public void setPossibleRight(Tile tile, ArrayList<Tile> areaTiles) {
      BitSet right = new BitSet(areaTiles.size());
      for (int i = 0; i < areaTiles.size(); i++) {
        if (areaTiles.get(i).getEdgeTiles().get(3).equals(tile.getEdgeTiles().get(1))) {
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
    public void setPossibleDown(Tile tile, ArrayList<Tile> areaTiles) {
      BitSet down = new BitSet(areaTiles.size());
      for (int i = 0; i < areaTiles.size(); i++) {
        if (areaTiles.get(i).getEdgeTiles().get(0).equals(tile.getEdgeTiles().get(2))) {
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
    public void setPossibleLeft(Tile tile, ArrayList<Tile> areaTiles) {
      BitSet left = new BitSet(areaTiles.size());
      for (int i = 0; i < areaTiles.size(); i++) {
        if (areaTiles.get(i).getEdgeTiles().get(1).equals(tile.getEdgeTiles().get(3))) {
          left.set(i, true);
        }
      }
      tile.setPossibleLeft(left);
    }

    /**
     * Get a tile by index and map type.
     * 
     * @param index The index of the tile
     * @param mapType The map type of the tile
     * @return The tile with the given index and map type
     */
    public Tile getMapTilebyIndex(int index, MapType mapType) {
      switch(mapType) {
        case FOREST:
          return forestTiles.get(index);
        case WATER:
          return waterTiles.get(index);
        case AIR:
          return airTiles.get(index);
        case FOG:
          return fogTiles.get(index);
        case COMBAT:
          return null;
        default:
          throw new IllegalArgumentException("No such map type:" + mapType);
      }
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
    private String centre;

    // all possible tiles
    private BitSet up = new BitSet();
    private BitSet right = new BitSet();
    private BitSet down = new BitSet();
    private BitSet left = new BitSet();

    public boolean collapsed = false;

    public Tile(String name, TextureRegion texture, ArrayList<String> edgeTiles, String centre) {
      this.name = name;
      this.texture = texture;
      this.edgeTiles = edgeTiles;
      this.centre = centre;
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
     * Get the centre tile of the tile.
     * 
     * @return The centre tile of the tile
     */
    public String getCentre() {
      return centre;
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
