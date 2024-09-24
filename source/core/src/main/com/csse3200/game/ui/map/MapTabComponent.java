package com.csse3200.game.ui.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.areas.ForestGameArea;

import java.util.ArrayList;
import java.util.List;

/**
 * A component responsible for rendering the full map view when the player presses the map key ('M').
 * This will show the player’s position as well as key landmarks on the map.
 *
 * It supports features like locking certain kingdoms (e.g., Water and Air kingdoms) with a blur effect
 * until they are unlocked. The player's position is drawn on the map along with any defined landmarks.
 * TODO: Add input handler, refine parameters, junit tests
 */
public class MapTabComponent extends UIComponent {

    private SpriteBatch batch;
    private Texture playerIcon;
    private Texture landmarkIcon;
    private Texture mapBackground;
    private int screenWidth;
    private int screenHeight;
    private int mapWidth;
    private int mapHeight;
    private boolean waterKingdomUnlocked = false;
    private boolean airKingdomUnlocked = false;
    private ShapeRenderer shapeRenderer;

    /**
     * Initializes the component, setting up the textures, batch, and initial map settings.
     */
    @Override
    public void create() {
        super.create();

        // Initialise the batch for rendering and ShapeRenderer for blur effects
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Set screen dimensions
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        // Define map dimensions (probably need to adjust)
        mapWidth = 1024;
        mapHeight = 1024;

        playerIcon = new Texture("path/to/image/player.png"); // Player location icon
        landmarkIcon = new Texture("path/to/image/landmark.png"); // Landmark icon
        mapBackground = new Texture("path/to/image/map_background.png"); // Background map image
    }

    /**
     * Retrieves a list of landmarks for the map. This method should eventually be replaced by a
     * more dynamic system for adding landmarks based on game events.
     *
     * @return a list of landmarks represented by entities, each with its own position.
     */
    public List<Entity> getLandmarks() {
        List<Entity> landmarks = new ArrayList<>();

        // EXAMPLE: Creating a static landmark (e.g., boss location)
        Entity forestBoss = new Entity();
        forestBoss.setPosition(100, 100);  // Example coordinates for forest boss location
        landmarks.add(forestBoss);

        // Addition of other landmarks

        return landmarks;
    }

    /**
     * Set whether the water kingdom is unlocked, which determines if a blur effect is applied.
     */
    public void setWaterKingdomUnlocked(boolean unlocked) {
        waterKingdomUnlocked = unlocked;
    }

    /**
     * Set whether the air kingdom is unlocked, which determines if a blur effect is applied.
     */
    public void setAirKingdomUnlocked(boolean unlocked) {
        airKingdomUnlocked = unlocked;
    }

    /**
     * Draws the map, player position, and landmarks, and applies blur effects for locked regions.
     *
     * @param batch The batch used for rendering.
     */
    @Override
    public void draw(SpriteBatch batch) {
        // Draw the base map background
        batch.begin();
        batch.draw(mapBackground, 0, 0, mapWidth, mapHeight);  // Base map
        batch.end();

        // Get the list of landmarks to draw
        List<Entity> landmarks = getLandmarks();

        // Draw landmarks on the map
        batch.begin();
        for (Entity landmark : landmarks) {
            batch.draw(landmarkIcon, landmark.getPosition().x, landmark.getPosition().y, 32, 32);
        }

        // Draw the player's current position on the map
        Vector2 playerPosition = getPlayerPosition();
        if (playerPosition != null) {
            Vector2 mappedPosition = convertGamePositionToMap(playerPosition);
            batch.draw(playerIcon, mappedPosition.x, mappedPosition.y, 32, 32); // Player icon
        }
        batch.end();

        // Apply blur effect to locked Water Kingdom if it is not unlocked
        if (!waterKingdomUnlocked) {
            applyBlurEffect(0, mapHeight / 3, mapWidth, mapHeight / 3);  // Example blur region for water kingdom
        }

        // Apply blur effect to locked Air Kingdom if it is not unlocked
        if (!airKingdomUnlocked) {
            applyBlurEffect(0, (2 * mapHeight) / 3, mapWidth, mapHeight / 3);  // Example blur region for air kingdom
        }
    }

    /**
     * Gets the player's current position in the game world by using the registered ForestGameArea.
     * The player is retrieved from the game area, and their position is fetched.
     *
     * @return The player's position as a Vector2, or null if the player entity is not available.
     */
    private Vector2 getPlayerPosition() {
        // Retrieve the player entity from the ForestGameArea
        ForestGameArea forestGameArea = (ForestGameArea) ServiceLocator.getGameArea();
        Entity player = forestGameArea.getPlayer();
        if (player != null) {
            return player.getPosition(); // Fetch player's current position in the world
        }
        return null;
    }

    /**
     * Converts the player's game world position to the corresponding position on the map.
     * This method scales the player's position from the game world to the map view based on
     * the size of the game world and the map dimensions.
     *
     * @param gamePosition The player's position in the game world.
     * @return The player's position scaled to fit within the map dimensions.
     */
    private Vector2 convertGamePositionToMap(Vector2 gamePosition) {
        // Scale factors between game world size and map size
        float scaleFactorX = mapWidth / 5000f;  // Assuming the game world width is 5000 tiles
        float scaleFactorY = mapHeight / 5000f; // Assuming the game world height is 5000 tiles

        // Scale the game world position to fit on the map
        return new Vector2(gamePosition.x * scaleFactorX, gamePosition.y * scaleFactorY);
    }

    /**
     * Applies a blur effect by rendering a translucent rectangle over a specified region.
     * This effect is used to visually "lock" parts of the map, such as kingdoms that haven't been unlocked yet.
     *
     * @param x The X coordinate of the top-left corner of the blur region.
     * @param y The Y coordinate of the top-left corner of the blur region.
     * @param width The width of the blur region.
     * @param height The height of the blur region.
     */
    private void applyBlurEffect(float x, float y, float width, float height) {
        // Begin shape rendering
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Set the color to a translucent dark overlay for the blur effect
        shapeRenderer.setColor(new Color(0, 0, 0, 0.75f)); // 75% transparency

        // Draw a rectangle over the specified region to create the blur effect
        shapeRenderer.rect(x, y, width, height);

        // End shape rendering
        shapeRenderer.end();
    }

    /**
     * Disposes of all resources used by this component, including textures and the batch.
     */
    @Override
    public void dispose() {
        batch.dispose();
        playerIcon.dispose();
        landmarkIcon.dispose();
        mapBackground.dispose();
        shapeRenderer.dispose();
    }
}
