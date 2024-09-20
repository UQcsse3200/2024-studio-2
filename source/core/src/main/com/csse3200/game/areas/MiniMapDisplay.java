package com.csse3200.game.areas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.areas.MapHandler;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Drawable;

import java.util.ArrayList;
import java.util.List;

public class MiniMapDisplay extends UIComponent {

    private Entity player;
    private List<Entity> enemies;
    private Texture miniMapBackground;
    private Image greenDotPointImage;
    private List<Image> redDotPointImages;
    private GameArea gameArea;
    private float miniMapX = 300;  // Minimap's X position on the screen
    private float miniMapY = 400;  // Minimap's Y position on the screen
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
        stage.addActor(greenDotPointImage);
        for (Image redDot : redDotPointImages) {
            stage.addActor(redDot);
        }
    }

    private void initializeImages() {
        Pixmap pixmap = new Pixmap(miniMapSize, miniMapSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fillRectangle(0, 0, miniMapSize, miniMapSize);
        // Convert the Pixmap to a texture
        miniMapBackground = new Texture(pixmap);

        // Dispose the Pixmap after creating the texture (no longer needed)
        pixmap.dispose();
        greenDotPointImage = new Image(new Texture("images/minimap/greenDotPoint.png"));
        greenDotPointImage.setSize(7, 7);
        redDotPointImages = new ArrayList<>();
        for(Entity enemy : enemies) {
            Image redDotImage = new Image(new Texture("images/minimap/redDotPoint.png"));
            redDotImage.setSize(5, 5);
            redDotPointImages.add(redDotImage);
        }

    }


    private Vector2 transferToMiniMapPos(Entity entity) {
        Vector2 playerPos = player.getPosition();
        Vector2 entityPos = entity.getPosition();

        Vector2 enityMiniMapPos = new Vector2((entityPos.x - playerPos.x) /100 * miniMapSize, (entityPos.y - playerPos.y) / 100 * miniMapSize);
        enityMiniMapPos.add(miniMapX + miniMapSize/2, miniMapY + miniMapSize/2);

        return enityMiniMapPos;
    }

    @Override
    public void update() {
        //Update green point position (player in minimap)
        Vector2 playerMiniMapPos = transferToMiniMapPos(player);
        greenDotPointImage.setPosition(playerMiniMapPos.x, playerMiniMapPos.y);
        //Update red points position (enemies in minimap)

        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);
            Vector2 enemyMiniMapPos = transferToMiniMapPos(enemy);
            redDotPointImages.get(i).setPosition(enemyMiniMapPos.x, enemyMiniMapPos.y);
            System.out.println(enemyMiniMapPos);
        }


    }
    @Override
    protected void draw(SpriteBatch batch) {
        batch = new SpriteBatch();
        batch.begin();
        batch.draw(miniMapBackground, miniMapX, miniMapY, miniMapSize, miniMapSize);
        batch.end();
    }
}
