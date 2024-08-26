package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
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
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(9, 15);
  private static final GridPoint2 ENEMY_COMBAT_SPAWN = new GridPoint2(22, 15);

  private static final float WALL_WIDTH = 0.1f;
  private static final String[] forestTextures = {
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
    "images/iso_grass_3.png",
    "images/combat_base.png",
  };
  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas", "images/combatBase.atlas"
  };
  private static final String[] combatBaseTexture = {
    "images/combat_base.png"
  };
  private static final String[] combatTextureAtlas = {
    "images/combatBase.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};
  private final TerrainFactory terrainFactory;
  private Entity player;


  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory and the enemy which player
   * has engaged combat with.
   *
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @requires terrainFactory != null
   */
  // I believe a variable Entity combatEnemyNPC can be passed to this func which sets the current enemy.
  // Then this enemy can be spawned within this class in some function spawn_enemy()
  public CombatGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
    //this.enemyNPC = enemyNPC;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();
    displayUI();
    spawnTerrain();

    player = spawnPlayer();
    spawnCombatEnemy();
    spawnBase(); // spawn combat base
    playMusic();
  }


  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

  // spawns a combat base
  private void spawnBase() {
    GridPoint2 pos1 = new GridPoint2(20, 16);
    Entity base1 = ObstacleFactory.createCombatBase();
    spawnEntityAt(base1, pos1, true, false);

    GridPoint2 pos2 = new GridPoint2(9, 6);
    Entity base2 = ObstacleFactory.createCombatBase();
    spawnEntityAt(base2, pos2, true, false);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO);
    spawnEntity(new Entity().addComponent(terrain));

    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    // Left
    spawnEntityAt(
        ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
    // Right
    spawnEntityAt(
        ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
        new GridPoint2(tileBounds.x, 0),
        false,
        false);
    // Top
    spawnEntityAt(
        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
        new GridPoint2(0, tileBounds.y),
        false,
        false);
    // Bottom
    spawnEntityAt(
        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);

    // spawnCombatBases();
  }

  private void spawnCombatBases() {
    // Define positions for combat bases
    GridPoint2[] combatBasePositions = {
            new GridPoint2(20, 16),  // Example positions
            new GridPoint2(9, 6),
            // Add more positions as needed
    };

    // Create and spawn combat bases
    for (GridPoint2 position : combatBasePositions) {
      Entity combatBase = ObstacleFactory.createCombatBase();
      // Adjust position as needed
      spawnEntityAt(combatBase, position, false, false);
    }
  }

  /** Spawn a player for testing purposes. Currently, this player can be moved */
  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
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
    resourceService.loadTextureAtlases(combatTextureAtlas);
    resourceService.loadTextures(combatBaseTexture);
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }

    /*
    TextureAtlas combatAtlas = ServiceLocator.getResourceService().getAsset("images/combatBase.atlas", TextureAtlas.class);
    if (combatAtlas == null) {
      logger.error("Combat texture atlas failed to load.");
    } else {
      TextureRegion combatBaseTextureRegion = combatAtlas.findRegion("combat_base"); // Check if name matches
      if (combatBaseTextureRegion == null) {
        logger.error("Combat base texture region not found in atlas.");
      }
    }
     */
 }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(combatBaseTexture);
    resourceService.unloadAssets(combatTextureAtlas);
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
