package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.CombatTerrainFactory;
import com.csse3200.game.areas.terrain.CombatTerrainFactory.TerrainType;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class CombatArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(CombatGameArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(9, 15);
    private static final GridPoint2 ENEMY_COMBAT_SPAWN = new GridPoint2(22, 15);

    private static final float WALL_WIDTH = 0.1f;
    private static final String[] forestTextures = {
            "images/box_boy_leaf.png",
            "images/tree.png",
            "images/ghost_king.png",
            "images/final_boss_kangaroo_idle.png",
            "images/Cow.png",
            "images/snake.png",
            "images/eagle.png",
            "images/lion.png",
            "images/turtle.png",
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
            "images/gt.png",
            "images/top_left_grass.png",
            "images/top_middle_grass.png",
            "images/top_right_grass.png",
            "images/middle_left_grass.png",
            "images/middle_grass.png",
            "images/middle_right_grass.png",
            "images/lower_left_grass.png",
            "images/lower_middle_grass.png",
            "images/lower_right_grass.png",
            "images/full_sand_tile.png",
            "images/dog.png",
            "images/croc.png",
            "images/bird.png",
            "images/Healthpotion.png",
            "images/foodtextures/apple.png",
    };
    private static final String[] forestTextureAtlases = {
            "images/terrain_iso_grass.atlas", "images/chicken.atlas", "images/frog.atlas",
            "images/monkey.atlas", "images/Cow.atlas", "images/snake.atlas", "images/lion.atlas",
            "images/eagle.atlas", "images/turtle.atlas", "images/final_boss_kangaroo.atlas"
    };
    private static final String[] questSounds = {"sounds/QuestComplete.wav"};
    private static final String[] forestSounds = {"sounds/Impact4.ogg"};
    private static final String heartbeat = "sounds/heartbeat.mp3";
    private static final String[] heartbeatSound = {heartbeat};
    private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
    private static final String[] forestMusic = {backgroundMusic};
    private final CombatTerrainFactory combatTerrainFactory;
    private Entity player;
    private Entity enemy;

    /**
     * Initialise this ForestGameArea to use the provided CombatTerrainFactory and the enemy which player
     * has engaged combat with.
     *
     * @param combatTerrainFactory CombatTerrainFactory used to create the terrain for the GameArea.
     * @requires terrainFactory != null
     */
    // I believe a variable Entity combatEnemyNPC can be passed to this func which sets the current enemy.
    // Then this enemy can be spawned within this class in some function spawn_enemy()
    public CombatArea(CombatTerrainFactory combatTerrainFactory, Entity player, Entity enemy) {
        super();
        this.combatTerrainFactory = combatTerrainFactory;
        //this.player = player;
        //this.enemy = enemy;
    }

    /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
    @Override
    public void create() {
        loadAssets();
        displayUI();
        spawnTerrain();
        spawnPlayer();
        spawnCombatEnemy();
        playMusic();
    }


    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Box Forest"));
        spawnEntity(ui);
    }

    private void spawnTerrain() {
        // Background terrain
        terrain = combatTerrainFactory.createTerrain(TerrainType.FOREST_DEMO);
        spawnEntity(new Entity().addComponent(terrain));

    }

    /** Spawn a player for testing purposes. Currently, this player can be moved */
    private void spawnPlayer() {

    }

    /** Spawn a combat enemy. Different to a regular enemy npc */
    private void spawnCombatEnemy() {
        Entity combatEnemyNPC = NPCFactory.createKangaBossCombatEntity();
        // Create in the world
        spawnEntityAt(combatEnemyNPC, ENEMY_COMBAT_SPAWN, true, true);
        //return combatEnemyNPC;
        //spawnEntityAt(enemy, ENEMY_COMBAT_SPAWN, true, true);
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