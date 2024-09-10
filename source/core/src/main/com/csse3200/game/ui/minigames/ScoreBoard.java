package com.csse3200.game.ui.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.minigames.MiniGameNames;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.minigames.MiniGameConstants;

import static com.csse3200.game.components.minigames.MiniGameNames.BIRD;
import static com.csse3200.game.components.minigames.MiniGameNames.SNAKE;

/**
 * UI component for displaying the scoreboard in the Snake mini-game.
 * Manages score and medal display with dynamic scaling based on screen size.
 */
public class ScoreBoard {

    private Label scoreLabel;
    private Label bronzeLabel;
    private Label silverLabel;
    private Label goldLabel;
    private Label medalLabel;
    private Table table;
    private final MiniGameNames gameName;
    private int bronzeThreshold;
    private int silverThreshold;
    private int goldThreshold;
    private int initialScore;
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
            bronzeThreshold = MiniGameConstants.SNAKE_BRONZE_THRESHOLD;
            silverThreshold = MiniGameConstants.SNAKE_SILVER_THRESHOLD;
            goldThreshold = MiniGameConstants.SNAKE_GOLD_THRESHOLD;
        } else if (gameName == BIRD) {
            this.scale = 0.6;
            bronzeThreshold = MiniGameConstants.BIRDIE_DASH_BRONZE_THRESHOLD;
            silverThreshold = MiniGameConstants.BIRDIE_DASH_SILVER_THRESHOLD;
            goldThreshold = MiniGameConstants.BIRDIE_DASH_GOLD_THRESHOLD;
        } else { // MAZE
            this.scale = 1;
            bronzeThreshold = MiniGameConstants.MAZE_BRONZE_THRESHOLD;
            silverThreshold = MiniGameConstants.MAZE_SILVER_THRESHOLD;
            goldThreshold = MiniGameConstants.MAZE_GOLD_THRESHOLD;
        }
        Stage stage = ServiceLocator.getRenderService().getStage();
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Create score label
        scoreLabel = new Label("Score: " + initialScore, skin, "default-white");
        scoreLabel.setFontScale((float) (2.0f * scale));
        scoreLabel.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        scoreLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        // Create medal title
        medalLabel = new Label("Medals", skin, "default-white");
        medalLabel.setFontScale((float) (2.0f * scale));
        medalLabel.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        medalLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        // Create labels for bronze, silver, gold
        bronzeLabel = new Label("Bronze: " + bronzeThreshold, skin, "default-white");
        bronzeLabel.setFontScale((float) (1.5f * scale));
        bronzeLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        silverLabel = new Label("Silver: " + silverThreshold, skin, "default-white");
        silverLabel.setFontScale((float) (1.5f * scale));
        silverLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        goldLabel = new Label("Gold: " + goldThreshold, skin, "default-white");
        goldLabel.setFontScale((float) (1.5f * scale));
        goldLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        table = new Table();
        table.top().right();

        table.add(scoreLabel).center().padTop(120).padBottom(40).expandX().fillX().padLeft(120);
        table.row();
        table.add(medalLabel).center().padTop(80).padBottom(20).expandX().fillX().padLeft(120);
        table.row();
        table.add(bronzeLabel).center().padTop(20).padBottom(20).expandX().fillX().padLeft(120);
        table.row();
        table.add(silverLabel).center().padTop(20).padBottom(20).expandX().fillX().padLeft(120);
        table.row();
        table.add(goldLabel).center().padTop(20).padBottom(20).expandX().fillX().padLeft(120);

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
    public void resize(int width, int height) {
        updateTableSizeAndPosition();
    }

    /**
     * Disposes of the scoreboard resources.
     */
    public void dispose() {
        if (table != null) table.remove();
        if (scoreLabel != null) scoreLabel.remove();
        if (bronzeLabel != null) bronzeLabel.remove();
        if (silverLabel != null) silverLabel.remove();
        if (goldLabel != null) goldLabel.remove();
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
        table.setSize((float) (screenWidth * 0.22f * scale), ((float) (screenHeight * 0.5f * scale)));
        //table.setPosition(0, screenHeight - table.getHeight() - 15); // top right of screen
        table.setPosition(screenWidth - table.getWidth() - 20,
                screenHeight - table.getHeight() - 25);

        // Adjust padding and font sizes within the table for the scaling factor
        scoreLabel.setFontScale(2.0f * scaleFactor);
        medalLabel.setFontScale(2.0f * scaleFactor);
        bronzeLabel.setFontScale(1.5f * scaleFactor);
        silverLabel.setFontScale(1.5f * scaleFactor);
        goldLabel.setFontScale(1.5f * scaleFactor);

        table.clear();
        table.add(scoreLabel).center().padTop(120 * scaleFactor).padBottom(10 * scaleFactor).expandX().fillX().padLeft(120 * scaleFactor);
        table.row();
        table.add(medalLabel).center().padTop(50 * scaleFactor).padBottom(10 * scaleFactor).expandX().fillX().padLeft(120 * scaleFactor);
        table.row();
        table.add(bronzeLabel).center().padTop(20 * scaleFactor).padBottom(10 * scaleFactor).expandX().fillX().padLeft(120 * scaleFactor);
        table.row();
        table.add(silverLabel).center().padTop(20 * scaleFactor).padBottom(10 * scaleFactor).expandX().fillX().padLeft(120 * scaleFactor);
        table.row();
        table.add(goldLabel).center().padTop(20 * scaleFactor).padBottom(10 * scaleFactor).expandX().fillX().padLeft(120 * scaleFactor);
    }
}
