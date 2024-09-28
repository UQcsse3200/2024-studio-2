package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays the player icon and a larger minimap frame based on the player's image corresponding to its kingdom.
 * Also handles rendering the secondary minimap view using a different camera.
 */
public class GameAreaDisplay extends UIComponent {
    private final String gameAreaName;
    private Label title;
    private Texture playerIconTexture;
    private Texture minimapFrameTexture;
    private final OrthographicCamera mainCamera;
    private final CameraComponent minimapCameraComponent; // Secondary camera for minimap

    /**
     * Constructs a GameAreaDisplay component.
     *
     * @param gameAreaName           the name of the current game area to be displayed
     * @param batch                  the SpriteBatch used for rendering
     * @param mainCamera             the main game camera
     * @param minimapCameraComponent the component providing the minimap camera
     */
    public GameAreaDisplay(String gameAreaName, SpriteBatch batch, OrthographicCamera mainCamera, CameraComponent minimapCameraComponent) {
        this.gameAreaName = gameAreaName != null ? gameAreaName : "Default Area"; // Provide a default name if null
        this.mainCamera = mainCamera;
        this.minimapCameraComponent = minimapCameraComponent;
    }

    /**
     * Initializes the GameAreaDisplay component and calls addActors() to add actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Adds the title label, player icon, and minimap frame to the UI using a table layout.
     * The player icon and minimap frame are determined based on the selected player's image path,
     * which corresponds to its kingdom.
     */
    private void addActors() {
        title = new Label(this.gameAreaName, skin, "large");

        // Get the player image path from GameState
        String playerImagePath = GameState.player.selectedAnimalPath;

        // Load the appropriate textures based on the player image path
        try {
            switch (playerImagePath) {
                case "images/dog.png" -> loadTextures("forest");
                case "images/croc.png" -> loadTextures("sea");
                case "images/bird.png" -> loadTextures("sky");
                default -> loadTextures("forest"); // Default to forest textures
            }
        } catch (Exception e) {
            Gdx.app.error("GameAreaDisplay", "Error loading textures: " + e.getMessage());
            // Optionally set default textures or handle the error further
            loadTextures("forest"); // Load default textures on error
        }

        // Create images for the player icon and the minimap frame
        Image playerIcon = new Image(playerIconTexture);
        Image minimapFrame = new Image(minimapFrameTexture);

        // Set the size of the icons
        float titleHeight = title.getPrefHeight();
        float playerIconScaleFactor = 5f;   // Size for the player icon
        float minimapScaleFactor = 10f;      // Larger size for the minimap frame

        playerIcon.setSize(titleHeight * playerIconScaleFactor, titleHeight * playerIconScaleFactor);
        minimapFrame.setSize(titleHeight * minimapScaleFactor, titleHeight * minimapScaleFactor);

        // Create a table for the top UI
        Table topTable = new Table();
        topTable.setFillParent(true);
        topTable.top().left();

        // Add the player icon to the left side of the table
        topTable.add(playerIcon)
                .size(titleHeight * playerIconScaleFactor, titleHeight * playerIconScaleFactor)
                .align(Align.left | Align.top)
                .pad(15); // Padding from the edges

        // Add space to push the title and minimap frame to their respective locations
        topTable.add().expandX();

        // Add the title in the center
        topTable.add(title).align(Align.center | Align.top).pad(10);

        // Add space between the title and the minimap frame
        topTable.add().expandX();

        // Add the minimap frame to the right side of the table
        topTable.add(minimapFrame)
                .size(titleHeight * minimapScaleFactor, titleHeight * minimapScaleFactor)
                .align(Align.right | Align.top)
                .pad(30); // Padding from the edges

        // Add the table to the stage
        stage.addActor(topTable);
    }

    /**
     * Loads the textures for the player icon and minimap frame based on the specified type.
     *
     * @param type the type of textures to load (forest, sea, sky)
     */
    private void loadTextures(String type) {
        playerIconTexture = new Texture(Gdx.files.internal("images/player_icon_" + type + ".png"));
        minimapFrameTexture = new Texture(Gdx.files.internal("images/player_icon_" + type + ".png")); // Different texture for minimap frame
    }

    /**
     * Draws the UI component on the screen, including the main game area and the minimap.
     *
     * @param batch the SpriteBatch used to draw the component
     */
    @Override
    public void draw(SpriteBatch batch) {
        // Render main game view
        renderMainView(batch);

        // Render minimap view
        renderMinimapView(batch);

        // Draw UI elements (player icon, title, minimap frame)
        // Draw the stage which includes the UI components
    }

    /**
     * Renders the main game view using the main camera.
     *
     * @param batch the SpriteBatch used for rendering
     */
    private void renderMainView(SpriteBatch batch) {
        // Set the batch to the main camera's projection matrix
        batch.setProjectionMatrix(mainCamera.combined);

        // Insert your game area rendering logic here
        // Example: batch.draw(texture, x, y, width, height);
    }

    /**
     * Renders the minimap view using the minimap camera.
     *
     * @param batch the SpriteBatch used for rendering
     */
    private void renderMinimapView(SpriteBatch batch) {
        // Get the minimap camera from the CameraComponent
        OrthographicCamera minimapCamera = (OrthographicCamera) minimapCameraComponent.getCamera();

        // Set the batch to the minimap camera's projection matrix
        batch.setProjectionMatrix(minimapCamera.combined);

        // Insert your minimap rendering logic here
        // Example: batch.draw(minimapTexture, x, y, width, height);
    }

    /**
     * Disposes of assets used by this component, including the player icon texture and the title label.
     */
    @Override
    public void dispose() {
        super.dispose();
        if (playerIconTexture != null) {
            playerIconTexture.dispose();
        }
        if (minimapFrameTexture != null) {
            minimapFrameTexture.dispose();
        }
        title.remove();
    }
}
