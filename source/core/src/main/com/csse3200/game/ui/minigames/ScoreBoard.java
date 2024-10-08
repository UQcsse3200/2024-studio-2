package com.csse3200.game.ui.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.minigames.MiniGameNames;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.minigames.MiniGameConstants;

import static com.csse3200.game.minigames.MiniGameNames.BIRD;
import static com.csse3200.game.minigames.MiniGameNames.SNAKE;

/**
 * UI component for displaying the scoreboard in all mini-games.
 * Manages score and medal display with dynamic scaling based on screen size.
 */
public class ScoreBoard {

    private Label scoreLabel;
    private Label highscoreLabel;
    private Table table;
    private final MiniGameNames gameName;
    private final int initialScore;
    private int highscore;
    private double scale; // scale relative to the game

    /**
     * Creates a new SnakeScoreBoard with the initial score.
     *
     * @param initialScore The starting score to display.
     */
    public ScoreBoard(int initialScore, MiniGameNames gameName) {
        this.initialScore = initialScore;
        this.gameName = gameName;

        scoreBoardSetUp();
    }

    private void scoreBoardSetUp () {

        // Assign thresholds depending on game
        if (gameName == SNAKE) {
            this.scale = 1;
            this.highscore = GameState.minigame.getHighScore("snake");
        } else if (gameName == BIRD) {
            this.scale = 1;
            this.highscore =  GameState.minigame.getHighScore("bird");
        } else { // MAZE
            this.scale = 1;
            this.highscore = GameState.minigame.getHighScore("maze");
        }

        Stage stage = ServiceLocator.getRenderService().getStage();
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Create labels
        scoreLabel = new Label("Score: " + initialScore, skin, "default-white");
        scoreLabel.setFontScale((float) (2.0f * scale));
        scoreLabel.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        scoreLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        highscoreLabel = new Label("High Score: " + highscore, skin, "default-white");
        highscoreLabel.setFontScale((float) (2.0f * scale));
        highscoreLabel.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        highscoreLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        table = new Table();
        table.top().right();

        table.add(scoreLabel).center().padTop(100).padBottom(10).expandX().fillX().padLeft(120);
        table.row();
        table.add(highscoreLabel).center().padTop(20).padBottom(10).expandX().fillX().padLeft(120);
        table.row();

        // Set the background image
        table.setBackground(new TextureRegionDrawable(new Texture(Gdx.files.internal("images/minigames/scoreboard.png"))));

        stage.addActor(table);
        updateTableSizeAndPosition();
    }


    /**
     * Updates the score displayed on the scoreboard.
     *
     * @param newScore The new score to display.
     */
    public void updateScore(int newScore) {
        scoreLabel.setText("Score: " + newScore);
    }

    /**
     * Adjusts the scoreboard's size and position based on screen size.
     */
    public void resize() {
        updateTableSizeAndPosition();
    }

    /**
     * Disposes of the scoreboard resources.
     */
    public void dispose() {
        if (table != null) table.remove();
        if (scoreLabel != null) scoreLabel.remove();
        if (highscoreLabel != null) highscoreLabel.remove();
    }

    /**
     * Configures the size, position, and scaling of the table and labels.
     */
    private void updateTableSizeAndPosition() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Define a base width and height for scaling
        float baseWidth = (float) (1920f * (1/scale));
        float baseHeight = (float) (1200f * (1/scale));

        // Calculate the scale factor based on screen size
        float scaleFactorX = screenWidth / baseWidth; // Scale relative to screen size
        float scaleFactorY = screenHeight / baseHeight;
        float scaleFactor = Math.min(scaleFactorX, scaleFactorY);

        // Scale the table's size and position based on screen dimensions
        table.setSize((float) (screenWidth * 0.3f * scale), ((float) (screenHeight * 0.2f * scale)));
        //table.setPosition(0, screenHeight - table.getHeight() - 15); // top right of screen
        table.setPosition(screenWidth - table.getWidth() + 40,
                screenHeight - table.getHeight() - 4);

        // Adjust padding and font sizes within the table for the scaling factor
        scoreLabel.setFontScale(1.6f * scaleFactor);
        highscoreLabel.setFontScale(1.6f * scaleFactor);
        table.clear();
        table.add(scoreLabel).center().padTop(60 * scaleFactor).padBottom(10 * scaleFactor).expandX().fillX().padLeft(120 * scaleFactor);
        table.row();
        table.add(highscoreLabel).center().padTop(20 * scaleFactor).padBottom(10 * scaleFactor).expandX().fillX().padLeft(120 * scaleFactor);
        table.row();
    }
}