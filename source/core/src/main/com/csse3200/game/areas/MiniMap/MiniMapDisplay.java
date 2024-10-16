package com.csse3200.game.areas.MiniMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.ui.UIComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * The MiniMapDisplay class is a UI component responsible for displaying a minimap
 * on the screen. It shows the player's position, as well as various entities like
 * enemies, bosses, friendly NPCs, and minigame NPCs, represented by different colored
 * dots. The minimap updates positions based on the entities' world coordinates.
 */
public class MiniMapDisplay extends UIComponent {

    private Entity player;
    private List<Entity> enemies;
    private List<Entity> bosses;
    private List<Entity> friendlyNPCs;
    private List<Entity> minigameNPCs;
    private Texture miniMapBackground;
    private Image greenDotPointImage;
    private List<Image> redDotPointImages;
    private List<Image> blueDotPointImages;
    private List<Image> largeRedPointImages;
    private List<Image> purplePointImages;
    private GameArea gameArea;
    private int scaleFactor = 50;
    private float miniMapX = 15;  // Minimap's X position on the screen
    private float miniMapY = 15;  // Minimap's Y position on the screen
    private int miniMapDiameter = 300;  // Size of the minimap
    float centerX = miniMapX + (float) miniMapDiameter / 2;
    float centerY = miniMapY + (float) miniMapDiameter / 2;
    float minimapRadius = (float) miniMapDiameter / 2;
    int frameCount;

    /**
     * Default constructor for MiniMapDisplay. Initializes all entity lists.
     */
    public MiniMapDisplay() {
        player = new Entity();
        enemies = new ArrayList<>();
        bosses = new ArrayList<>();
        friendlyNPCs = new ArrayList<>();
        minigameNPCs = new ArrayList<>();
    }

    /**
     * Constructor that initializes the MiniMapDisplay with a specified GameArea.
     *
     * @param gameArea the game area from which to retrieve player and entities.
     */
    public MiniMapDisplay(GameArea gameArea) {
        this.gameArea = gameArea;
    }

    /**
     * Creates the minimap, initializing the player and entity lists from the GameArea.
     */
    @Override
    public void create() {
        super.create();
        player = gameArea.getPlayer();
        enemies = gameArea.getEnemies();
        bosses = gameArea.getBosses();
        friendlyNPCs = gameArea.getFriendlyNPCs();
        minigameNPCs = gameArea.getMinigameNPCs();
        redDotPointImages = new ArrayList<>();
        blueDotPointImages = new ArrayList<>();
        largeRedPointImages = new ArrayList<>();
        purplePointImages = new ArrayList<>();
        initializeMiniMap();
    }

    /**
     * Initializes the minimap's center, radius, and background image.
     */
    private void initializeMiniMap() {
        centerX = miniMapX + (float) miniMapDiameter / 2;
        centerY = miniMapY + (float) miniMapDiameter / 2;
        minimapRadius = (float) miniMapDiameter / 2;
        miniMapBackground = new Texture("images/minimap/minimap_background_land.png");

        initializeAllImages();
    }

    /**
     * Initializes all dot images for player, enemies, bosses, friendly NPCs, and minigame NPCs.
     */
    public void initializeAllImages() {
        initializeGreenDotImages();
        initializeRedDotImages();
        initializeBlueDotImages();
        initializeLargeRedPointImages();
        initializePurplePointImages();
    }

    /**
     * Initializes the green dot image representing the player on the minimap.
     */
    public void initializeGreenDotImages() {
        greenDotPointImage = new Image(new Texture("images/minimap/greenDotPoint.png"));
        greenDotPointImage.setSize(15, 15);
        stage.addActor(greenDotPointImage);
    }

    /**
     * Initializes red dot images for each enemy on the minimap.
     */
    public void initializeRedDotImages() {
        for (int i = 0; i < enemies.size(); i++) {
            Image redDotImage = new Image(new Texture("images/minimap/redDotPoint.png"));
            redDotImage.setSize(10, 10);
            redDotPointImages.add(redDotImage);
            stage.addActor(redDotImage);  // Add the red dot actor to the stage
        }
    }

    /**
     * Initializes blue dot images for friendly NPCs on the minimap.
     */
    public void initializeBlueDotImages() {
        for (int i = 0; i < friendlyNPCs.size(); i++) {
            Image blueDotImage = new Image(new Texture("images/minimap/blueDotPoint.png"));
            blueDotImage.setSize(10, 10);
            blueDotPointImages.add(blueDotImage);
            stage.addActor(blueDotImage);  // Add the red dot actor to the stage
        }
    }

    /**
     * Initializes large red dot images for bosses on the minimap.
     */
    public void initializeLargeRedPointImages() {
        for (int i = 0; i < bosses.size(); i++) {
            Image largeRedDotImage = new Image(new Texture("images/minimap/redDotPoint.png"));
            largeRedDotImage.setSize(30, 30);
            largeRedPointImages.add(largeRedDotImage);
            stage.addActor(largeRedDotImage);  // Add the red dot actor to the stage
        }
    }

