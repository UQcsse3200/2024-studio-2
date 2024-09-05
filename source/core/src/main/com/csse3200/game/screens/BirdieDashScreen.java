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

    public BirdieDashScreen(GdxGame game) {
        this.game = game;
    }

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

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();


        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
        stage.dispose();
        skin.dispose();
    }
}
