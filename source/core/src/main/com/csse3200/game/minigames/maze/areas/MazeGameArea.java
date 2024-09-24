package com.csse3200.game.minigames.maze.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.minigames.maze.areas.terrain.MazeTerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.Maze;
import com.csse3200.game.minigames.maze.components.gamearea.MazeGameAreaDisplay;
import com.csse3200.game.minigames.maze.components.tasks.MazeHuntTask;
import com.csse3200.game.minigames.maze.entities.factories.MazeNPCFactory;
import com.csse3200.game.minigames.maze.entities.factories.MazeObstacleFactory;
import com.csse3200.game.minigames.maze.entities.factories.MazePlayerFactory;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import static com.csse3200.game.utils.math.GridPoint2Utils.GRID_DIRECTIONS;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class MazeGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(MazeGameArea.class);
  public static final float WALL_THICKNESS = 0.1f;
  public static final int NUM_WALL_BREAKS = 10;
  private static final String[] forestTextures = {
    "images/box_boy_leaf.png",
    "images/tree.png",
    "images/ghost_king.png",
    "images/ghost_1.png",
    "images/minigames/water.png",
    "images/minigames/wall.png"
  };
  private static final String[] forestTextureAtlases = {
    "images/minigames/Angler.atlas","images/minigames/fish.atlas",
          "images/minigames/Jellyfish.atlas", "images/minigames/eels.atlas"

  };
  private static final String[] forestSounds = {"sounds/minigames/angler-chomp.mp3"};
  private static final String backgroundMusic = "sounds/minigames/maze-bg.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final MazeTerrainFactory terrainFactory;

  private Maze maze;

  private Entity player;

  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @requires terrainFactory != null
   */
  public MazeGameArea(MazeTerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();

    displayUI();

    maze = new Maze(MazeTerrainFactory.MAP_SIZE);
    maze.breakWalls(NUM_WALL_BREAKS);

    spawnTerrain();
    spawnWalls();
    player = spawnPlayer();
    spawnAngler(1);
    spawnJellyfish(20);

    playMusic();
  }

  private GridPoint2 getSimpleStartLocation() {
    while (true) {
      GridPoint2 start = maze.getRandomCell();
      if (new Vector2(start.x, start.y).dst(player.getCenterPosition()) > 1f) {
        return start;
      }
    }
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new MazeGameAreaDisplay("Underwater Maze"));
    spawnEntity(ui);
  }

  private void spawnWalls() {
    for (int x = 0; x < maze.getWidth(); x++) {
      for (int y = 0; y < maze.getHeight(); y++) {
        for (GridPoint2 direction : GRID_DIRECTIONS) {
          if (maze.isWall(x, y, direction)) {
            float width = 1 + WALL_THICKNESS - Math.abs(direction.x);
            float height = 1 + WALL_THICKNESS - Math.abs(direction.y);
            float xPos = x + .5f * (1 + direction.x);
            float yPos = y + .5f * (1 + direction.y);
            spawnEntityCenteredAt(MazeObstacleFactory.createMazeWall(width, height), new Vector2(xPos, yPos));
          }
        }
      }
    }
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain();
    spawnEntity(new Entity().addComponent(terrain));
  }

  private Entity spawnPlayer() {
    Entity newPlayer = MazePlayerFactory.createPlayer();
    newPlayer.addComponent(terrainFactory.getCameraComponent());
    spawnEntityAt(newPlayer, maze.getNextStartLocation(), true, true);
    newPlayer.getEvents().trigger("wanderStart");
    return newPlayer;
  }

  /**
   * Spawns in the angler npc.
   * @param number The number of angler to be spawned in
   */
  private void spawnAngler(int number) {
    for (int i = 0; i < number; i++) {
      Entity angler = MazeNPCFactory.createAngler(player);
      spawnEntityAt(angler, maze.getNextStartLocation(), true, true);
      angler.getComponent(AITaskComponent.class).addTask(
              new MazeHuntTask(player, maze, 2));
    }
  }

  /**
   * Spawns in the jellyfish npc. Jellyfish wander around, and do not actively seek
   * the player.
   * @param number The number of jellyfish to be spawned in
   */
  private void spawnJellyfish(int number) {
    for (int i = 0; i < number; i++) {
      Entity jellyfish = MazeNPCFactory.createJellyfish();
      spawnEntityAt(jellyfish, getSimpleStartLocation(), true, true);
    }
  }

  @Override
  public void playMusic() {
    AudioManager.playMusic("sounds/minigames/maze-bg.mp3", true);
    AudioManager.setMusicVolume(AudioManager.getDesiredMusicVolume() / 2);
  }

  public void pauseMusic() {
    AudioManager.stopMusic();  // Stop the music
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

  @Override
  public void unloadAssets() {
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

  /**
   * gets the player
   * @return player entity
   */
  @Override
  public Entity getPlayer () {
    return player;
  }

  public List<Entity> getEnemies() {
    return null;
  }
}
