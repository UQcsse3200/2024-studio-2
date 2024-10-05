package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.Component;

import java.util.ArrayList;
import java.util.List;

public class MapTabComponent extends Component {
    private Texture mapTexture;
    private Texture playerLocationTexture;
    private Texture landmarkIconTexture;
    private Texture xButtonTexture;
    private SpriteBatch batch;
    public boolean isMapVisible = false;
    private Rectangle xButtonBounds;
    private List<Vector2> landmarks;
    final GameArea gameArea;

    public MapTabComponent(GameArea gameArea) {
        this.gameArea = gameArea;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        mapTexture = new Texture(Gdx.files.internal("map/MAP.png"));
        playerLocationTexture = new Texture(Gdx.files.internal("map/Lion_Icon.png"));
        landmarkIconTexture = new Texture(Gdx.files.internal("map/landmark_icon.png"));
        xButtonTexture = new Texture(Gdx.files.internal("map/x_button.jpg"));

        xButtonBounds = new Rectangle(10, Gdx.graphics.getHeight() - 50, 40, 40);
        preloadLandmarks();
    }


    @Override
    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            isMapVisible = !isMapVisible; // Toggle map visibility
        }

        if (isMapVisible && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (xButtonBounds.contains(mouseX, mouseY)) {
                isMapVisible = false; // Close the map if X button is clicked
            }
        }
    }

    // Public method to render the map
    public void drawMap() {
        batch.begin();
        // Draw the map
        batch.draw(mapTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Get player position
        Vector2 playerWorldPosition = gameArea.getPlayer().getPosition();

        // Convert the player's game position to the map coordinates
        Vector2 playerMapPosition = convertGamePositionToMap(playerWorldPosition);

        // Draw the player location icon
        batch.draw(playerLocationTexture, playerMapPosition.x, playerMapPosition.y, 32, 32);

        // Draw all landmarks on the map
        for (Vector2 landmarkPosition : landmarks) {
            Vector2 mapPos = convertGamePositionToMap(landmarkPosition);
            batch.draw(landmarkIconTexture, mapPos.x, mapPos.y, 32, 32);
        }

        // Draw the 'X' button
        batch.draw(xButtonTexture, xButtonBounds.x, xButtonBounds.y, 40, 40);
        batch.end();
    }

    private void preloadLandmarks() {
        landmarks = new ArrayList<>();
        landmarks.add(new Vector2(500, 800));  // Example
        // Add as needed
    }

    private Vector2 convertGamePositionToMap(Vector2 gamePosition) {
        // Assumption: world is 5000x5000 and map is 1024x1024
        float scaleX = Gdx.graphics.getWidth() / 5000f;
        float scaleY = Gdx.graphics.getHeight() / 5000f;

        return new Vector2(gamePosition.x * scaleX, gamePosition.y * scaleY);
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapTexture.dispose();
        playerLocationTexture.dispose();
        landmarkIconTexture.dispose();
        xButtonTexture.dispose();
    }
}
