package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Screen;
import com.csse3200.game.GdxGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BirdyDashScreen implements Screen {
    private static final Logger logger = LoggerFactory.getLogger(BirdyDashScreen.class);
    private final GdxGame game;
    private SpriteBatch batch;
    private Texture background;

    public BirdyDashScreen(GdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("images/minigames/snakebodyvertical.png"));
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin drawing with the SpriteBatch
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

        dispose();
    }

    @Override
    public void dispose() {

        background.dispose();
        batch.dispose();
    }
}

