package com.csse3200.game.areas.combat;

import java.util.Arrays;
import java.util.List;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.CombatTerrainFactory;
import com.csse3200.game.areas.terrain.CombatTerrainFactory.TerrainType;
import com.csse3200.game.components.animal.AnimalRouletteActions1;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CombatAnimalFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class CombatArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(CombatArea.class);

    private GdxGame game;

    private Entity player;
    private Entity playerDisplay;
    private GridPoint2 playerSpawn = new GridPoint2(290,  335); // 9, 14...384, 256 // 290, 335

    private CombatTerrainFactory combatTerrainFactory;
    private static final GridPoint2 MAP_SIZE = new GridPoint2(1030, 590);

    private Entity enemy;
    private Entity enemyDisplay;

    public enum CombatAnimation { IDLE, MOVE }
    public enum KINGDOM {LAND, AIR, WATER }

    public static KINGDOM kingdomType;

    /**
     * Initialise this ForestGameArea to use the provided CombatTerrainFactory and the enemy which player
     * has engaged combat with.
     *
     * @param terrainFactory CombatTerrainFactory used to create the terrain for the GameArea.
     * @requires terrainFactory != null
     */
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
        // get the enemy type player collided into for combat and spawn that
        switch (enemy.getEnemyType()) {
            case MONKEY -> {spawnMonkey(); kingdomType = KINGDOM.LAND;}
            case FROG -> {spawnFrog(); kingdomType = KINGDOM.WATER;}
            case CHICKEN -> {spawnChicken(); kingdomType = KINGDOM.LAND;}
            case BEAR -> {spawnBear(); kingdomType = KINGDOM.LAND;}
            case JOEY -> {spawnJoey(); kingdomType = KINGDOM.LAND;}
            case KANGAROO -> {spawnKangaBoss(); kingdomType = KINGDOM.LAND;}
            case WATER_BOSS -> {spawnWaterBoss(); kingdomType = KINGDOM.WATER;}
            case AIR_BOSS -> {spawnAirBoss(); kingdomType = KINGDOM.AIR;}
            case BEE -> {spawnBee(); kingdomType = KINGDOM.AIR;}
            case PIGEON -> {spawnPigeon(); kingdomType = KINGDOM.AIR;}
            case EEL -> {spawnEel(); kingdomType = KINGDOM.WATER;}
            case OCTOPUS -> {spawnOctopus(); kingdomType = KINGDOM.WATER;}
            case BIGSAWFISH -> {spawnBigSawfish(); kingdomType = KINGDOM.WATER;}
            case MACAW -> {spawnMacaw(); kingdomType = KINGDOM.AIR;}
            // case null, default -> spawnCombatEnemy(); // Combat Enemy
        }
        spawnPlayer();
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
    public void spawnTerrain() {
        if(kingdomType == KINGDOM.LAND){
          terrain = combatTerrainFactory.createBackgroundTerrainLand(TerrainType.FOREST_DEMO, playerSpawn, MAP_SIZE);
        } else if (kingdomType == KINGDOM.AIR) {
           terrain = combatTerrainFactory.createBackgroundTerrainAir(TerrainType.FOREST_DEMO, playerSpawn, MAP_SIZE);
        } else { // water
           terrain = combatTerrainFactory.createBackgroundTerrainWater(TerrainType.FOREST_DEMO, playerSpawn, MAP_SIZE);
        }
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
        spawnCameraInvisibleEntity(); // Create invisible entity to centre camera at centre of background

        /*
         * The following entity is the real entity of the player to be used for combat,
         * with health, stats, etc.
         */
        String imagePath = AnimalRouletteActions1.getSelectedAnimalImagePath();
        Entity newPlayer = PlayerFactory.createCombatPlayer(imagePath);
        if (imagePath.equals("images/croc.png")){
            if (kingdomType == KINGDOM.WATER) {
                playerSpawn = new GridPoint2(330, 337);
            } else if (kingdomType == KINGDOM.AIR) {
                playerSpawn = new GridPoint2(370, 240);
                newPlayer.scaleHeight(130f);
            } else {
                playerSpawn = new GridPoint2(332, 210);
                newPlayer.scaleHeight(150f);
            }
            // newPlayer.scaleHeight(60f);
        } else if (imagePath.equals("images/dog.png")){
            if(kingdomType == KINGDOM.WATER) {
                playerSpawn = new GridPoint2(360, 330);
            } else if  (kingdomType == KINGDOM.AIR) {
                playerSpawn = new GridPoint2(337, 215);
            } else {
                playerSpawn = new GridPoint2(337, 190);
            }
        } else { //animal is bird
            if(kingdomType == KINGDOM.WATER) {
                // newPlayer.setScale(5f, 5f); // 2.5f, 2.5f
                playerSpawn = new GridPoint2(380, 340);
                newPlayer.scaleHeight(180f);
            } else if (kingdomType == KINGDOM.AIR) {
                playerSpawn = new GridPoint2(337, 220);
                newPlayer.scaleHeight(160);
            } else {
                playerSpawn = new GridPoint2(350, 200);
                newPlayer.scaleHeight(200);
            }
        }
        spawnEntityAt(newPlayer, playerSpawn, true, true);
    }

    /** Spawns an invisible entity to set the camera at the centre of the screen without having to
     * attach the camera to the real player entity. This entity should be ignored, and it's
     * health/stats etc.
     * are NOT to be used for combat logic or anywehre else
     */
    private void spawnCameraInvisibleEntity(){
        /* Entity nP is a non-visible entity placed at the centre of the background to
         * ensure the camera component stays stagnant in the centre of the combat background.
         * The entity serves no other purpose and is not visible
         */
        String iP = AnimalRouletteActions1.getSelectedAnimalImagePath();
        Entity nP = PlayerFactory.createCombatPlayer(iP);
        nP.addComponent(combatTerrainFactory.getCameraComponent());
        if (kingdomType == KINGDOM.WATER) {
            nP.setPosition(520, 250); // water background
        } else {
            nP.setPosition(520, 250);
        }
    }

    /** Spawn a combat enemy. Different to a regular enemy npc */
    private void spawnCombatEnemy() {
        Entity combatEnemyNPC = CombatAnimalFactory.createJoeyCombatEnemy();
        spawnEntityAt(combatEnemyNPC, new GridPoint2(800, 346), true, true);
    }

    /** Spawn a combat enemy. Different to a regular enemy npc */
    private void spawnKangaBoss() {
        Entity newEnemy = CombatAnimalFactory.createKangaBossCombatEntity();
        spawnEntityAt(newEnemy, new GridPoint2(800, 230), true, true);
        this.enemyDisplay = newEnemy;
    }

    /** Spawn a combat enemy. Different to a regular enemy npc */
    private void spawnWaterBoss() {
        Entity newEnemy = CombatAnimalFactory.createWaterBossCombatEntity();
        spawnEntityAt(newEnemy, new GridPoint2(800, 390), true, true);
        this.enemyDisplay = newEnemy;
    }

    /** Spawn a combat enemy. Different to a regular enemy npc */
    private void spawnAirBoss() {
        Entity newEnemy = CombatAnimalFactory.createAirBossCombatEntity();
        spawnEntityAt(newEnemy, new GridPoint2(800, 330), true, true);
        this.enemyDisplay = newEnemy;
    }

    /** The following functions spawn chicken, monkey, and frog entities as NPC's for static combat
     */
    private void spawnChicken() {
        Entity newEnemy = CombatAnimalFactory.createChickenCombatEnemy();
        spawnEntityAt(newEnemy, new GridPoint2(760, 190), true, true);
        this.enemyDisplay = newEnemy;
    }

    /**
     * spawns a frog enemy, with the player entity as its target
     */
    private void spawnFrog() {
        Entity newEnemy = CombatAnimalFactory.createFrogCombatEnemy();
        spawnEntityAt(newEnemy, new GridPoint2(785, 320), true, true);
        this.enemyDisplay = newEnemy;
    }

    /**
     * spawns a monkey enemy, with the player entity as its target
     */
    private void spawnMonkey() {
        Entity newEnemy = CombatAnimalFactory.createMonkeyCombatEnemy();
        spawnEntityAt(newEnemy, new GridPoint2(750, 190), true, true);
        this.enemyDisplay = newEnemy;
    }

    /**
     * spawns a bear enemy, with the player entity as its target
     */
    private void spawnBear() {
        Entity newEnemy = CombatAnimalFactory.createBearCombatEnemy();
        spawnEntityAt(newEnemy, new GridPoint2(750, 185), true, true);
        this.enemyDisplay = newEnemy;
    }

    /**
     * spawns a macaw enemy, with the player entity as its target
     */
    private void spawnMacaw() {
        Entity newEnemy = CombatAnimalFactory.createMacawCombatEnemy();
        spawnEntityAt(newEnemy, new GridPoint2(785, 237), true, true);
        this.enemyDisplay = newEnemy;
    }

    /**
     * spawns a bee enemy, with the player entity as its target
     */
    private void spawnBee() {
        Entity newEnemy = CombatAnimalFactory.createBeeCombatEnemy();
        spawnEntityAt(newEnemy, new GridPoint2(785, 217), true, true);
        this.enemyDisplay = newEnemy;
    }

    /**
     * spawns an octopus enemy, with the player entity as its target
     */
    private void spawnOctopus() {
        Entity newEnemy = CombatAnimalFactory.createOctopusCombatEnemy();
        spawnEntityAt(newEnemy, new GridPoint2(785, 337), true, true);
        this.enemyDisplay = newEnemy;
    }
    
    /**
     * spawns a pigeon enemy, with the player entity as its target
     */
    private void spawnPigeon() {
        Entity newEnemy = CombatAnimalFactory.createPigeonCombatEnemy();
        spawnEntityAt(newEnemy, new GridPoint2(785, 217), true, true);
        this.enemyDisplay = newEnemy;
    }

    /**
     * spawns an eel enemy, with the player entity as its target
     */
    private void spawnEel() {
        Entity newEnemy = CombatAnimalFactory.createEelCombatEnemy();
        spawnEntityAt(newEnemy, new GridPoint2(780, 337), true, true);
        this.enemyDisplay = newEnemy;
    }

    /**
     * spawns a big saw fish enemy, with the player entity as its target
     */
    private void spawnBigSawfish() {
        Entity newEnemy = CombatAnimalFactory.createBigsawfishCombatEnemy();
        spawnEntityAt(newEnemy, new GridPoint2(785, 337), true, true);
        this.enemyDisplay = newEnemy;
    }

    /**
     * spawns a joey enemy, with the player entity as its target
     */
    private void spawnJoey() {
        Entity newEnemy = CombatAnimalFactory.createJoeyCombatEnemy();
        spawnEntityAt(newEnemy, new GridPoint2(796, 180), true, true);
        this.enemyDisplay = newEnemy;
    }

    /**
     * Plays an enemy animation in combat
     * @param animation CombatAnimation (IDLE or MOVE) to trigger
     */
    public void startEnemyAnimation(CombatAnimation animation) {
        switch (animation) {
            case IDLE:
                enemyDisplay.getEvents().trigger("idleLeft");
                break;
            case MOVE:
                enemyDisplay.getEvents().trigger("moveLeft");
                break;
        }
    }

    /**
     * Plays a player animation in combat
     * @param animation CombatAnimation (IDLE or MOVE) to trigger
     */
    public void startPlayerAnimation(CombatAnimation animation) {
        switch (animation) {
            case IDLE:
                playerDisplay.getEvents().trigger("idleRight");
                break;
            case MOVE:
                playerDisplay.getEvents().trigger("moveRight");
                break;
        }
    }

    /**
     * Play the music for combat
     */
    public void playMusic() {
        Entity.EnemyType[] bossTypes = {Entity.EnemyType.KANGAROO, Entity.EnemyType.WATER_BOSS, Entity.EnemyType.AIR_BOSS};

        AudioManager.stopMusic();
        if (Arrays.asList(bossTypes).contains(this.enemy.getEnemyType())) {
            AudioManager.playMusic("sounds/boss-combat-music.mp3", true);
        } else {
            AudioManager.playMusic(CombatAreaConfig.COMBATBACKGROUND_MUSIC, true);
        }
    }

    /** Pause the music for combat. Will be finalised and used when
     * combat pause is implemented
     */
    public void pauseMusic() {
        // Stop the music using AudioManager
        AudioManager.stopMusic();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(CombatAreaConfig.combatTexture);
        resourceService.loadTextureAtlases(CombatAreaConfig.forestTextureAtlases);
        resourceService.loadSounds(CombatAreaConfig.forestSounds);
        resourceService.loadMusic(CombatAreaConfig.forestMusic);
        resourceService.loadSounds(CombatAreaConfig.questSounds);
        resourceService.loadSounds(CombatAreaConfig.combatSounds);
        resourceService.loadMusic(CombatAreaConfig.combatBackgroundMusic);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    public void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(CombatAreaConfig.combatTexture);
        resourceService.unloadAssets(CombatAreaConfig.forestTextureAtlases);
        resourceService.unloadAssets(CombatAreaConfig.forestSounds);
        resourceService.unloadAssets(CombatAreaConfig.forestMusic);
        resourceService.unloadAssets(CombatAreaConfig.questSounds);
        resourceService.unloadAssets(CombatAreaConfig.combatSounds);
        resourceService.unloadAssets(CombatAreaConfig.combatBackgroundMusic);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(CombatAreaConfig.COMBATBACKGROUND_MUSIC,
                Music.class).stop();
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

    @Override
    public List<Entity> getBosses() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnemies'");
    }

    @Override
    public List<Entity> getFriendlyNPCs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnemies'");
    }

    @Override
    public List<Entity> getMinigameNPCs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnemies'");
    }
}
