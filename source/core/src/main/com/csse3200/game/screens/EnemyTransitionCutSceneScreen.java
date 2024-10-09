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
     */
    public EnemyTransitionCutSceneScreen(GdxGame game) {
        this.game = game;
        this.stage = ServiceLocator.getRenderService().getStage();
        this.batch = new SpriteBatch();
        initializeSkin();
        initializeBackground();
    }

    /**
     * Resizes the screen, adjusting the viewport of the stage to match the new width and height.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Disposes of resources when screen is no longer needed.
     */
    @Override
    public void dispose() {
        // Dispose of  resources
        stage.dispose();
        backgroundTexture.dispose();
        batch.dispose();
        skin.dispose();
    }

    private void initializeSkin() {
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
    }

    private void initializeBackground() {
        backgroundTexture = new Texture(Gdx.files.internal("images/BackgroundSplashBasic.png"));
    }

    @Override
    public void show() {
        // This method is called when this screen becomes the current screen
    }

    @Override
    public void render(float delta) {
        // Begin drawing the background texture
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }
}