    /**
     * Initializes purple dot images for minigame NPCs on the minimap.
     */
    public void initializePurplePointImages() {
        for (int i = 0; i < minigameNPCs.size(); i++) {
            Image purpleDotImage = new Image(new Texture("images/minimap/purpleDotPoint.png"));
            purpleDotImage.setSize(10, 10);
            purplePointImages.add(purpleDotImage);
            stage.addActor(purpleDotImage);  // Add the red dot actor to the stage
        }
    }

    /**
     * Transfers the world position of an entity to the corresponding minimap position.
     *
     * @param entity the entity to transfer.
     * @return the position of the entity on the minimap.
     */
    public Vector2 transferToMiniMapPos(Entity entity) {
        Vector2 playerPos = player.getPosition();
        Vector2 entityPos = entity.getPosition();

        Vector2 entityMiniMapPos = new Vector2(
                (entityPos.x - playerPos.x) / scaleFactor * miniMapDiameter,
                (entityPos.y - playerPos.y) / scaleFactor * miniMapDiameter
        );
        entityMiniMapPos.add(miniMapX + (float)  miniMapDiameter / 2,
                miniMapY + (float) miniMapDiameter / 2);

        return entityMiniMapPos;
    }

    /**
     * Updates the positions of the dot points (entities) on the minimap.
     *
     * @param pointImages the list of dot images (e.g., enemies, NPCs, or bosses).
     * @param entities    the list of corresponding entities.
     */
    private void updatePoints(List<Image> pointImages, List<Entity> entities) {
        if (pointImages.size() != entities.size()) {
            if (pointImages.equals(redDotPointImages)) {
                initializeRedDotImages();
            } else if (pointImages.equals(blueDotPointImages)) {
                initializeBlueDotImages();
            } else if (pointImages.equals(largeRedPointImages)) {
                initializeLargeRedPointImages();
            } else if (pointImages.equals(purplePointImages)) {
                initializePurplePointImages();
            }
        }

        for (int i = 0; i < entities.size(); i++) {
            Entity enemy = entities.get(i);
            Vector2 enemyMiniMapPos = transferToMiniMapPos(enemy);

            float distanceFromCenter = Vector2.dst(centerX, centerY, enemyMiniMapPos.x, enemyMiniMapPos.y);
            if (distanceFromCenter <= minimapRadius) {
                pointImages.get(i).setVisible(true);
                pointImages.get(i).setPosition(enemyMiniMapPos.x, enemyMiniMapPos.y);
            } else {
                pointImages.get(i).setVisible(false);
            }
        }
    }

    /**
     * Updates all dot points on the minimap, including enemies, friendly NPCs, bosses, and minigame NPCs.
     */
    public void updateAllPoints() {
        updatePoints(redDotPointImages, enemies);
        updatePoints(blueDotPointImages, friendlyNPCs);
        updatePoints(largeRedPointImages, bosses);
        updatePoints(purplePointImages, minigameNPCs);
    }

    /**
     * Updates the minimap, adjusting the player and entity positions each frame.
     */
    @Override
    public void update() {
        // Update blue point position (player on the minimap)
        Vector2 playerMiniMapPos = transferToMiniMapPos(player);
        greenDotPointImage.setPosition(playerMiniMapPos.x, playerMiniMapPos.y);
        frameCount++;
        if (frameCount % 15 == 0) {
            updateAllPoints();
        }
    }

    /**
     * Draws the minimap background on the screen.
     *
     * @param batch the sprite batch used for drawing the minimap.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        SpriteBatch batchDupe = new SpriteBatch();
        batchDupe.begin();
        batchDupe.draw(miniMapBackground, miniMapX, miniMapY, miniMapDiameter, miniMapDiameter);
        batchDupe.end();
    }

    /**
     * Retrieves the player entity displayed on the minimap.
     *
     * @return the player entity.
     */
    public Entity getPlayer() {
        return player;
    }

    /**
     * Gets the X position of the minimap on the screen.
     *
     * @return the X coordinate of the minimap.
     */
    public float getMiniMapX() {
        return miniMapX;
    }

    /**
     * Gets the Y position of the minimap on the screen.
     *
     * @return the Y coordinate of the minimap.
     */
    public float getMiniMapY() {
        return miniMapY;
    }

    /**
     * Gets the diameter of the minimap.
     *
     * @return the diameter of the minimap.
     */
    public float getMiniMapDiameter() {
        return miniMapDiameter;
    }

    /**
     * Sets the position of the minimap on the screen.
     *
     * @param posX the new X coordinate for the minimap.
     * @param posY the new Y coordinate for the minimap.
     */
    public void setMiniMapPosition(float posX, float posY) {
        this.miniMapX = posX;
        this.miniMapY = posY;
    }

    /**
     * Sets the diameter of the minimap.
     *
     * @param size the new diameter for the minimap.
     */
    public void setMiniMapDiameter(int size) {
        this.miniMapDiameter = size;
    }


}
