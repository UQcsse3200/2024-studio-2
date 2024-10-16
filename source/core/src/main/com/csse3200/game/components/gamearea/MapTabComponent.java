package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Component responsible for displaying and handling the game map when the player presses 'M'.
 * Displays the map texture, player location, landmarks and a legend.
 * The map maintains its aspect ratio and adjusts according to window size (sometimes).
 *
 * Additionally, splits the map into three equal horizontal sections representing grass (bottom),
 * ocean (middle), and sky (top). Applies a blur effect to ocean and sky areas by default,
 * which can be toggled via (not yet implemented).
 */
public class MapTabComponent extends Component {
    // Textures
    private Texture mapTexture;
    private Texture playerLocationTexture;
    private Texture landmarkIconTexture;
    private Texture xButtonTexture;
    private Texture northSymbolTexture;
    private Texture whitePixelTexture;

    private SpriteBatch batch;
    public boolean isMapVisible = false;
    private Rectangle xButtonBounds;
    private List<Vector2> landmarks;
    private final GameArea gameArea;

    // Font for drawing text
    private BitmapFont font;
    private GlyphLayout layout;

    // Constants for UI layout
    private static final float TOP_BAR_HEIGHT = 50;
    private static final float LEGEND_WIDTH = 200;

    // Boolean flags to control blur effect on locked areas
    private boolean blurOcean = true;
    private boolean blurSky = true;

    /**
     * Constructs a MapTabComponent associated with a specific GameArea.
     *
     * @param gameArea The GameArea to which this map component belongs.
     */
    public MapTabComponent(GameArea gameArea) {
        this.gameArea = gameArea;
    }

