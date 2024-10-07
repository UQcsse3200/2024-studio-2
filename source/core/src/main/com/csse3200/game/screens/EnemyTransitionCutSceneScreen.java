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

public class EnemyTransitionCutSceneScreen extends ScreenAdapter {
    private final GdxGame game;
    private final Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private SpriteBatch batch; // SpriteBatch for rendering textures

    public EnemyTransitionCutSceneScreen(GdxGame game) {
        this.game = game;
        this.stage = ServiceLocator.getRenderService().getStage();
        this.batch = new SpriteBatch(); // Initialize the SpriteBatch
        initializeSkin(); // Load or initialize a skin for UI components
        initializeBackground(); // Initialize the background texture
    }

    private void initializeSkin() {
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
    }

    private void initializeBackground() {

        backgroundTexture = new Texture(Gdx.files.internal("images/BackgroundSplashBasic.png"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Start drawing the background texture
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        // Dispose of any resources
        stage.dispose();
        backgroundTexture.dispose(); // Dispose of the background texture
        batch.dispose(); // Dispose of the SpriteBatch
    }
}
