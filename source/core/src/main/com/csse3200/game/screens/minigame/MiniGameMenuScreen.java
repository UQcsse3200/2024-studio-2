package com.csse3200.game.screens.minigame;

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
import com.csse3200.game.screens.MainMenuScreen;
import com.csse3200.game.screens.minigame.bird.BirdScreen;
import com.csse3200.game.screens.minigame.snake.SnakeScreen;

/**
 * CLass for the MiniGame Menu Screen
 */
public class MiniGameMenuScreen implements Screen {

    private Stage stage;
    private BitmapFont font;
    private final GdxGame game;
    private Skin skin;
    private SpriteBatch batch;

    // Image textures
    private Texture snakeTexture;
    private Texture skyTexture;
    private Texture waterTexture;
    private Texture backgroundTexture;

    /**
     * Constructor initializes the main game object
     * @param game the gdx game
     */
    public MiniGameMenuScreen(GdxGame game) {
        this.game = game;
    }

    /**
     * Called when the screen is shown for the first time
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport()); // Sets up a stage with a viewport
        Gdx.input.setInputProcessor(stage); // Sets the input processor to handle stage events

        batch = new SpriteBatch(); // Initializes SpriteBatch for rendering
        font = new BitmapFont(); // Loads a default font
        font.getData().setScale(2); // Scales the font size

        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json")); // Loads a UI skin

        // Load Textures for background and minigames
        backgroundTexture = new Texture(Gdx.files.internal("images/BackgroundSplashBasic.png"));
        snakeTexture = new Texture(Gdx.files.internal("images/minigames/snakeicon.png"));
        skyTexture = new Texture(Gdx.files.internal("images/minigames/flappybirdicon.png"));
        waterTexture = new Texture(Gdx.files.internal("images/minigames/underwatermazeicon.png"));

        // Create buttons and images for the minigames
        TextButton exitButton = new TextButton("Exit", skin);
        Image snakeImage = new Image(snakeTexture);
        Image skyImage = new Image(skyTexture);
        skyImage.setScale(0.8f); // scale down bird to 80%
        Image waterImage = new Image(waterTexture);
        TextButton snakeButton = new TextButton("Snake", skin);
        TextButton skyButton = new TextButton("Flappy bird", skin);
        TextButton waterButton = new TextButton("Underwater maze", skin);

        // Put images and buttons in table
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Add each image to table
        Table snakeTable = new Table();
        snakeTable.add(snakeImage).bottom().padBottom(10).row();
        snakeTable.add(snakeButton).padTop(10);

        Table skyTable = new Table();
        skyTable.add(skyImage).bottom().padBottom(10).padLeft(80).row();
        skyTable.add(skyButton).padTop(10);

        Table waterTable = new Table();
        waterTable.add(waterImage).bottom().padBottom(10).row();
        waterTable.add(waterButton).padTop(10);

        // Format table
        table.add(snakeTable).bottom().pad(20).space(30);
        table.add(skyTable).bottom().pad(20).space(30);
        table.add(waterTable).bottom().pad(20).space(30);

        stage.addActor(table);

        // Create an exit button table and add it to the stage
        Table exitButtonTable = new Table();
        exitButtonTable.setFillParent(true);
        exitButtonTable.top().right();
        exitButtonTable.add(exitButton).pad(10);
        stage.addActor(exitButtonTable);

        // Add listener for elements, highlight images and buttons and move screen accordingly
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
                game.setScreen(new BirdScreen(game));
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
                game.setScreen(new SnakeScreen(game)); // Switches to the Snake minigame screen
            }
        });

        skyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skyButton.setColor(Color.GREEN);
                game.setScreen(new BirdScreen(game));
            }
        });

        waterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waterButton.setColor(Color.GREEN);
            }
        });
    }

    /**
     * Called every frame to render the screen
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Set the background color
        Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears the screen

        // Draw the background texture
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Update and draw the stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // Handle the Escape key to return to the main menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game)); // Switches to the main menu when ESC is pressed
        }
    }

    /**
     * Called when the screen is resized
     * @param width width fo the new screen
     * @param height height of the new screen
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Updates the viewport to match the new screen dimensions
    }

    /**
     * Called when the game is paused
     */
    @Override
    public void pause() {
    }

    /**
     *  Called when the game is resumed after a pause
     */
    @Override
    public void resume() {
    }

    /**
     * Called when the screen is hidden
     */
    @Override
    public void hide() {
        dispose();
    }

    /**
     *  Called to dispose of resources to prevent memory leaks
     */
    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        skin.dispose();
        snakeTexture.dispose();
        skyTexture.dispose();
        waterTexture.dispose();
        backgroundTexture.dispose();
        batch.dispose();
    }
}
