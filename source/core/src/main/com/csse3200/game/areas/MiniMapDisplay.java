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
    private Texture miniMapBackground;
    private Image blueDotPointImage;
    private List<Image> redDotPointImages;
    private final GameArea gameArea;
    private static final int SCALE_FACTOR = 50;
    private static final float MINI_MAP_X = 15;  // Minimap's X position on the screen
    private static final float MINI_MAP_Y = 15;  // Minimap's Y position on the screen
    private static final int MINI_MAP_SIZE = 300;  // Size of the minimap

    public MiniMapDisplay(GameArea gameArea) {
        this.gameArea = gameArea;
    }

    @Override
    public void create() {
        super.create();
        player = gameArea.getPlayer();
        enemies = gameArea.getEnemies();
        addActors();
    }

    private void addActors() {
        initializeImages();
        stage.addActor(blueDotPointImage);
        for (Image redDot : redDotPointImages) {
            stage.addActor(redDot);
        }
    }

    private void initializeImages() {
        Pixmap pixmap = new Pixmap(MINI_MAP_SIZE, MINI_MAP_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fillCircle(MINI_MAP_SIZE / 2, MINI_MAP_SIZE / 2, MINI_MAP_SIZE / 2);
        // Convert the Pixmap to a texture
        miniMapBackground = new Texture("images/minimap/minimap_background_land.png");

        // Dispose the Pixmap after creating the texture (no longer needed)
        pixmap.dispose();
        blueDotPointImage = new Image(new Texture("images/minimap/blueDotPoint.png"));
        blueDotPointImage.setSize(15, 15);
        redDotPointImages = new ArrayList<>();
        initializeRedDotImages();
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

    private Vector2 transferToMiniMapPos(Entity entity) {
        Vector2 playerPos = player.getPosition();
        Vector2 entityPos = entity.getPosition();

        Vector2 entityMiniMapPos = new Vector2(
                (entityPos.x - playerPos.x) / SCALE_FACTOR * MINI_MAP_SIZE,
                (entityPos.y - playerPos.y) / SCALE_FACTOR * MINI_MAP_SIZE
        );
        entityMiniMapPos.add(MINI_MAP_X + (float) MINI_MAP_SIZE / 2,
                MINI_MAP_Y + (float) MINI_MAP_SIZE / 2);

        return entityMiniMapPos;
    }

    @Override
    public void update() {
        float centerX = MINI_MAP_X + (float) MINI_MAP_SIZE / 2;
        float centerY = MINI_MAP_Y + (float) MINI_MAP_SIZE / 2;
        float minimapRadius = (float) MINI_MAP_SIZE / 2;

        // Synchronize the number of red dot images with the number of enemies
        if (redDotPointImages.size() != enemies.size()) {
            initializeRedDotImages();
        }

        // Update blue point position (player on the minimap)
        Vector2 playerMiniMapPos = transferToMiniMapPos(player);
        blueDotPointImage.setPosition(playerMiniMapPos.x, playerMiniMapPos.y);

        // Update red points position (enemies on the minimap)
        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
            Vector2 enemyMiniMapPos = transferToMiniMapPos(enemy);

            float distanceFromCenter = Vector2.dst(centerX, centerY, enemyMiniMapPos.x, enemyMiniMapPos.y);
            if (distanceFromCenter <= minimapRadius) {
                redDotPointImages.get(i).setVisible(true);
                redDotPointImages.get(i).setPosition(enemyMiniMapPos.x, enemyMiniMapPos.y);
            } else {
                redDotPointImages.get(i).setVisible(false);
            }
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        SpriteBatch newBatch = new SpriteBatch();
        newBatch.begin();
        newBatch.draw(miniMapBackground, MINI_MAP_X, MINI_MAP_Y, MINI_MAP_SIZE, MINI_MAP_SIZE);
        newBatch.end();
    }
}
