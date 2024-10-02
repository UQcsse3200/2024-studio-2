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
    private GameArea gameArea;
    private int scaleFactor = 50;
    private float miniMapX = 15;  // Minimap's X position on the screen
    private float miniMapY = 15;  // Minimap's Y position on the screen
    private int miniMapSize = 300;  // Size of the minimap

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
        Pixmap pixmap = new Pixmap(miniMapSize, miniMapSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fillCircle(miniMapSize / 2, miniMapSize / 2, miniMapSize / 2);
        // Convert the Pixmap to a texture
        miniMapBackground = new Texture("images/minimap/minimap_background_land.png");

        // Dispose the Pixmap after creating the texture (no longer needed)
        pixmap.dispose();
        blueDotPointImage = new Image(new Texture("images/minimap/blueDotPoint.png"));
        blueDotPointImage.setSize(12, 13);
        redDotPointImages = new ArrayList<>();
        for(Entity enemy : enemies) {
            Image redDotImage = new Image(new Texture("images/minimap/redDotPoint.png"));
            redDotImage.setSize(10, 10);
            redDotPointImages.add(redDotImage);
        }

    }


    private Vector2 transferToMiniMapPos(Entity entity) {
        Vector2 playerPos = player.getPosition();
        Vector2 entityPos = entity.getPosition();

        Vector2 enityMiniMapPos = new Vector2(
                (entityPos.x - playerPos.x) / scaleFactor * miniMapSize,
                (entityPos.y - playerPos.y) / scaleFactor * miniMapSize
        );
        enityMiniMapPos.add(miniMapX + miniMapSize/2, miniMapY + miniMapSize/2);

        return enityMiniMapPos;
    }

    @Override
    public void update() {
        float centerX = miniMapX + miniMapSize / 2;
        float centerY = miniMapY + miniMapSize / 2;
        float minimapRadius = miniMapSize / 2;
        //Update green point position (player in minimap)
        Vector2 playerMiniMapPos = transferToMiniMapPos(player);
        blueDotPointImage.setPosition(playerMiniMapPos.x, playerMiniMapPos.y);
        //Update red points position (enemies in minimap)

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
            System.out.println(enemyMiniMapPos);
            //Hide enemyDotPoints if these are outside the minimap
        }

        // Try to take check enemies and put to the minimap

    }
    @Override
    protected void draw(SpriteBatch batch) {
        batch = new SpriteBatch();
        batch.begin();
        batch.draw(miniMapBackground, miniMapX, miniMapY, miniMapSize, miniMapSize);
        batch.end();
    }
}