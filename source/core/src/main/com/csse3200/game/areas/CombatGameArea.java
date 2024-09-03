package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
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

/** Forest area for the demo game with trees, a player, and some enemies. */
public class CombatGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(CombatGameArea.class);
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 12);
  private static final GridPoint2 ENEMY_COMBAT_SPAWN = new GridPoint2(22, 13);
  private static final GridPoint2 MAP_SIZE = new GridPoint2(5000, 5000);

  private static final float WALL_WIDTH = 0.1f;
  private static final String[] combatAreaTextures = {
    "images/box_boy_leaf.png",
    "images/tree.png",
    "images/final_boss_kangaroo_idle.png",
    "images/ghost_king.png",
    "images/ghost_1.png",
    "images/grass_1.png",
    "images/grass_2.png",
    "images/grass_3.png",
    "images/hex_grass_1.png",
    "images/hex_grass_2.png",
    "images/hex_grass_3.png",
    "images/iso_grass_1.png",
    "images/iso_grass_2.png",
    "images/iso_grass_3.png"
  };
  private static final String[] combatAreaTextureAtlases = {
    "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  private Entity player;

  private final GdxGame game;

  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory and the enemy which player
   * has engaged combat with.
   *
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @requires terrainFactory != null
   */
  // I believe a variable Entity combatEnemyNPC can be passed to this func which sets the current enemy.
  // Then this enemy can be spawned within this class in some function spawn_enemy()
  public CombatGameArea(TerrainFactory terrainFactory, GdxGame game) {
    super();
    this.terrainFactory = terrainFactory;
    this.game = game;
    //this.enemyNPC = enemyNPC;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();
    displayUI();

    // spawnTerrain();
    player = spawnPlayer();
    spawnCombatEnemy();

    playMusic();
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

//  private void spawnTerrain() {
//    // Background terrain
//    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO, PLAYER_SPAWN, MAP_SIZE);
//    spawnEntity(new Entity().addComponent(terrain));
//
//    // Terrain walls
//    float tileSize = terrain.getTileSize();
//    GridPoint2 tileBounds = terrain.getMapBounds(0);
//    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);
//
//    // Left
//    spawnEntityAt(
//        ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
//    // Right
//    spawnEntityAt(
//        ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
//        new GridPoint2(tileBounds.x, 0),
//        false,
//        false);
//    // Top
//    spawnEntityAt(
//        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
//        new GridPoint2(0, tileBounds.y),
//        false,
//        false);
//    // Bottom
//    spawnEntityAt(
//        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);
//  }

//  /** Spawn a player for testing purposes. Currently, this player can be moved */
  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer(game);
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  /** Spawn a combat enemy. Different to a regular enemy npc */
  private void spawnCombatEnemy() {
    // Create entity
    // for now, I have just manually initialised a boss Entity see CombatGameArea() for my
    // planned functionality -- callumR
    Entity combatEnemyNPC = NPCFactory.createKangaBossCombatEntity();
    // Create in the world
    spawnEntityAt(combatEnemyNPC, ENEMY_COMBAT_SPAWN, true, true);
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
    resourceService.loadTextures(combatAreaTextures);
    resourceService.loadTextureAtlases(combatAreaTextureAtlases);
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
    resourceService.unloadAssets(combatAreaTextures);
    resourceService.unloadAssets(combatAreaTextureAtlases);
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
