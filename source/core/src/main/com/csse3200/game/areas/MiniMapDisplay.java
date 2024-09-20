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
import org.lwjgl.opengl.Drawable;

import java.util.List;

public class MiniMapDisplay extends UIComponent {

    private Entity player;
    private List<Entity> enemies;
    private Texture miniMapBackground;
    private Image greenDotPointImage;
    private Image redDotPointImage;
    private Table table;
    private GameArea gameArea;

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
        initializeTables();
        addPointsToMiniMap();

        stage.addActor(table);
    }

    private void initializeImages() {
        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fillRectangle(0, 0, 100, 100);
        // Convert the Pixmap to a texture
        miniMapBackground = new Texture(pixmap);

        // Dispose the Pixmap after creating the texture (no longer needed)
        pixmap.dispose();
        greenDotPointImage = new Image(new Texture("images/minimap/greenDotPoint.png"));
        redDotPointImage = new Image(new Texture("images/minimap/redDotPoint.png"));

    }

    public Table initializeTables() {
        initializeImages();
        table = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(miniMapBackground)));
        table.setSize(200, 200);
        table.setPosition(500, 500);
        table.setVisible(true);
        stage.addActor(table);
        return table;
    }

    private void addPointsToMiniMap() {
        table.add(greenDotPointImage).size(15,15);
        for (Entity enemy : enemies) {
            table.add(redDotPointImage).size(5,5);
        }
    }


    private Vector2 transformEnityToDotPoint(Entity entity, int size) {
        Vector2 playerPos = player.getPosition();
        Vector2 entityPos = entity.getPosition();

        Vector2 enityMiniMapPos = new Vector2((entityPos.x - playerPos.x)/size, (entityPos.y - playerPos.y)/size);

        return enityMiniMapPos;
    }

    @Override
    public void update() {
        //Update green point position (player in minimap)
        Vector2 playerMiniMapPos = transformEnityToDotPoint(player, 200);
        greenDotPointImage.setPosition(playerMiniMapPos.x, playerMiniMapPos.y);
        //Update red points position (enemies in minimap)

        for (Entity enemy : enemies) {
            Vector2 enemyMiniMapPos = transformEnityToDotPoint(enemy, 100);
            redDotPointImage.setPosition(enemyMiniMapPos.x, enemyMiniMapPos.y);
        }

        //Update big red points position (boss in minimap)

    }
    @Override
    protected void draw(SpriteBatch batch) {

    }
}