    /**
     * Initialises the component, loading textures, fonts, and setting up initial data.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        mapTexture = new Texture(Gdx.files.internal("map/MAP.png"));
        playerLocationTexture = new Texture(Gdx.files.internal("map/Lion_Icon.png"));
        landmarkIconTexture = new Texture(Gdx.files.internal("map/landmark_icon.png"));
        xButtonTexture = new Texture(Gdx.files.internal("map/x_button.png"));
        northSymbolTexture = new Texture(Gdx.files.internal("map/north_symbol.png"));

        // Create a 1x1 white pixel texture for drawing rectangles
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixelTexture = new Texture(pixmap);
        pixmap.dispose();

        // Initialise font and layout for text drawing
        font = new BitmapFont();
        layout = new GlyphLayout();

        // Initialise xButtonBounds; position will be updated in update()
        xButtonBounds = new Rectangle(10, Gdx.graphics.getHeight() - TOP_BAR_HEIGHT + (TOP_BAR_HEIGHT - 40) / 2, 40, 40);

        preloadLandmarks();
    }

    /**
     * Handles input to toggle map visibility and close the map.
     * Also updates positions in case of window resize.
     */
    @Override
    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            isMapVisible = !isMapVisible; // Toggle map visibility
        }

        // Update xButtonBounds position in case of window resize
        xButtonBounds.setPosition(10, Gdx.graphics.getHeight() - TOP_BAR_HEIGHT + (TOP_BAR_HEIGHT - xButtonBounds.height) / 2);

        if (isMapVisible && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (xButtonBounds.contains(mouseX, mouseY)) {
                isMapVisible = false; // Close the map if X button is clicked
            }
        }
    }

    /**
     * Renders the map on the screen, including:
     * - The map texture.
     * - Player's current location.
     * - Landmarks.
     * - Legend displaying icons and their meanings.
     * - A north symbol indicating the map orientation.
     * - A top bar with the map title.
     *
     * The map maintains its aspect ratio and adjusts according to the current window size.
     * If blurOcean or blurSky are enabled, respective areas of the map are overlaid with a blur effect.
     */
    public void drawMap() {
        batch.begin();
        // Get the aspect ratio of the map texture
        float mapAspectRatio = (float) mapTexture.getWidth() / mapTexture.getHeight();

        // Calculate available width and height for the map
        float availableWidth = Gdx.graphics.getWidth() - LEGEND_WIDTH;
        float availableHeight = Gdx.graphics.getHeight() - TOP_BAR_HEIGHT;

        // Calculate the maximum width and height for the map while maintaining aspect ratio
        float mapWidth, mapHeight;

        if (availableWidth / availableHeight > mapAspectRatio) {
            // Available area is wider than the map aspect ratio, so limit by height
            mapHeight = availableHeight;
            mapWidth = mapHeight * mapAspectRatio;
        } else {
            // Available area is taller than the map aspect ratio, so limit by width
            mapWidth = availableWidth;
            mapHeight = mapWidth / mapAspectRatio;
        }

        // Position the map so that it is below the top bar and to the left of the legend, centered horizontally
        float mapX = (availableWidth - mapWidth) / 2;
        float mapY = 0;

        // Draw the map texture
        batch.draw(mapTexture, mapX, mapY, mapWidth, mapHeight);

        // Calculate the height of each area
        float areaHeight = mapHeight / 3f;

        // Define rectangles for ocean and sky areas
        Rectangle oceanArea = new Rectangle(mapX, mapY + areaHeight, mapWidth, areaHeight);
        Rectangle skyArea = new Rectangle(mapX, mapY + 2 * areaHeight, mapWidth, areaHeight);

        // Draw blur effect over ocean and sky if respective flags are true
        if (blurOcean) {
            batch.setColor(1f, 1f, 1f, 0.7f);
            batch.draw(whitePixelTexture, oceanArea.x, oceanArea.y, oceanArea.width, oceanArea.height);
        }

        if (blurSky) {
            batch.setColor(1f, 1f, 1f, 0.7f);
            batch.draw(whitePixelTexture, skyArea.x, skyArea.y, skyArea.width, skyArea.height);
        }

        // Reset colour
        batch.setColor(Color.WHITE);

        // Get player position
        Vector2 playerWorldPosition = gameArea.getPlayer().getPosition();

        // Convert the player's game position to the map coordinates
        Vector2 playerMapPosition = convertGamePositionToMap(playerWorldPosition, mapX, mapY, mapWidth, mapHeight);

        // Draw the player location icon (centered on position)
        batch.draw(playerLocationTexture, playerMapPosition.x - 16, playerMapPosition.y - 16, 32, 32);

        // Draw all landmarks on the map
        for (Vector2 landmarkPosition : landmarks) {
            Vector2 mapPos = convertGamePositionToMap(landmarkPosition, mapX, mapY, mapWidth, mapHeight);
            batch.draw(landmarkIconTexture, mapPos.x - 16, mapPos.y - 16, 32, 32);
        }

        // Draw the 'X' button
        batch.draw(xButtonTexture, xButtonBounds.x, xButtonBounds.y, xButtonBounds.width, xButtonBounds.height);

        // Draw the north symbol at the top right
        float northSymbolWidth = 50;
        float northSymbolHeight = 50;
        float northSymbolX = mapX + mapWidth - northSymbolWidth - 10;
        float northSymbolY = mapY + mapHeight - northSymbolHeight - 10; // 10 pixels from top edge of map
        batch.draw(northSymbolTexture, northSymbolX, northSymbolY, northSymbolWidth, northSymbolHeight);

        // Draw the legend area on the right
        float legendX = Gdx.graphics.getWidth() - LEGEND_WIDTH;
        float legendY = 0;

        // Semi-transparent background for legend
        batch.setColor(0f, 0f, 0f, 0.5f); // Black with 50% opacity
        batch.draw(whitePixelTexture, legendX, legendY, LEGEND_WIDTH, Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE); // Reset colour

        // Positions for icons and text in legend
        float iconSize = 32;
        float padding = 20;
        float textOffsetX = iconSize + 10;

        // First icon (Player)
        float icon1X = legendX + padding;
        float icon1Y = Gdx.graphics.getHeight() - padding - iconSize - TOP_BAR_HEIGHT;

        // Second icon (Landmark)
        float icon2X = icon1X;
        float icon2Y = icon1Y - iconSize - padding;

        // Draw player icon
        batch.draw(playerLocationTexture, icon1X, icon1Y, iconSize, iconSize);

        // Draw text "Player" next to the icon
        layout.setText(font, "Player");
        font.draw(batch, "Player", icon1X + textOffsetX, icon1Y + iconSize / 2 + layout.height / 2);

        // Draw landmark icon
        batch.draw(landmarkIconTexture, icon2X, icon2Y, iconSize, iconSize);

        // Draw text "Landmark" next to the icon
        layout.setText(font, "Landmark");
        font.draw(batch, "Landmark", icon2X + textOffsetX, icon2Y + iconSize / 2 + layout.height / 2);

        // Draw the top bar with text in the middle
        batch.setColor(0f, 0f, 0f, 0.5f);
        batch.draw(whitePixelTexture, 0, Gdx.graphics.getHeight() - TOP_BAR_HEIGHT, Gdx.graphics.getWidth() - LEGEND_WIDTH, TOP_BAR_HEIGHT);
        batch.setColor(Color.WHITE);

        String topBarText = "Map";
        layout.setText(font, topBarText);
        float textWidth = layout.width;
        float textHeight = layout.height;

        float textX = (Gdx.graphics.getWidth() - LEGEND_WIDTH - textWidth) / 2;
        float textY = Gdx.graphics.getHeight() - (TOP_BAR_HEIGHT - textHeight) / 2 - 10;

        font.draw(batch, topBarText, textX, textY);

        batch.draw(xButtonTexture, xButtonBounds.x, xButtonBounds.y, xButtonBounds.width, xButtonBounds.height);

        batch.end();
    }

    /**
     * Preloads landmark positions to be displayed on the map.
     * This method initialises the list of landmarks that will be shown on the map.
     * Each landmark is represented by a Vector2 position in the game world coordinates.
     */
    private void preloadLandmarks() {
        landmarks = new ArrayList<>();
        landmarks.add(new Vector2(50, 80));  // Example landmark position
        // Add more landmarks as needed
    }

    /**
     * Converts a position from game world coordinates to map screen coordinates.
     * This method maps the game world position to the corresponding position on the map texture as it is displayed on screen.
     *
     * @param gamePosition The position in the game world (e.g., player's position or a landmark).
     * @param mapX         The x-coordinate where the map is drawn on the screen.
     * @param mapY         The y-coordinate where the map is drawn on the screen.
     * @param mapWidth     The width of the map as it is displayed on the screen.
     * @param mapHeight    The height of the map as it is displayed on the screen.
     * @return The corresponding position on the map as it appears on the screen.
     */
    private Vector2 convertGamePositionToMap(Vector2 gamePosition, float mapX, float mapY, float mapWidth, float mapHeight) {
        // Assuming world is 5000x5000
        float worldWidth = 175f;
        float worldHeight = 175f;

        float normalizedX = gamePosition.x / worldWidth;
        float normalizedY = gamePosition.y / worldHeight;

        float mapPosX = mapX + normalizedX * mapWidth;
        float mapPosY = mapY + normalizedY * mapHeight;

        return new Vector2(mapPosX, mapPosY);
    }

    /**
     * Sets whether to apply a blur effect on the ocean area of the map.
     *
     * @param blur True to apply blur effect to the ocean, false to display the ocean normally.
     */
    public void setBlurOcean(boolean blur) {
        this.blurOcean = blur;
    }

    /**
     * Sets whether to apply a blur effect on the sky area of the map.
     *
     * @param blur True to apply blur effect to the sky, false to display the sky normally.
     */
    public void setBlurSky(boolean blur) {
        this.blurSky = blur;
    }

    /**
     * Disposes of all resources used by this component, including textures, batch, and fonts.
     * This method should be called when the component is no longer needed to free up resources.
     */
    @Override
    public void dispose() {
        batch.dispose();
        mapTexture.dispose();
        playerLocationTexture.dispose();
        landmarkIconTexture.dispose();
        xButtonTexture.dispose();
        northSymbolTexture.dispose();
        whitePixelTexture.dispose();
        font.dispose();
    }
}
