package com.csse3200.game.areas;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.areas.MapHandler;
import org.lwjgl.opengl.Drawable;

import java.util.List;

public class MiniMapDisplay extends UIComponent {

    private GameArea currentGameArea;
    private Entity player;
    private List<Entity> enemies;
    private Texture miniMapBackground;
    private Image greenDotPoint;
    private Image redDotPoint;
    private Table table;


    @Override
    public void create() {
        super.create();
        addActors();
        currentGameArea = MapHandler.getCurrentMap();
        player = currentGameArea.getPlayer();
        enemies = currentGameArea.getEnemies();
    }
    private void addActors() {
        initializeImages();
    }

    private void initializeTables() {
        table = new Table();
    }

    private void initializeImages() {
        Pixmap pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fillRectangle(0, 0, 50, 50);
        // Convert the Pixmap to a texture
        miniMapBackground = new Texture(pixmap);
        // Dispose the Pixmap after creating the texture (no longer needed)
        pixmap.dispose();
        greenDotPoint = new Image(new Texture("/images/minimap/greenDotPoint.png"));
        redDotPoint = new Image(new Texture("/images/minimap/greenDotPoint.png"));
    }

    private void addPointsToMiniMap() {

    }


    private void transformEnityToDotPoint(Entity entity, int size) {
        Vector2 playerPos = player.getPosition();
        Vector2 entityPos = entity.getPosition();

        Vector2 miniMapPlayerPos = Vector2.Zero;
        Vector2 miniMapEnityPos = new Vector2(entityPos.x - playerPos.x, entityPos.y - playerPos.y);

    }

    @Override
    public void update() {
        //Update green point position (player in minimap)

        //Update red points position (enemies in minimap)

        //Update big red points position (boss in minimap)

    }
    @Override
    protected void draw(SpriteBatch batch) {

    }
}
