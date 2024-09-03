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
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
//import com.badlogic.gdx.utils.Array;


/**
 * Render a tiled terrain for a given tiled map and orientation. A terrain is a
 * map of tiles that
 * shows the 'ground' in the game. Enabling/disabling this component will
 * show/hide the terrain.
 */
public class TerrainComponent extends RenderComponent {
  public static final int CHUNK_SIZE = 16;

  private static final int TERRAIN_LAYER = 0;
  private static TiledMap tiledMap;
  private TiledMapRenderer tiledMapRenderer;
  private OrthographicCamera camera;
  private TerrainOrientation orientation;
  private float tileSize;

  private static Map<GridPoint2, TerrainChunk> loadedChunks = new HashMap<>();
  private static TerrainResource terrainResource;

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


  /**
   * Fill a chunk with tiles if it is not already loaded.
   *
   * @param chunkPos The position of the chunk to fill
   */
  public static void fillChunk(GridPoint2 chunkPos) {

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
  public static void loadChunks(GridPoint2 chunkPos) {
    int[] moves = {-1, 0, 1};
    for (int dx : moves) {
      for (int dy : moves) {
        fillChunk(new GridPoint2(chunkPos.x + dx, chunkPos.y + dy));
      }
    }
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
   * @param layer the bounds of the layer
   * @return The bounds of the terrain map
   */
  public GridPoint2 getMapBounds(int layer) {
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layer);
    return new GridPoint2(terrainLayer.getWidth(), terrainLayer.getHeight());
  }

  /**
   * Get current map
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

  public enum TerrainOrientation {
    ORTHOGONAL,
    ISOMETRIC,
    HEXAGONAL
  }

  /**
   * TerrainResource class to store all possible tiles and their edge tiles.
   */
  public class TerrainResource {
    private Tile grassTL, grassTM, grassTR, grassML, grassMM, grassMR, grassBL, grassBM, grassBR;
    private Tile fullSand;
    private ArrayList<Tile> tiles;
    
    // total number of tiles
    public static final int TILE_SIZE = 9;

    public TerrainResource() {
      tiles = new ArrayList<Tile>();
      ResourceService resourceService = ServiceLocator.getResourceService();


      // edge: TOP, RIGHT, BOTTOM, LEFT
      // A: sand, B: grass, C: water
      // =======================
      this.grassTL = new Tile("grassTL", new TextureRegion(resourceService.getAsset("images/top_left_grass.png", Texture.class)),
          new ArrayList<String>(Arrays.asList("AAA", "ABB", "ABB", "AAA")));

      this.grassTM = new Tile("grassTM", new TextureRegion(resourceService.getAsset("images/top_middle_grass.png", Texture.class)),
          new ArrayList<String>(Arrays.asList("AAA", "ABB", "BBB", "ABB")));

      this.grassTR = new Tile("grassTR", new TextureRegion(resourceService.getAsset("images/top_right_grass.png", Texture.class)),
          new ArrayList<String>(Arrays.asList("AAA", "AAA", "BBA", "ABB")));

      this.grassML = new Tile("grassML", new TextureRegion(resourceService.getAsset("images/middle_left_grass.png", Texture.class)),
          new ArrayList<String>(Arrays.asList("ABB", "BBB", "ABB", "AAA")));

      this.grassMM = new Tile("grassMM", new TextureRegion(resourceService.getAsset("images/middle_grass.png", Texture.class)),
          new ArrayList<String>(Arrays.asList("BBB", "BBB", "BBB", "BBB")));

      this.grassMR = new Tile("grassMR", new TextureRegion(resourceService.getAsset("images/middle_right_grass.png", Texture.class)),
          new ArrayList<String>(Arrays.asList("BBA", "AAA", "BBA", "BBB")));

      this.grassBL = new Tile("grassBL", new TextureRegion(resourceService.getAsset("images/lower_left_grass.png", Texture.class)),
          new ArrayList<String>(Arrays.asList("ABB", "BBA", "AAA", "AAA")));

      this.grassBM = new Tile("grassBM", new TextureRegion(resourceService.getAsset("images/lower_middle_grass.png", Texture.class)),
          new ArrayList<String>(Arrays.asList("BBB", "BBA", "AAA", "BBA")));

      this.grassBR = new Tile("grassBR", new TextureRegion(resourceService.getAsset("images/lower_right_grass.png", Texture.class)),
          new ArrayList<String>(Arrays.asList("BBA", "AAA", "AAA", "BBA")));

      tiles.add(this.grassTL);
      tiles.add(this.grassTM);
      tiles.add(this.grassTR);
      tiles.add(this.grassML);
      tiles.add(this.grassMM);
      tiles.add(this.grassMR);
      tiles.add(this.grassBL);
      tiles.add(this.grassBM);
      tiles.add(this.grassBR);

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
     * @param tile The tile to set possible tiles
     */
    public void setPossibleUp(Tile tile) {
      BitSet up = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(2) == tile.getEdgeTiles().get(0)) {
          up.set(i, true);
        }
      }
      tile.up = up;
    }

    /**
     * Set possible tiles for right direction.
     * @param tile The tile to set possible tiles 
     */
    public void setPossibleRight(Tile tile) {
      BitSet right = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(3) == tile.getEdgeTiles().get(1)) {
          right.set(i, true);
        }
      }
      tile.right = right;
    }

    /**
     * Set possible tiles for down direction.
     * @param tile The tile to set possible tiles
     */
    public void setPossibleDown(Tile tile) {
      BitSet down = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(0) == tile.getEdgeTiles().get(2)) {
          down.set(i, true);
        }
      }
      tile.down = down;
    }

    /**
     * Set possible tiles for left direction.
     * @param tile The tile to set possible tiles 
     */
    public void setPossibleLeft(Tile tile) {
      BitSet left = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(1) == tile.getEdgeTiles().get(3)) {
          left.set(i, true);
        }
      }
      tile.left = left;
    }

    /**
     * Get a tile by name.
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
     * @param index The index of the tile
     * @return The tile with the given index
     */
    public Tile getTilebyIndex(int index) {
      return this.tiles.get(index);
    }

    /**
     * Get all tiles.
     * @return All loaded tiles
     */
    public ArrayList<Tile> getAllTiles() {
      return this.tiles;
    }

    /**
     * Get top left grass tile.
     * @return The top left grass tile
     */
    public Tile getGrassTL() {
      return this.grassTL;
    }

    /**
     * Get top middle grass tile.
     * @return The top middle grass tile
     */
    public Tile getGrassTM() {
      return this.grassTM;
    }

    /**
     * Get top right grass tile.
     * @return The top right grass tile
     */
    public Tile getGrassTR() {
      return this.grassTR;
    }

    /**
     * Get middle left grass tile.
     * @return The middle left grass tile
     */
    public Tile getGrassML() {
      return this.grassML;
    }

    /**
     * Get middle middle grass tile.
     * @return The middle middle grass tile
     */
    public Tile getGrassMM() {
      return this.grassMM;
    }

    /**
     * Get middle right grass tile.
     * @return The middle right grass tile
     */
    public Tile getGrassMR() {
      return this.grassMR;
    }

    /**
     * Get bottom left grass tile.
     * @return The bottom left grass tile
     */
    public Tile getGrassBL() {
      return this.grassBL;
    }

    /**
     * Get bottom middle grass tile.
     * @return The bottom middle grass tile
     */
    public Tile getGrassBM() {
      return this.grassBM;
    }

    /**
     * Get bottom right grass tile.
     * @return The bottom right grass tile
     */
    public Tile getGrassBR() {
      return this.grassBR;
    }

    /**
     * Get full sand tile.
     * @return The full sand tile
     */
    public Tile getFullSand() {
      return this.fullSand;
    }

    /**
     * Randomly pick a tile from the list of tiles.
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
     * @return The name of the tile
     */
    public String getName() {
      return name;
    }

    /**
     * Get the texture of the tile.
     * @return The texture of the tile
     */
    public TextureRegion getTexture() {
      return texture;
    }

    /**
     * Get the edge tiles of the tile.
     * @return The edge tiles of the tile
     */
    public ArrayList<String> getEdgeTiles() {
      return edgeTiles;
    }

    /**
     * Get the up edge tiles of the tile.
     * @return The up edge tiles of the tile
     */
    public BitSet getUp() {
      return up;
    }

    /**
     * Get the right edge tiles of the tile.
     * @return The right edge tiles of the tile
     */
    public BitSet getRight() {
      return right;
    }

    /**
     * Get the down edge tiles of the tile.
     * @return The down edge tiles of the tile
     */
    public BitSet getDown() {
      return down;
    }

    /**
     * Get the left edge tiles of the tile.
     * @return The left edge tiles of the tile
     */
    public BitSet getLeft() {
      return left;
    }

  }

}
