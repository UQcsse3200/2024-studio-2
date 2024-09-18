package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.areas.terrain.TerrainFactory.UNDERWATER_MAZE_SIZE;
import static com.csse3200.game.utils.math.GridPoint2Utils.GRID_DIRECTIONS;
import static com.csse3200.game.utils.math.GridPoint2Utils.UP;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class MazeGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(MazeGameArea.class);
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(6, 6);
  private static final float WALL_THICKNESS = 0.1f;
  private static final String[] forestTextures = {
    "images/box_boy_leaf.png",
    "images/tree.png",
    "images/ghost_king.png",
    "images/ghost_1.png",
    "images/minigames/water.png",
    "images/minigames/wall.png"
  };
  private static final String[] forestTextureAtlases = {
    "images/ghost.atlas", "images/minigames/Angler.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  private Maze maze;

  private Entity player;

  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @requires terrainFactory != null
   */
  public MazeGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();

    displayUI();

    maze = new Maze(UNDERWATER_MAZE_SIZE);

    spawnTerrain();
    spawnWalls();
    player = spawnPlayer();
    spawnGhostKing();
    spawnTrees();

    playMusic();
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

  private void spawnTrees() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < 2; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = ObstacleFactory.createTree();
      spawnEntityAt(tree, randomPos, true, false);
    }
  }

  private void spawnWalls() {
    for (int x = 0; x < maze.getWidth(); x++) {
      for (int y = 0; y < maze.getHeight(); y++) {
          for (GridPoint2 direction : GRID_DIRECTIONS) {
              if (maze.isWall(x, y, direction)) {
                  System.out.println(String.valueOf(x) + " " + String.valueOf(y) + " " + direction);
                  float width = 1 + WALL_THICKNESS - Math.abs(direction.x);
                  float height = 1 + WALL_THICKNESS - Math.abs(direction.y);
                  float xPos = x + .5f * (1 + direction.x);
                  float yPos = y + .5f * (1 + direction.y);
                  spawnEntityAt(ObstacleFactory.createMazeWall(width, height), xPos, yPos, true, true);
              }
          }
      }
    }
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.UNDERWATER_MAZE);
    spawnEntity(new Entity().addComponent(terrain));
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    newPlayer.addComponent(terrainFactory.getCameraComponent());
    newPlayer.addComponent(new LightingComponent(LightingComponent.createConeLight(4, 50, Color.CORAL)));
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  private void spawnGhostKing() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity ghostKing = NPCFactory.createAngler(player);
    spawnEntityAt(ghostKing, randomPos, true, true);
  }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}
