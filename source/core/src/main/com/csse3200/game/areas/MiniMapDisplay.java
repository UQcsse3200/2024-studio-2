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
/**
 * A UI component responsible for displaying a mini map in the game. The mini map shows the
 * player's position as a green dot and enemies as red dots. It updates in real time, showing
 * the player's location and displaying enemies within a defined radius.
 */
public class MiniMapDisplay extends UIComponent {
    private Entity player;
    private List<Entity> enemies;
    private Texture miniMapBackground;
    private Image greenDotPointImage;
    private List<Image> redDotPointImages;
    private GameArea gameArea;
    private int scaleFactor = 50;
    private float miniMapX = 950;  // Minimap's X position on the screen
    private float miniMapY = 450;  // Minimap's Y position on the screen
    private int miniMapSize = 300;  // Size of the minimap

    /**
     * Constructor for the MiniMapDisplay component. It links the minimap to the current game area.
     *
     * @param gameArea The game area containing player and enemies data.
     */
    public MiniMapDisplay(GameArea gameArea) {
        this.gameArea = gameArea;
    }

    /**
     * Initializes the minimap, retrieves the player and enemy entities from the game area,
     * and adds the visual elements to the stage.
     */
    @Override
    public void create() {
        super.create();
        player = gameArea.getPlayer();
        enemies = gameArea.getEnemies();
        addActors();
    }

    /**
     * Adds the visual components representing the player and enemies to the minimap.
     */
    private void addActors() {
        initializeImages();
        stage.addActor(greenDotPointImage);
        for (Image redDot : redDotPointImages) {
            stage.addActor(redDot);
        }
    }

    /**
     * Initializes the background of the minimap and creates images for the player (a green dot)
     * and enemies (red dots).
     */
    private void initializeImages() {
        Pixmap pixmap = new Pixmap(miniMapSize, miniMapSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fillCircle(miniMapSize / 2, miniMapSize / 2, miniMapSize / 2);
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

    /**
     * Transfers an entity's world position to a minimap position based on the player's position
     * with a scale factor to fit the world into the minimap.
     *
     * @param entity The entity whose position needs to be transferred to the minimap.
     * @return The transformed position in minimap coordinates.
     */
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

    /**
     * Updates the minimap once per frame, moving the player's dot and the enemy dots
     * based on their current positions.
     * It hides enemy dots if they are outside the minimap's circular boundary.
     */
    @Override
    public void update() {
        float centerX = miniMapX + miniMapSize / 2;
        float centerY = miniMapY + miniMapSize / 2;
        float minimapRadius = miniMapSize / 2;

        //Update green point position (player in minimap)
        Vector2 playerMiniMapPos = transferToMiniMapPos(player);
        greenDotPointImage.setPosition(playerMiniMapPos.x, playerMiniMapPos.y);

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

    /**
     * Draws the minimap on the screen.
     *
     * @param batch The SpriteBatch used for drawing.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        batch = new SpriteBatch();
        batch.begin();
        batch.draw(miniMapBackground, miniMapX, miniMapY, miniMapSize, miniMapSize);
        batch.end();
    }
}