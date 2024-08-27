package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;

public class MiniGameMenuScreen implements Screen {

    private Stage stage;
    private BitmapFont font;
    private final GdxGame game;
    private Skin skin;
    private SpriteBatch batch;

    private Texture snakeTexture;
    private Texture skyTexture;
    private Texture waterTexture;

    // New: Background Texture
    private Texture backgroundTexture;

    public MiniGameMenuScreen(GdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();  // Create SpriteBatch for rendering
        font = new BitmapFont();
        font.getData().setScale(2);

        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Load Textures
        backgroundTexture = new Texture(Gdx.files.internal("images/BackgroundSplash.png")); // Path to your background image
        snakeTexture = new Texture(Gdx.files.internal("images/minigames/Snake.png"));
        skyTexture = new Texture(Gdx.files.internal("images/minigames/Flappy_bird.png"));
        waterTexture = new Texture(Gdx.files.internal("images/minigames/Underwater_maze.png"));

        TextButton exitButton = new TextButton("Exit", skin);

        Image snakeImage = new Image(snakeTexture);
        Image skyImage = new Image(skyTexture);
        Image waterImage = new Image(waterTexture);

        TextButton snakeButton = new TextButton("Snake", skin);
        TextButton skyButton = new TextButton("Flappy bird", skin);
        TextButton waterButton = new TextButton("Underwater maze", skin);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

// Create a cell for the snake image and button
        Table snakeTable = new Table();
        snakeTable.add(snakeImage).padBottom(10).row();
        snakeTable.add(snakeButton).padBottom(10);

// Create a cell for the sky image and button
        Table skyTable = new Table();
        skyTable.add(skyImage).padBottom(10).row();
        skyTable.add(skyButton).padBottom(10);

// Create a cell for the water image and button
        Table waterTable = new Table();
        waterTable.add(waterImage).padBottom(10).row();
        waterTable.add(waterButton).padBottom(10);

// Add all the image/button pairs to the main table in a single row
        table.add(snakeTable).pad(20);
        table.add(skyTable).pad(20);
        table.add(waterTable).pad(20);

// Add the table to the stage
        stage.addActor(table);




        Table exitButtonTable = new Table();
        exitButtonTable.setFillParent(true);
        exitButtonTable.top().right();

        exitButtonTable.add(exitButton).pad(10);
        stage.addActor(exitButtonTable);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        snakeImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                snakeImage.setColor(Color.GREEN);
                game.setScreen(new SnakeScreen(game));
            }
        });

        skyImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skyImage.setColor(Color.GREEN);
            }
        });

        waterImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waterImage.setColor(Color.GREEN);
            }
        });

        snakeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                snakeButton.setColor(Color.GREEN);
                game.setScreen(new SnakeScreen(game));
            }
        });

        skyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skyButton.setColor(Color.GREEN);
            }
        });

        waterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waterButton.setColor(Color.GREEN);
            }
        });
    }

    @Override
    public void render(float delta) {
        // Set background color
        Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background image
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Draws background
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        font.dispose();
        skin.dispose();
        snakeTexture.dispose();
        skyTexture.dispose();
        waterTexture.dispose();
        backgroundTexture.dispose();  // Dispose of the background texture
        batch.dispose();  // Dispose of the SpriteBatch
    }
}
