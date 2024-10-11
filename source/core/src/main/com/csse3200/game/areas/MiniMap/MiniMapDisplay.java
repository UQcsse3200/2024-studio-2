package com.csse3200.game.areas.MiniMap;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.ui.UIComponent;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

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
    float centerX = miniMapX + miniMapDiameter / 2;
    float centerY = miniMapY + miniMapDiameter / 2;
    float minimapRadius = miniMapDiameter / 2;
    int frameCount;

    public MiniMapDisplay() {
        player = new Entity();
        enemies = new ArrayList<>();
        bosses = new ArrayList<>();
        friendlyNPCs = new ArrayList<>();
        minigameNPCs = new ArrayList<>();
    }
    public MiniMapDisplay(GameArea gameArea) {
        this.gameArea = gameArea;
    }

    @Override
    public void create() {
        super.create();
        System.out.println("ABC");
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

    private void initializeMiniMap() {
        centerX = miniMapX + miniMapDiameter / 2;
        centerY = miniMapY + miniMapDiameter / 2;
        minimapRadius = miniMapDiameter / 2;
        miniMapBackground = new Texture("images/minimap/minimap_background_land.png");

        initializeAllImages();
    }

    public void initializeAllImages() {
        initializeGreenDotImages();
        initializeRedDotImages();
        initializeBlueDotImages();
        initializeLargeRedPointImages();
        initializePurplePointImages();
    }

    // Initialize green dots to match the number of player
    public void initializeGreenDotImages() {
        greenDotPointImage = new Image(new Texture("images/minimap/greenDotPoint.png"));
        greenDotPointImage.setSize(15, 15);
        stage.addActor(greenDotPointImage);
    }

    // Initialize red dots to match the number of enemies
    public void initializeRedDotImages() {
        for (Entity enemy : enemies) {
            Image redDotImage = new Image(new Texture("images/minimap/redDotPoint.png"));
            redDotImage.setSize(10, 10);
            redDotPointImages.add(redDotImage);
            stage.addActor(redDotImage);  // Add the red dot actor to the stage
        }
    }
    /**
     * Initializes blue dot points for the friendly NPCs.
     */
    public void initializeBlueDotImages() {
        for (Entity npc : friendlyNPCs) {
            Image blueDotImage = new Image(new Texture("images/minimap/blueDotPoint.png"));
            blueDotImage.setSize(10, 10);
            blueDotPointImages.add(blueDotImage);
            stage.addActor(blueDotImage);  // Add the red dot actor to the stage
        }
    }

    /**
     * Initializes large red dot points for the friendly NPCs.
     */
    public void initializeLargeRedPointImages() {
        for (Entity boss : bosses) {
            Image largeRedDotImage = new Image(new Texture("images/minimap/redDotPoint.png"));
            largeRedDotImage.setSize(30, 30);
            largeRedPointImages.add(largeRedDotImage);
            stage.addActor(largeRedDotImage);  // Add the red dot actor to the stage
        }
    }

    /**
     * Initializes purple dot points for the minigame NPCs.
     */
    public void initializePurplePointImages() {
        for (Entity minigameNpc : minigameNPCs) {
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
        entityMiniMapPos.add(miniMapX + miniMapDiameter / 2, miniMapY + miniMapDiameter / 2);

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
        batch = new SpriteBatch();
        batch.begin();
        batch.draw(miniMapBackground, miniMapX, miniMapY, miniMapDiameter, miniMapDiameter);
        batch.end();
    }



    public Entity getPlayer() {
        return player;
    }

    public float getMiniMapX() {
        return miniMapX;
    }
    public float getMiniMapY() {
        return miniMapX;
    }
    public float getMiniMapDiameter() {
        return miniMapDiameter;
    }
    public void setMiniMapPosition(float posX, float posY) {
        this.miniMapX = posX;
        this.miniMapY = posY;
    }
    public void setMiniMapDiameter(int size) {
        this.miniMapDiameter = size;
    }


}
