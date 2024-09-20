package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.areas.MapHandler.MapType;
import com.csse3200.game.components.CameraComponent;

import java.util.HashMap;
import java.util.Map;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private GridPoint2 mapSize;

  public static final int CHUNK_SIZE = 16;
  private static final int TUFT_TILE_COUNT = 1;
  private static final int ROCK_TILE_COUNT = 1;

  private final OrthographicCamera camera;
  private final CameraComponent cameraComponent;
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
    this.cameraComponent = cameraComponent;
    this.orientation = orientation;
  }

  /**
   * Retrieve the component to which the camera is attached
   *
   * @return the camera component
   */
  public CameraComponent getCameraComponent() {
    return this.cameraComponent;
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory. This can be extended
   * to add additional game terrains.
   *
   * @param terrainType Terrain to create
   * @param playerPosition The current position of the player in the world
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TerrainType terrainType, GridPoint2 playerPosition, GridPoint2 mapSize, MapType mapType) {
    this.mapSize = mapSize;
    float tileWorldSize = 1.f;

    TiledMap tiledMap = new TiledMap();
    TiledMapTileLayer layer = new TiledMapTileLayer(this.mapSize.x, this.mapSize.y, 1000, 1000);
    tiledMap.getLayers().add(layer);

    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / 1000);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize, mapType);
  }


  private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
      return switch (orientation) {
          case ORTHOGONAL -> new OrthogonalTiledMapRenderer(tiledMap, tileScale);
          case ISOMETRIC -> new IsometricTiledMapRenderer(tiledMap, tileScale);
          case HEXAGONAL -> new HexagonalTiledMapRenderer(tiledMap, tileScale);
          default -> null;
      };
  }

  public enum TerrainType {
    FOREST_DEMO,
    FOREST_DEMO_ISO,
    FOREST_DEMO_HEX;
      public static TerrainType Ocean_DEMO;
  }
}
