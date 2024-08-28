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

    private Stage stage; // Handles UI elements and input events
    private BitmapFont font; // For rendering fonts
    private final GdxGame game; // Reference to the main game class
    private Skin skin; // Skin for UI styling
    private SpriteBatch batch; // SpriteBatch for rendering textures

    private Texture snakeTexture; // Texture for Snake minigame
    private Texture skyTexture; // Texture for Flappy Bird minigame
    private Texture waterTexture; // Texture for Underwater Maze minigame

    private Texture backgroundTexture; // Background texture for the menu screen

    // Constructor initializes the main game object
    public MiniGameMenuScreen(GdxGame game) {
        this.game = game;
    }

    // Called when the screen is shown for the first time
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
        Image waterImage = new Image(waterTexture);
        TextButton snakeButton = new TextButton("Snake", skin);
        TextButton skyButton = new TextButton("Flappy bird", skin);
        TextButton waterButton = new TextButton("Underwater maze", skin);

        // Table layout for organizing buttons and images
        Table table = new Table();
        table.setFillParent(true); // Makes the table take up the entire screen
        table.center(); // Centers the table on the screen

        // Set up separate tables for each minigame's image and button
        // Set up separate tables for each minigame's image and button with consistent padding
        Table snakeTable = new Table();
        snakeTable.add(snakeImage).padBottom(10).row(); // Image padding from the bottom
        snakeTable.add(snakeButton).padTop(10); // Button padding from the top

        Table skyTable = new Table();
        skyTable.add(skyImage).padBottom(10).row(); // Image padding from the bottom
        skyTable.add(skyButton).padTop(10); // Button padding from the top

        Table waterTable = new Table();
        waterTable.add(waterImage).padBottom(10).row(); // Image padding from the bottom
        waterTable.add(waterButton).padTop(10); // Button padding from the top

// Add all game tables to the main table with consistent spacing between columns
        table.add(snakeTable).pad(20).space(30); // Padding and space between columns
        table.add(skyTable).pad(20).space(30);   // Padding and space between columns
        table.add(waterTable).pad(20).space(30); // Padding and space between columns


        // Add the main table to the stage
        stage.addActor(table);

        // Create an exit button table and add it to the stage
        Table exitButtonTable = new Table();
        exitButtonTable.setFillParent(true); // Makes the exit button table take up the entire screen
        exitButtonTable.top().right(); // Positions the exit button in the top-right corner
        exitButtonTable.add(exitButton).pad(10); // Adds padding around the exit button
        stage.addActor(exitButtonTable); // Adds the exit button to the stage

        // Add click listeners for buttons and images to handle user interaction
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game)); // Switches to the main menu screen when Exit is clicked
            }
        });

        snakeImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                snakeImage.setColor(Color.GREEN); // Highlights the Snake image
                game.setScreen(new SnakeScreen(game)); // Switches to the Snake minigame screen
            }
        });

        skyImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skyImage.setColor(Color.GREEN); // Highlights the Flappy Bird image
            }
        });

        waterImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waterImage.setColor(Color.GREEN); // Highlights the Underwater Maze image
            }
        });

        snakeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                snakeButton.setColor(Color.GREEN); // Highlights the Snake button
                game.setScreen(new SnakeScreen(game)); // Switches to the Snake minigame screen
            }
        });

        skyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skyButton.setColor(Color.GREEN); // Highlights the Flappy Bird button
            }
        });

        waterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waterButton.setColor(Color.GREEN); // Highlights the Underwater Maze button
            }
        });
    }

    // Called every frame to render the screen
    @Override
    public void render(float delta) {
        // Set the background color
        Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f); // A light yellow color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears the screen

        // Draw the background texture
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Draws the background image filling the screen
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime()); // Updates the stage
        stage.draw(); // Draws the stage

        // Handle the Escape key to return to the main menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game)); // Switches to the main menu when ESC is pressed
        }
    }

    // Called when the screen is resized
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Updates the viewport to match the new screen dimensions
    }

    // Called when the game is paused
    @Override
    public void pause() {
    }

    // Called when the game is resumed after a pause
    @Override
    public void resume() {
    }

    // Called when the screen is hidden
    @Override
    public void hide() {
        dispose(); // Disposes of resources when the screen is no longer visible
    }

    // Called to dispose of resources to prevent memory leaks
    @Override
    public void dispose() {
        stage.dispose(); // Disposes of the stage
        font.dispose(); // Disposes of the font
        skin.dispose(); // Disposes of the skin
        snakeTexture.dispose(); // Disposes of the snake texture
        skyTexture.dispose(); // Disposes of the sky texture
        waterTexture.dispose(); // Disposes of the water texture
        backgroundTexture.dispose(); // Disposes of the background texture
        batch.dispose(); // Disposes of the SpriteBatch
    }
}
