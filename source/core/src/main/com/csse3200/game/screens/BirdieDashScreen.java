package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BirdieDashScreen implements Screen {
    private static final Logger logger = LoggerFactory.getLogger(BirdieDashScreen.class);
    private final GdxGame game;
    private SpriteBatch batch;
    private Texture background;
    private Stage stage;
    private Skin skin;
    private TextButton exitButton;

    /**
     * Constructor for BirdieDashScreen.
     * @param game The game object to manage screens.
     */
    public BirdieDashScreen(GdxGame game) {
        this.game = game;
    }

    /**
     * Called when this screen becomes the current screen.
     */
    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("images/minigames/snakebodyvertical.png"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        exitButton = new TextButton("Exit", skin);
        exitButton.setSize(100, 50);
        exitButton.setPosition(Gdx.graphics.getWidth() - exitButton.getWidth() - 10, Gdx.graphics.getHeight() - exitButton.getHeight() - 10);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                logger.info("Exit button clicked!");
                game.setScreen(new MiniGameMenuScreen(game));
            }
        });

        stage.addActor(exitButton);
    }

    /**
     * Called every frame to render the screen.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    /**
     * Called when the screen is resized.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Called when the application is paused.
     */
    @Override
    public void pause() {}

    /**
     * Called when the application is resumed.
     */
    @Override
    public void resume() {}

    /**
     * Called when this screen is no longer the current screen.
     */
    @Override
    public void hide() {
        dispose();
    }

    /**
     * Cleans up resources when the screen is disposed.
     */
    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
        stage.dispose();
        skin.dispose();
    }
}
