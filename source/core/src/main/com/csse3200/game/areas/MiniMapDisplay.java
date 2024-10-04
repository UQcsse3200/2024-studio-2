package com.csse3200.game.areas;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.ui.UIComponent;

import java.util.ArrayList;
import java.util.List;

public class MiniMapDisplay extends UIComponent {

    private Entity player;
    private List<Entity> enemies;
    private List<Entity> bosses;
    private List<Entity> friendlyNPCs;
    private Texture miniMapBackground;
    private Image greenDotPointImage;
    private List<Image> redDotPointImages;
    private List<Image> blueDotPointImages;
    private List<Image> largeRedPointImages;
    private GameArea gameArea;
    private int scaleFactor = 50;
    private float miniMapX = 15;  // Minimap's X position on the screen
    private float miniMapY = 15;  // Minimap's Y position on the screen
    private int miniMapSize = 300;  // Size of the minimap
    float centerX = miniMapX + miniMapSize / 2;
    float centerY = miniMapY + miniMapSize / 2;
    float minimapRadius = miniMapSize / 2;

    public MiniMapDisplay(GameArea gameArea) {
        this.gameArea = gameArea;
    }

    @Override
    public void create() {
        super.create();

        centerX = miniMapX + miniMapSize / 2;
        centerY = miniMapY + miniMapSize / 2;
        minimapRadius = miniMapSize / 2;

        player = gameArea.getPlayer();
        enemies = gameArea.getEnemies();
        bosses = gameArea.getBosses();
        friendlyNPCs = gameArea.getFriendlyNPCs();
        redDotPointImages = new ArrayList<>();
        blueDotPointImages = new ArrayList<>();
        largeRedPointImages = new ArrayList<>();
        initializeMiniMap();
    }

    private void initializeMiniMap() {
        Pixmap pixmap = new Pixmap(miniMapSize, miniMapSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 1);
        pixmap.fillCircle(miniMapSize / 2, miniMapSize / 2, miniMapSize / 2);

        // Convert the Pixmap to a texture
        miniMapBackground = new Texture("images/minimap/minimap_background_land.png");

        // Dispose the Pixmap after creating the texture (no longer needed)
        pixmap.dispose();
        initializeGreenDotImages();
        initializeRedDotImages();
        initializeBlueDotImages();
        initializeLargeRedPointImages();
    }

    // Initialize green dots to match the number of player
    private void initializeGreenDotImages() {
        greenDotPointImage = new Image(new Texture("images/minimap/greenDotPoint.png"));
        greenDotPointImage.setSize(15, 15);
        stage.addActor(greenDotPointImage);
    }

    // Initialize red dots to match the number of enemies
    private void initializeRedDotImages() {
        redDotPointImages.clear();
        for (Entity enemy : enemies) {
            Image redDotImage = new Image(new Texture("images/minimap/redDotPoint.png"));
            redDotImage.setSize(10, 10);
            redDotPointImages.add(redDotImage);
            stage.addActor(redDotImage);  // Add the red dot actor to the stage
        }
    }

    public void initializeBlueDotImages() {
        blueDotPointImages.clear();
        for (Entity npc : friendlyNPCs) {
            Image purpleDotImage = new Image(new Texture("images/minimap/blueDotPoint.png"));
            purpleDotImage.setSize(10, 10);
            blueDotPointImages.add(purpleDotImage);
            stage.addActor(purpleDotImage);  // Add the red dot actor to the stage
        }
    }

    public void initializeLargeRedPointImages() {
        largeRedPointImages.clear();
        for (Entity boss : bosses) {
            Image largeRedDotImage = new Image(new Texture("images/minimap/redDotPoint.png"));
            largeRedDotImage.setSize(20, 20);
            largeRedPointImages.add(largeRedDotImage);
            stage.addActor(largeRedDotImage);  // Add the red dot actor to the stage
        }
    }

    private Vector2 transferToMiniMapPos(Entity entity) {
        Vector2 playerPos = player.getPosition();
        Vector2 entityPos = entity.getPosition();

        Vector2 entityMiniMapPos = new Vector2(
                (entityPos.x - playerPos.x) / scaleFactor * miniMapSize,
                (entityPos.y - playerPos.y) / scaleFactor * miniMapSize
        );
        entityMiniMapPos.add(miniMapX + miniMapSize / 2, miniMapY + miniMapSize / 2);

        return entityMiniMapPos;
    }

    private void updatePoints(List<Image> pointImages, List<Entity> entities) {
        // Synchronize the number of red dot images with the number of enemies
        if (pointImages.size() != entities.size()) {
            initializeRedDotImages();
        }
        // Update red points position (enemies on the minimap)
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

    @Override
    public void update() {
        // Update blue point position (player on the minimap)
        Vector2 playerMiniMapPos = transferToMiniMapPos(player);
        greenDotPointImage.setPosition(playerMiniMapPos.x, playerMiniMapPos.y);
        updatePoints(redDotPointImages, enemies);
        updatePoints(largeRedPointImages, bosses);
        updatePoints(blueDotPointImages, friendlyNPCs);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        batch = new SpriteBatch();
        batch.begin();
        batch.draw(miniMapBackground, miniMapX, miniMapY, miniMapSize, miniMapSize);
        batch.end();
    }
}
