package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
    private SnakePopup helpWindow;

    private Texture backgroundTexture;

    public MiniGameMenuScreen(GdxGame game) {
        this.game = game;
        this.scale = 1;
    }

    /**
     * Sets up the images and buttons. This is called when the screen is made and resized.
     */
    @Override
    public void show() {

        // Set up the stage and sprite bach for background
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        backgroundTexture = new Texture(Gdx.files.internal("images/BackgroundSplashBasic.png"));

        // Setup UI elements including all images and buttons

        // Snake image and button
        setupMinigameUI("Snake", "images/minigames/snakeicon.png", 0.2f, 1, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                helpWindow = new SnakePopup("images/minigames/BirdieDashPopUp.png");
                helpWindow.show();
            }
        });

        // Bird image and button
        setupMinigameUI("Flappy bird", "images/minigames/flappybirdicon.png", 0.5f, 0.8f, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.enterBirdieDashScreen();
            }
        });

        // Fish Image and button
        setupMinigameUI("Underwater maze", "images/minigames/underwatermazeicon.png", 0.8f, 1, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.enterMazeGameScreen();
            }
        });

        // Exit button
        TextButton exitButton = createButton("Exit", scale, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        exitButton.setPosition(Gdx.graphics.getWidth() - exitButton.getWidth() - 10 * scale, Gdx.graphics.getHeight() - exitButton.getHeight() - 10 * scale);
        stage.addActor(exitButton);
    }


    /**
     * Sets up the image for each mini-game and the button underneath
     * @param buttonText the text to go on the button
     * @param texturePath the image texture
     * @param xPositionFactor the image position on the x coordinate
     * @param imageScale to scale the image up or down (and match the other two)
     * @param listener the listener to attach the button and image to (that boots up the mini-game)
     */
    private void setupMinigameUI(String buttonText, String texturePath, float xPositionFactor, float imageScale, ClickListener listener) {
        // Load the texture for the image
        Texture texture = new Texture(Gdx.files.internal(texturePath));

        // Create the image with scaling
        Image image = createImage(texture, imageScale);
        TextButton button = createButton(buttonText, scale, listener);

        // Calculate screen positions
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float centerY = screenHeight / 3f;

        // Adjust the X position based on the scaled width of the image
        float xPos = screenWidth * xPositionFactor - (image.getWidth()) / 2;

        // Set positions for the image and button
        image.setPosition(xPos, centerY);
        button.setPosition(xPos + 80 * scale, 200 * scale);

        // Add both image and button to the stage
        stage.addActor(image);
        stage.addActor(button);

        // Add the click listener to the image as well
        image.addListener(listener);
    }

    /**
     * Makes the images from a texture
     * @param texture the texture for the image to be made from
     * @param imageScale the amount to scale the image (in relation to the other images)
     * @return the image to put on the screen
     */
    private Image createImage(Texture texture, float imageScale) {
        Image image = new Image(texture);
        // Scale the image with the screen
        image.setSize(image.getWidth() * imageScale * scale, image.getHeight() * imageScale * scale);
        return image;
    }

    /**
     * Makes all buttons on the screen
     * @param text text to put on the button
     * @param scale the scale of the button (with the screen)
     * @param listener the listener attached to the button (used to boot up the mini-game)
     * @return the new button
     */
    private TextButton createButton(String text, float scale, ClickListener listener) {
        TextButton button = new TextButton(text, skin);
        button.getLabel().setFontScale(scale);
        button.setSize(button.getWidth() * scale, button.getHeight() * scale);
        button.addListener(listener);
        return button;
    }

    /**
     * Renders the background and polls for BACKSPACE key press
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Draw the background texture
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Update and draw the stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // Handle the Backspace key to return to the main menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            game.setScreen(new MainMenuScreen(game));
        }
        if (helpWindow != null) {
            helpWindow.render();
        }
    }

    /**
     * Called when the screen is resized
     * @param width width fo the new screen
     * @param height height of the new screen
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        float baseWidth = 1920f;
        float baseHeight = 1200f;
        float scaleWidth = width / baseWidth;
        float scaleHeight = height / baseHeight;
        scale = Math.min(scaleWidth, scaleHeight);
        if (scale == 0) {
            scale = 1;
        }
        show();
    }

    /**
     * Called when the screen is paused
     */
    @Override
    public void pause() {}

    /**
     *  Called when the screen is resumed after a pause
     */
    @Override
    public void resume() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen is hidden
     */
    @Override
    public void hide() {}

    /**
     *  Disposes of stage, skin, textures and batch
     */
    @Override
    public void dispose() { if (helpWindow != null) {
        helpWindow.dispose();
    }
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        batch.dispose();
    }
}