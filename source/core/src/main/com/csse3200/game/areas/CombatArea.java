package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.CombatTerrainFactory;
import com.csse3200.game.areas.terrain.CombatTerrainFactory.TerrainType;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.ProximityComponent;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class CombatArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(CombatGameArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(200,  130); // 9, 14...384, 256
    private static final GridPoint2 ENEMY_COMBAT_SPAWN = new GridPoint2(480, 200); // 20, 20

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
            "images/combat_background_one.png",
            "images/chicken_idle.png",
            "images/monkey_idle.png",
            "images/frog_idle.png",
            "images/dog.png",
            "images/croc.png",
            "images/bird.png",
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
    private CombatTerrainFactory combatTerrainFactory;
    private Entity player;
    private Entity enemy;
    private static final GridPoint2 MAP_SIZE = new GridPoint2(768, 512);
    private static GdxGame game;


    /**
     * Initialise this ForestGameArea to use the provided CombatTerrainFactory and the enemy which player
     * has engaged combat with.
     *
     * @param combatTerrainFactory CombatTerrainFactory used to create the terrain for the GameArea.
     * @requires terrainFactory != null
     */
    // I believe a variable Entity combatEnemyNPC can be passed to this func which sets the current enemy.
    // Then this enemy can be spawned within this class in some function spawn_enemy()
    public CombatArea(CombatTerrainFactory combatTerrainFactory, Entity player, Entity enemy, GdxGame game, CombatTerrainFactory terrainFactory) {
        super();
        this.combatTerrainFactory = combatTerrainFactory;
        this.game = game;
        this.combatTerrainFactory = terrainFactory;
        this.player = player;
        this.enemy = enemy;
    }

    /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
    @Override
    public void create() {
        loadAssets();
        displayUI();
        spawnTerrain();
        spawnPlayer();
        if(enemy.getEnemyType() == Entity.EnemyType.MONKEY) { // get the enemy type player collided into for combat and spawn that
            spawnMonkey();
        } else if (enemy.getEnemyType() == Entity.EnemyType.FROG) {
            spawnFrog();
        } else if (enemy.getEnemyType() == Entity.EnemyType.CHICKEN) {
            spawnChicken();
        } else { // Kangaroo Boss
            spawnCombatEnemy();
        }
        playMusic();
    }


    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Box Forest"));
        spawnEntity(ui);
    }

    private void spawnTerrain() {
        terrain = combatTerrainFactory.createBackgroundTerrain2(TerrainType.FOREST_DEMO, PLAYER_SPAWN, MAP_SIZE);
        // spawnEntity(new Entity().addComponent(terrain));
        Entity t = new Entity();
        // t.addComponent(combatTerrainFactory.getCameraComponent());
        spawnEntityAt((t.addComponent(terrain)), new GridPoint2(-10, 0), true, true);
        // spawnEntityAt((new Entity().addComponent(terrain)), new GridPoint2(-10, 0), true, true);
    }

    /** Spawn a static player entity as an NPC for static combat
     *  the player has a health component.
     *  The combat logic can use the health component of this NPC player by setting
     *  this.player to be newPlayer
     *  or continue using the health components of the player loaded in through the constructor
     */
    private void spawnPlayer() {

        /** Entity nP is a non-visible entity placed at the centre of the background to
         * ensure the camera component stays stagnant in the centre of the combat background.
         * The entity serves no other purpose and is not visible
         */
        String iP = AnimalSelectionActions.getSelectedAnimalImagePath();
        Entity nP = NPCFactory.createCombatPlayer(iP);
        nP.addComponent(combatTerrainFactory.getCameraComponent());
        nP.setPosition(340, 230);

        /**
         * The following entity is the real entity of the player to be used for combat,
         * with health, stats, etc.
         */
        String imagePath = AnimalSelectionActions.getSelectedAnimalImagePath();
        Entity newPlayer = NPCFactory.createCombatPlayer(imagePath);
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    }

    /** Spawn a combat enemy. Different to a regular enemy npc */
    // Eventually pass a variable to determine which enemy needs to be spawned
    private void spawnCombatEnemy() {
        Entity combatEnemyNPC = NPCFactory.createKangaBossCombatEntity();
        spawnEntityAt(combatEnemyNPC, ENEMY_COMBAT_SPAWN, true, true);
    }

    // The following functions spawn chicken, monkey, and frog entities as NPC's for static combat
    private void spawnChicken() {
        Entity combatEnemyNPC = NPCFactory.createChickenCombatEnemy();
        spawnEntityAt(combatEnemyNPC, ENEMY_COMBAT_SPAWN, true, true);
    }
    /**
     * spawns a frog enemy, with the player entity as its target
     */
    private void spawnFrog() {
        Entity combatEnemyNPC = NPCFactory.createFrogCombatEnemy();
        spawnEntityAt(combatEnemyNPC, ENEMY_COMBAT_SPAWN, true, true);
    }

    /**
     * spawns a monkey enemy, with the player entity as its target
     */
    private void spawnMonkey() {
        Entity combatEnemyNPC = NPCFactory.createMonkeyCombatEnemy();
        spawnEntityAt(combatEnemyNPC, ENEMY_COMBAT_SPAWN, true, true);
    }

    private void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }
    public void pauseMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.pause();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(forestTextures);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadSounds(forestSounds);
        resourceService.loadMusic(forestMusic);
        resourceService.loadSounds(questSounds);

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
        resourceService.unloadAssets(questSounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }
}