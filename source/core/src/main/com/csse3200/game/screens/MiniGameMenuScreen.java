package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;

/**
 * CLass for the MiniGame Menu Screen
 */
public class MiniGameMenuScreen implements Screen {

    private Stage stage;
    private final GdxGame game;
    private Skin skin;
    private SpriteBatch batch;
    private float scale;

    // Image textures
    private Texture snakeTexture;
    private Texture skyTexture;
    private Texture waterTexture;
    private Texture backgroundTexture;

    public MiniGameMenuScreen(GdxGame game) {
        this.game = game;
        this.scale = 1;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();

        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Load Textures for background and minigames
        backgroundTexture = new Texture(Gdx.files.internal("images/BackgroundSplashBasic.png"));
        snakeTexture = new Texture(Gdx.files.internal("images/minigames/snakeicon.png"));
        skyTexture = new Texture(Gdx.files.internal("images/minigames/flappybirdicon.png"));
        waterTexture = new Texture(Gdx.files.internal("images/minigames/underwatermazeicon.png"));

        // Create buttons and images for the minigames
        TextButton exitButton = new TextButton("Exit", skin);
        Image snakeImage = new Image(snakeTexture);
        snakeImage.setScale(scale);
        Image skyImage = new Image(skyTexture);
        skyImage.setScale(0.8f * scale); // scale down bird to 80%
        Image waterImage = new Image(waterTexture);
        waterImage.setScale(scale);
        TextButton snakeButton = new TextButton("Snake", skin);
        snakeButton.getLabel().setFontScale(scale);
        TextButton skyButton = new TextButton("Flappy bird", skin);
        skyButton.getLabel().setFontScale(scale);
        TextButton waterButton = new TextButton("Underwater maze", skin);
        waterButton.getLabel().setFontScale(scale);
        snakeButton.setSize(snakeButton.getWidth() * scale, snakeButton.getHeight() * scale);
        skyButton.setSize(skyButton.getWidth() * scale, skyButton.getHeight() * scale);
        waterButton.setSize(waterButton.getWidth() * scale, waterButton.getHeight() * scale);

        // Position elements manually
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Determine the Y position for all images (vertical alignment)
        float centerY = screenHeight / 3f;

        // Calculate the X positions based on the screen width
        float snakeX = screenWidth * 0.2f - (snakeImage.getWidth() * scale) / 2;
        float skyX = screenWidth * 0.5f - (skyImage.getWidth() * 0.8f * scale) / 2;
        float waterX = screenWidth * 0.8f - (waterImage.getWidth() * scale) / 2;

        // Set positions for the images
        snakeImage.setPosition(snakeX, centerY);
        skyImage.setPosition(skyX - 50*scale, centerY);
        waterImage.setPosition(waterX, centerY);


        // Set positions for the buttons consistently below the images
        // With some manual adjustments to x position
        snakeButton.setPosition(snakeX + 80 *scale, 200 * scale);
        skyButton.setPosition(skyX + 20 * scale, 200 * scale);
        waterButton.setPosition(waterX + 100 * scale, 200 * scale);


        // Position the exit button
        exitButton.setPosition(screenWidth - exitButton.getWidth() - 10 * scale, screenHeight - exitButton.getHeight() - 10 * scale);
        exitButton.setSize(exitButton.getWidth() * scale, exitButton.getHeight() * scale);
        exitButton.getLabel().setFontScale(scale);


        // Add actors to the stage
        stage.addActor(snakeImage);
        stage.addActor(snakeButton);
        stage.addActor(skyImage);
        stage.addActor(skyButton);
        stage.addActor(waterImage);
        stage.addActor(waterButton);
        stage.addActor(exitButton);

        // Add listener for elements
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //dispose();
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        snakeImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                snakeImage.setColor(Color.GREEN);
                game.enterSnakeScreen();
            }
        });

        skyImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skyImage.setColor(Color.GREEN);
                game.setScreen(new BirdyDashScreen(game));
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
                game.enterSnakeScreen(); // Switches to the Snake minigame screen
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            game.setScreen(new MainMenuScreen(game)); // Switches to the main menu when ESC is pressed
        }
    }

    @Override
    public void resize(int width, int height) {
        // Update the stage viewport
        stage.getViewport().update(width, height, true);
        float baseWidth = 1920f;
        float baseHeight = 1200f;
        float scaleWidth = width / baseWidth;
        float scaleHeight = height / baseHeight;
        scale = Math.min(scaleWidth, scaleHeight);
        show();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        //dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        snakeTexture.dispose();
        skyTexture.dispose();
        waterTexture.dispose();
        backgroundTexture.dispose();
        batch.dispose();
    }
}
