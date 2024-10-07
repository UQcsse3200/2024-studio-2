package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;

/**
 * Screen that handles the display of an enemy transition cutscene. Manages rendering the background
 * and UI elements, and acts as the entry point for the cutscene when triggered.
 * This class extends ScreenAdapter, which provides basic screen lifecycle methods.
 */
public class EnemyTransitionCutSceneScreen extends ScreenAdapter {
    private final GdxGame game;
    private final Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private SpriteBatch batch;

    /**
     * Constructor for EnemyTransitionCutSceneScreen. Initializes the screen with necessary resources,
     * including the stage and background texture.
     *
     * @param game The main game instance controlling overall game logic.
     */
    public EnemyTransitionCutSceneScreen(GdxGame game) {
        this.game = game;
        this.stage = ServiceLocator.getRenderService().getStage(); // Obtain stage from the render service
        this.batch = new SpriteBatch(); // Create a new SpriteBatch for rendering the background
        initializeSkin(); // Load or initialize a UI skin for use with scene elements
        initializeBackground(); // Load the background texture
    }

    /**
     * Initializes the skin used for UI components.
     */
    private void initializeSkin() {
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json")); // Load the skin file
    }

    /**
     * Initializes the background texture used in the cutscene. Loads the texture from a file.
     */
    private void initializeBackground() {
        backgroundTexture = new Texture(Gdx.files.internal("images/BackgroundSplashBasic.png"));
    }

    /**
     * Called when this screen becomes the current screen for the game. Can be used to set up the scene.
     */
    @Override
    public void show() {
    }

    /**
     * Renders the screen by clearing the previous frame and drawing the background and UI elements.
     * This method is called repeatedly at a fixed frame rate.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        // Begin drawing the background texture
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Draw the background
        batch.end();

        // Update the stage and render any UI elements
        stage.act(delta);
        stage.draw();
    }

    /**
     * Resizes the screen, adjusting the viewport of the stage to match the new width and height.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Disposes of resources when this screen is no longer needed.
     */
    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        batch.dispose();
    }
}
