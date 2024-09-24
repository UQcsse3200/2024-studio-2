package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.combat.CombatExitDisplay;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.components.settingsmenu.UserSettings;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.areas.terrain.CombatTerrainFactory;
import com.csse3200.game.areas.terrain.CombatTerrainFactory.TerrainType;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class CombatArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(CombatArea.class);
    private static GridPoint2 PLAYER_SPAWN = new GridPoint2(290,  335); // 9, 14...384, 256

    private static final String[] combatTexture = {
            "images/box_boy_leaf.png",
            "images/tree.png",
            "images/ghost_king.png",
            "images/final_boss_kangaroo_idle.png",
            "images/Cow.png",
            "images/snake.png",
            "images/eagle.png",
            "images/lion.png",
            "images/turtle.png",
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
            "images/combat_background.png",
            "images/chicken_idle.png",
            "images/bear_idle.png",
            "images/eel_idle.png",
            "images/pigeon_idle.png",
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
    private static final GridPoint2 MAP_SIZE = new GridPoint2(1030, 590);
    private static GdxGame game;


    /**
     * Initialise this ForestGameArea to use the provided CombatTerrainFactory and the enemy which player
     * has engaged combat with.
     *
     * @param terrainFactory CombatTerrainFactory used to create the terrain for the GameArea.
     * @requires terrainFactory != null
     */
    // I believe a variable Entity combatEnemyNPC can be passed to this func which sets the current enemy.
    // Then this enemy can be spawned within this class in some function spawn_enemy()
    public CombatArea(Entity player, Entity enemy, GdxGame game, CombatTerrainFactory terrainFactory) {
        super();
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
        } else if (enemy.getEnemyType() == Entity.EnemyType.BEAR) {
            spawnBear();
        } else if (enemy.getEnemyType() == Entity.EnemyType.PIGEON) {
            spawnPigeon();
        } else if (enemy.getEnemyType() == Entity.EnemyType.EEL) {
            spawnEel();
        } else { // Kangaroo Boss
            spawnCombatEnemy();
        }
        playMusic();
    }

    /** Adds all UI components to the combat area */
    private void displayUI() {
        Entity ui = new Entity();
        spawnEntity(ui);
    }

    /** Spawns the official backgrond terrain for
     * combat using combat terrain factory
     */
    private void spawnTerrain() {
        terrain = combatTerrainFactory.createBackgroundTerrain2(TerrainType.FOREST_DEMO, PLAYER_SPAWN, MAP_SIZE);
        Entity terrainEntity = new Entity();
        spawnEntityAt((terrainEntity.addComponent(terrain)), new GridPoint2(-10, 0), true, true);
    }

    /** Spawn a static player entity as an NPC for static combat
     *  the player has a health component.
     *  The combat logic can use the health component of this NPC player by setting
     *  this.player to be newPlayer
     *  or continue using the health components of the player loaded in through the constructor
     */
    private void spawnPlayer() {
        spawnCameraInvisibleEnity(); // Create invisible entity to centre camera at centre of background

        /**
         * The following entity is the real entity of the player to be used for combat,
         * with health, stats, etc.
         */
        String imagePath = AnimalSelectionActions.getSelectedAnimalImagePath();
        Entity newPlayer = PlayerFactory.createCombatPlayer(imagePath);
        if (imagePath == "images/croc.png"){
            PLAYER_SPAWN = new GridPoint2(332, 335);
        } else if (imagePath == "images/dog.png"){
            PLAYER_SPAWN = new GridPoint2(337, 330);
        } else { //animal is bird
            PLAYER_SPAWN = new GridPoint2(350, 335);
            newPlayer.scaleHeight(150);
        }
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    }

    /** Spawns an invisible entity to set the camera at the centre of the screen without having to
     * attach the camera to the real player entity. This entity should be ignored and it's health/stats etc.
     * are NOT to be used for combat logic or anywehre else
     */
    private void spawnCameraInvisibleEnity(){
        /** Entity nP is a non-visible entity placed at the centre of the background to
         * ensure the camera component stays stagnant in the centre of the combat background.
         * The entity serves no other purpose and is not visible
         */
        String iP = AnimalSelectionActions.getSelectedAnimalImagePath();
        Entity nP = PlayerFactory.createCombatPlayer(iP);
        nP.addComponent(combatTerrainFactory.getCameraComponent());
        nP.setPosition(520, 250);
    }

    /** Spawn a combat enemy. Different to a regular enemy npc */
    private void spawnCombatEnemy() {
        Entity combatEnemyNPC = EnemyFactory.createKangaBossCombatEntity();
        spawnEntityAt(combatEnemyNPC, new GridPoint2(800, 346), true, true);
    }

    /** The following functions spawn chicken, monkey, and frog entities as NPC's for static combat
     */
    private void spawnChicken() {
        Entity combatEnemyNPC = EnemyFactory.createChickenCombatEnemy();
        spawnEntityAt(combatEnemyNPC, new GridPoint2(800, 328), true, true);
    }
    /**
     * spawns a frog enemy, with the player entity as its target
     */
    private void spawnFrog() {
        Entity combatEnemyNPC = EnemyFactory.createFrogCombatEnemy();
        spawnEntityAt(combatEnemyNPC, new GridPoint2(800, 311), true, true);
    }

    /**
     * spawns a monkey enemy, with the player entity as its target
     */
    private void spawnMonkey() {
        Entity combatEnemyNPC = EnemyFactory.createMonkeyCombatEnemy();
        spawnEntityAt(combatEnemyNPC, new GridPoint2(796, 331), true, true);
    }

    /**
     * spawns a bear enemy, with the player entity as its target
     */
    private void spawnBear() {
        Entity combatEnemyNPC = EnemyFactory.createBearCombatEnemy();
        spawnEntityAt(combatEnemyNPC, new GridPoint2(785, 337), true, true);
    }

    /**
     * spawns a pigeon enemy, with the player entity as its target
     */
    private void spawnPigeon() {
        Entity combatEnemyNPC = EnemyFactory.createPigeonCombatEnemy();
        spawnEntityAt(combatEnemyNPC, new GridPoint2(785, 337), true, true);
    }

    /**
     * spawns an eel enemy, with the player entity as its target
     */
    private void spawnEel() {
        Entity combatEnemyNPC = EnemyFactory.createEelCombatEnemy();
        spawnEntityAt(combatEnemyNPC, new GridPoint2(785, 337), true, true);
    }

    /** Play the music for combat
     *
     */
    public void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    /** Pause the music for combat. Will be finalised and used when
     * combat pause is implemented
     */
    public void pauseMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.pause();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(combatTexture);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadSounds(forestSounds);
        resourceService.loadMusic(forestMusic);
        resourceService.loadSounds(questSounds);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    public void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(combatTexture);
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

    @Override
    public Entity getPlayer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayer'");
    }

    @Override
    public List<Entity> getEnemies() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnemies'");
    }
}
