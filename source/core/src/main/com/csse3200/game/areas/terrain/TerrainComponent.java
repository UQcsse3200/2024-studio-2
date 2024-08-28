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
  private TiledMap tiledMap;
  private TiledMapRenderer tiledMapRenderer;
  private OrthographicCamera camera;
  private TerrainOrientation orientation;
  private float tileSize;

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

    if (loadedChunks.containsKey(chunkPos))
      return;

    TerrainChunk chunk = new TerrainChunk(chunkPos, tiledMap);

    if ((chunkPos.x < 0 || chunkPos.y < 0) ||
        (chunkPos.x >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth() ||
            chunkPos.y >= ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight()))
      return;

    chunk.generateTiles(chunkPos, loadedChunks, this.terrainResource);
    loadedChunks.put(chunkPos, chunk);
  }

  public void loadChunks(GridPoint2 chunkPos) {

    System.out.println("got some chunks--------------------");
    // top left
    GridPoint2 tl = new GridPoint2(chunkPos.x - 1, chunkPos.y + 1);
    fillChunk(tl);

    // top
    GridPoint2 t = new GridPoint2(chunkPos.x, chunkPos.y + 1);
    fillChunk(t);

    // top right
    GridPoint2 tr = new GridPoint2(chunkPos.x + 1, chunkPos.y + 1);
    fillChunk(tr);

    // left
    GridPoint2 l = new GridPoint2(chunkPos.x - 1, chunkPos.y);
    fillChunk(l);

    // player position
    System.out.println("Player position: " + chunkPos);
    fillChunk(chunkPos);

    // right
    GridPoint2 r = new GridPoint2(chunkPos.x + 1, chunkPos.y);
    fillChunk(r);

    // bottom left
    GridPoint2 bl = new GridPoint2(chunkPos.x - 1, chunkPos.y - 1);
    fillChunk(bl);

    // bottom
    GridPoint2 b = new GridPoint2(chunkPos.x, chunkPos.y - 1);
    fillChunk(b);

    // bottom right
    GridPoint2 br = new GridPoint2(chunkPos.x + 1, chunkPos.y - 1);
    fillChunk(br);
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
    // private TextureRegion grassTL, grassTM, grassTR, grassML, grassMM, grassMR,
    // grassBL, grassBM, grassBR;
    //private TextureRegion fullSand;

    private Tile grassTL, grassTM, grassTR, grassML, grassMM, grassMR, grassBL, grassBM, grassBR;
    private Tile fullSand;

    private ArrayList<Tile> tiles;
    
    // varied
    public static final int TILE_SIZE = 9;

    public TerrainResource() {
      tiles = new ArrayList<Tile>();
      ResourceService resourceService = ServiceLocator.getResourceService();


      System.out.println("Loading assets----------------------------------");

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
      


      // this.grassTL = new
      // TextureRegion(resourceService.getAsset("images/top_left_grass.png",
      // Texture.class));
      // this.grassTM = new
      // TextureRegion(resourceService.getAsset("images/top_middle_grass.png",
      // Texture.class));
      // this.grassTR = new
      // TextureRegion(resourceService.getAsset("images/top_right_grass.png",
      // Texture.class));
      // this.grassML = new
      // TextureRegion(resourceService.getAsset("images/middle_left_grass.png",
      // Texture.class));
      // this.grassMM = new
      // TextureRegion(resourceService.getAsset("images/middle_grass.png",
      // Texture.class));
      // this.grassMR = new
      // TextureRegion(resourceService.getAsset("images/middle_right_grass.png",
      // Texture.class));
      // this.grassBL = new
      // TextureRegion(resourceService.getAsset("images/lower_left_grass.png",
      // Texture.class));
      // this.grassBM = new
      // TextureRegion(resourceService.getAsset("images/lower_middle_grass.png",
      // Texture.class));
      // this.grassBR = new
      // TextureRegion(resourceService.getAsset("images/lower_right_grass.png",
      // Texture.class));
      // this.fullSand = new
      // TextureRegion(resourceService.getAsset("images/full_sand_tile.png",
      // Texture.class));
    }

    public void setPossibleTiles() {
      for (int i = 0; i < this.tiles.size(); i++) {
        setPossibleUp(this.tiles.get(i));
        setPossibleRight(this.tiles.get(i));
        setPossibleDown(this.tiles.get(i));
        setPossibleLeft(this.tiles.get(i));
      }
    }

    public void setPossibleUp(Tile tile) {
      BitSet up = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(2) == tile.getEdgeTiles().get(0)) {
          up.set(i, true);
        }
      }
      tile.up = up;
    }

    public void setPossibleRight(Tile tile) {
      BitSet right = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(3) == tile.getEdgeTiles().get(1)) {
          right.set(i, true);
        }
      }
      tile.right = right;
    }

    public void setPossibleDown(Tile tile) {
      BitSet down = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(0) == tile.getEdgeTiles().get(2)) {
          down.set(i, true);
        }
      }
      tile.down = down;
    }

    public void setPossibleLeft(Tile tile) {
      BitSet left = new BitSet(TILE_SIZE);
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).getEdgeTiles().get(1) == tile.getEdgeTiles().get(3)) {
          left.set(i, true);
        }
      }
      tile.left = left;
    }

    public Tile getTilebyName(String name) {
      for (int i = 0; i < this.tiles.size(); i++) {
        if (this.tiles.get(i).name == name) {
          return this.tiles.get(i);
        }
      }
      return null;
    }

    public Tile getTilebyIndex(int index) {
      return this.tiles.get(index);
    }

    public ArrayList<Tile> getAllTiles() {
      return this.tiles;
    }

    public Tile getGrassTL() {
      return this.grassTL;
    }

    public Tile getGrassTM() {
      return this.grassTM;
    }

    public Tile getGrassTR() {
      return this.grassTR;
    }

    public Tile getGrassML() {
      return this.grassML;
    }

    public Tile getGrassMM() {
      return this.grassMM;
    }

    public Tile getGrassMR() {
      return this.grassMR;
    }

    public Tile getGrassBL() {
      return this.grassBL;
    }

    public Tile getGrassBM() {
      return this.grassBM;
    }

    public Tile getGrassBR() {
      return this.grassBR;
    }

    public Tile getFullSand() {
      return this.fullSand;
    }

    public Tile random_pick() {
      return this.tiles.get((int) (Math.random() * this.tiles.size()));
    }
  }

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

    public String getName() {
      return name;
    }

    public TextureRegion getTexture() {
      return texture;
    }

    public ArrayList<String> getEdgeTiles() {
      return edgeTiles;
    }

    public BitSet getUp() {
      return up;
    }

    public BitSet getRight() {
      return right;
    }

    public BitSet getDown() {
      return down;
    }

    public BitSet getLeft() {
      return left;
    }

  }

}
