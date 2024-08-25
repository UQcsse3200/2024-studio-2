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

import java.util.HashMap;
import java.util.Map;

/**
 * Render a tiled terrain for a given tiled map and orientation. A terrain is a map of tiles that
 * shows the 'ground' in the game. Enabling/disabling this component will show/hide the terrain.
 */
public class TerrainComponent extends RenderComponent {
  public static final int CHUNK_SIZE = 16;

  private static final int TERRAIN_LAYER = 0;
  private TiledMap tiledMap;
  private TiledMapRenderer tiledMapRenderer;
  private OrthographicCamera camera;
  private TerrainOrientation orientation;
  private float tileSize;

  private TextureRegion grass, grassTuft, rocks;
  private Map<GridPoint2, TiledMapTileLayer> loadedChunks = new HashMap<>();

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

    ResourceService resourceService = ServiceLocator.getResourceService();

    // get asset
    this.grass = new TextureRegion(resourceService.getAsset("images/gt.png", Texture.class));
    this.grassTuft = new TextureRegion(resourceService.getAsset("images/grass_2.png", Texture.class));
    this.rocks = new TextureRegion(resourceService.getAsset("images/grass_3.png", Texture.class));

    TerrainTile grassTuftTile = new TerrainTile(grassTuft);
    TerrainTile rockTile = new TerrainTile(rocks);
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

  public void loadChunk(GridPoint2 position) {

  }

  public void fillChunk(GridPoint2 position) {
    TerrainTile grassTile = new TerrainTile(grass);
    System.out.println("got some chunks");

    for (int x = 0; x < CHUNK_SIZE; x++) {
      for (int y = 0; y < CHUNK_SIZE; y++) {
        Cell cell = new Cell();
        cell.setTile(grassTile);
        ((TiledMapTileLayer)tiledMap.getLayers().get("forest")).setCell(x, y, cell);
      } 
    }
  }

  public float getTileSize() {
    return tileSize;
  }

  public GridPoint2 getMapBounds(int layer) {
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer)tiledMap.getLayers().get(layer);
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
}
