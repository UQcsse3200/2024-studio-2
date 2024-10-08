package com.csse3200.game.areas;

import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.forest.ForestGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.rendering.Renderer;

public class MapHandler {
  private static GameArea currentGameArea;
  private static MapType currentMap = MapType.NONE;
  private static MapType previousMap = MapType.NONE;
  private static boolean unlockedWater = false;
  private static boolean unlockedAir = false;
  private static int bossDefeat = 0;

  private static ForestGameArea forestGameArea;

  /**
   *
   */
  private MapHandler() {
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
   * Generates a new map - intended for use when a game is loaded, ie, so that
   * the new map can be set without attempting to dispose of an old map
   * (this should be handled by the screen).
   * NOTE: Erases reference to old map without disposing of it.
   *
   * @param mapType - the type of map to initiaise to
   * @param renderer renderer
   * @param game game
   * @return
   */
  public static GameArea createNewMap(MapType mapType, Renderer renderer, GdxGame game) {
    resetMapHandler();
    currentMap = mapType;

    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());

    if (mapType == MapType.FOREST) {
      currentGameArea = new ForestGameArea(terrainFactory, game);
      currentGameArea.create();
    }

    return currentGameArea;
  }

  /**
   * Unlock the next area.
   * Water is unlocked first, then air.
   */
  public static void unlockNextArea() {
    if (unlockedWater) {
      unlockedAir = true;
      currentGameArea.unlockArea("Air");
    }
    unlockedWater = true;
    currentGameArea.unlockArea("Water");
  }

  /**
   * checks if the water map is unlcked yet
   * @return true iff the map is unlocked
   */
  public static boolean getUnlockedOcean() {
    return MapHandler.unlockedWater;
  }

  /**
   * Updates the count of bosses defeated
   */
  public static void updateBossDefeatCount() {
    MapHandler.bossDefeat += 1;
  }

  /**
   * sets the state of unlocked water map
   * @param unlockedWater the state of unlocked map
   */
  public static void setUnlockedWater(boolean unlockedWater) {
    MapHandler.unlockedWater = unlockedWater;
  }

  /**
   * Deletes references to all maps and resets to original state.
   */
  public static void resetMapHandler() {
    currentMap = MapType.NONE;
    previousMap = MapType.NONE;
    currentGameArea = null;
    forestGameArea = null;
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
        return currentGameArea;
      default:
        throw new IllegalArgumentException("Map type not supported: " + mapType);
    }
  }

  /**
   * Map types
   */
  public enum MapType {
    FOREST, WATER, AIR, COMBAT, FOG, MAZE_MINIGAME, NONE
  }
}
