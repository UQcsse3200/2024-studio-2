package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.minigame.MiniGameConstants;


/**
 * Makes a new screen when the snake game is over.
 * Dispalys the stats and add buttons to exit and restart.
 */
public class EndSnakeScreen extends ScreenAdapter {
    private final GdxGame game;
    private final int score;
    private final Stage stage;
    private final Skin skin;

    // fonts
    private final BitmapFont font18;
    private final BitmapFont font26;
    private final BitmapFont font32;

    public EndSnakeScreen(GdxGame game, int score) {
        this.game = game;
        this.score = score;

        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        this.font18 = new BitmapFont(Gdx.files.internal("flat-earth/skin/fonts/pixel_18.fnt"));
        this.font26 = new BitmapFont(Gdx.files.internal("flat-earth/skin/fonts/pixel_26.fnt"));
        this.font32 = new BitmapFont(Gdx.files.internal("flat-earth/skin/fonts/pixel_32.fnt"));
        
        Gdx.input.setInputProcessor(stage);

        setupExitButton();
    }

    /**
     * Puts the exit button in the top right of the screen.
     * Will take the user back to the Main menu screen
     */
    private void setupExitButton() {
        // Create the exit button
        TextButton exitButton = new TextButton("Exit", skin);

        // Set up the exit button click event
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Return to main menu and original screen colour
                Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // Set up the table for UI layout
        Table table = new Table();
        table.setFillParent(true);
        table.top().right();
        table.add(exitButton).pad(10);

        // Add the table to the stage
        stage.addActor(table);
    }

    /**
     * Renders the screen. Sets the background colour, draws the UI elements (buttons) and
     * renders the message lables and handles keypresses
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Set the background color based on the score
        setBackgroundColor();

        // Draw the exit button and other UI elements
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // Render the game over messages
        renderEndMessage();

        handleKeyPress();
    }

    /**
     * Changes the screen if escape or R is pressed (to minigames menu or back to game respectively)
     */
    private void handleKeyPress() {

        // Key functionality for escape and restart
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {  // Restart game
            game.setScreen(new SnakeScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {  // Go to minigames menu
            Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
            game.setScreen(new MiniGameMenuScreen(game));
        }
    }

    /**
     * Changes the background colour based on sore/ medals (fail: green, bronze, silver and gold)
     */
    private void setBackgroundColor() {
        if (score < MiniGameConstants.SNAKE_FAIL_THRESHOLD) {
            // Failed
            // Background colour green rgb 50, 82, 29, 1
            Gdx.gl.glClearColor(50f / 255f, 82f / 255f, 29f / 255f, 1f);
        } else if (score <  MiniGameConstants.SNAKE_BRONZE_THRESHOLD) {
            // Bronze
            // Background colour green rgb 169, 113, 66, 1
            Gdx.gl.glClearColor(169f / 255f, 113f / 255f, 66f / 255f, 1f);
        } else if (score < MiniGameConstants.SNAKE_SILVER_THRESHOLD) {
            // Silver
            // Background colour green rgb 115, 122, 140, 1
            Gdx.gl.glClearColor(115f / 255f, 122f / 255f, 140f / 255f, 1f);
        } else {
            // Gold
            // Background colour green rgb 173, 162, 114, 1
            Gdx.gl.glClearColor(173f / 255f, 162f / 255f, 114f / 255f, 1f);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Renders the labels with score, message and title.
     * Renders the try again and menu buttons
     */
    private void renderEndMessage() {

        Table table = new Table();
        table.setFillParent(true);

        // End of Mini-Game label
        font32.getData().setScale(3f);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font32, Color.WHITE);
        Label endGameLabel = new Label("End of Mini-Game", labelStyle);
        table.add(endGameLabel).center().padBottom(50).row(); 
        table.row();

        // Score label
        font26.getData().setScale(2f);
        labelStyle = new Label.LabelStyle(font26, Color.WHITE);
        Label scoreLabel = new Label("Score: " + score, labelStyle);
        table.add(scoreLabel).center().padBottom(150).row(); 
        table.row();

        // Personalised message label
        font18.getData().setScale(2f);
        labelStyle = new Label.LabelStyle(font18, Color.WHITE);
        String scoreMessage = getMessage();
        Label scoreMessageLabel = new Label(scoreMessage, labelStyle);
        table.add(scoreMessageLabel).center().padBottom(200); 
        table.row();

        // Add buttons to the table
        TextButton tryAgainButton = new TextButton("Try Again", skin);
        tryAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SnakeScreen(game));
            }
        });

        TextButton menuButton = new TextButton("Mini-Game Menu", skin);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
                game.setScreen(new MiniGameMenuScreen(game));
            }
        });

        // Add buttons to the table and align them at the bottom
        table.add(tryAgainButton).pad(10).row();
        table.add(menuButton).center().pad(10).row();

        // Add to the stage
        stage.addActor(table);
    }

    /**
     * Gets the personalised massage based on score
     * @return the message
     */
    private String getMessage() {
        if (score < MiniGameConstants.SNAKE_FAIL_THRESHOLD) {
            // Fail
            return "Damn that was a small snake...";
        } else if (score < MiniGameConstants.SNAKE_BRONZE_THRESHOLD) {
            // Bronze
            return "Nawww, look he's growing";
        } else if (score < MiniGameConstants.SNAKE_SILVER_THRESHOLD) {
            // Silver
            return "That's a really big snake alright";
        } else {
            // Gold
            return "Snake king!";
        }
    }

    /**
     * Dispose the fonts, skin and stage.
     */
    @Override
    public void dispose() {
        font18.dispose();
        font26.dispose();
        font32.dispose();
        stage.dispose();
        skin.dispose(); 
    }
}
