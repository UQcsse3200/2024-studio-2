package com.csse3200.game.areas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.GdxGame;

public class MapHandler {
  private static GameArea currentGameArea;
  private static MapType currentMap = MapType.NONE;
  private static MapType previousMap = MapType.NONE;

  private static ForestGameArea forestGameArea;

  private static boolean isSavedPrevioud;
  // private static GameArea savedPrevioud;

  private MapHandler() {
    isSavedPrevioud = false;
  }

  /**
   * Switch to a NEW map
   *
   * Set saveState to true will remember the previous map state, which can
   * be restored later.
   *
   * @param mapType map type
   * @param renderer renderer
   * @param game game
   * @param saveState save state
   * @return 
   */
  public static GameArea switchMapTo(MapType mapType, Renderer renderer, GdxGame game, boolean saveState) {
    // TODO: save state
    if (saveState && currentMap != MapType.NONE) {
      // currentMap.saveState();
      isSavedPrevioud = true;
    }

    if (currentMap != MapType.NONE) {
      getCurrentMap().dispose();
    }

    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());

    if (mapType == MapType.FOREST) {
      currentGameArea = new ForestGameArea(terrainFactory, game);
      currentGameArea.create();
    } 

    previousMap = currentMap;
    currentMap = mapType;
    return currentGameArea;
  }

  /**
   * Get current map
   *
   * @return current map
   */
  @SuppressWarnings("unchecked")
  public static <T extends GameArea> T getCurrentMap() {
    return (T) getMap(currentMap);
  }

  /**
   * Get map by type
   *
   * @param mapType map type
   *
   * @return map
   */
  public static GameArea getMap(MapType mapType) {
    switch (mapType) {
      case FOREST:
        return (ForestGameArea) currentGameArea;
      default:
        throw new IllegalArgumentException("Map type not supported: " + mapType);
    }
  }

  /**
   * Map types
   */
  public enum MapType {
    FOREST, WATER, AIR, COMBAT, NONE
  }
}
