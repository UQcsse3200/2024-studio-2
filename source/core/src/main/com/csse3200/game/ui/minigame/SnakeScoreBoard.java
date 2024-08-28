package com.csse3200.game.ui.minigame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.minigame.MiniGameConstants;

/**
 * UI component for displaying the scoreboard in the Snake mini-game.
 * Manages score and medal display with dynamic scaling based on screen size.
 */
public class SnakeScoreBoard {

    private final Label scoreLabel;
    private final Label bronzeLabel;
    private final Label silverLabel;
    private final Label goldLabel;
    private final Label medalLabel;
    private final Table table;

    /**
     * Creates a new SnakeScoreBoard with the initial score.
     *
     * @param initialScore The starting score to display.
     */
    public SnakeScoreBoard(int initialScore) {
        Stage stage = ServiceLocator.getRenderService().getStage();
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Create and configure the score label
        scoreLabel = new Label("Score: " + initialScore, skin, "default-white");
        scoreLabel.setFontScale(2.0f);
        scoreLabel.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        scoreLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        // Create Medal Title
        medalLabel = new Label("Medals", skin, "default-white");
        medalLabel.setFontScale(2.0f);
        medalLabel.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        medalLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        // Create labels for Bronze, Silver, Gold
        bronzeLabel = new Label("Bronze: " + MiniGameConstants.SNAKE_BRONZE_THRESHOLD, skin, "default-white");
        bronzeLabel.setFontScale(1.5f);
        bronzeLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        silverLabel = new Label("Silver: " + MiniGameConstants.SNAKE_SILVER_THRESHOLD, skin, "default-white");
        silverLabel.setFontScale(1.5f);
        silverLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        goldLabel = new Label("Gold: " + MiniGameConstants.SNAKE_GOLD_THRESHOLD, skin, "default-white");
        goldLabel.setFontScale(1.5f);
        goldLabel.setAlignment(com.badlogic.gdx.utils.Align.left);

        // Create a table for better alignment and background color
        table = new Table();
        table.top().right(); // Align the table to the top-right corner

        table.add(scoreLabel).center().padTop(120).padBottom(40).expandX().fillX().padLeft(120);
        table.row();

        table.add(medalLabel).center().padTop(80).padBottom(20).expandX().fillX().padLeft(120);
        table.row();

        // Add the Bronze, Silver, Gold labels to the table with increased padding
        table.add(bronzeLabel).center().padTop(20).padBottom(20).expandX().fillX().padLeft(120);
        table.row();
        table.add(silverLabel).center().padTop(20).padBottom(20).expandX().fillX().padLeft(120);
        table.row();
        table.add(goldLabel).center().padTop(20).padBottom(20).expandX().fillX().padLeft(120);

        // Set the background image
        table.setBackground(new TextureRegionDrawable(new Texture(Gdx.files.internal("images/minigames/scoreboard.png"))));

        // Add the table to the stage; position will be relative to the screen size
        stage.addActor(table);

        // Set the table's size and position dynamically based on screen size
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
        if (table != null) {
            table.remove();
        }
        if (scoreLabel != null) {
            scoreLabel.remove();
        }
        if (bronzeLabel != null) {
            bronzeLabel.remove();
        }
        if (silverLabel != null) {
            silverLabel.remove();
        }
        if (goldLabel != null) {
            goldLabel.remove();
        }
    }

    /**
     * Configures the size, position, and scaling of the table and labels.
     */
    private void updateTableSizeAndPosition() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Define a base width and height for scaling
        float baseWidth = 1920f;
        float baseHeight = 1200f;

        // Calculate the scale factor based on screen size
        float scaleFactorX = screenWidth / baseWidth;
        float scaleFactorY = screenHeight / baseHeight;
        float scaleFactor = Math.min(scaleFactorX, scaleFactorY);

        // Scale the table's size and position based on screen dimensions
        table.setSize(screenWidth * 0.22f, screenHeight * 0.5f);
        table.setPosition(screenWidth - table.getWidth() - 20 * scaleFactor,
                screenHeight - table.getHeight() - 20 * scaleFactor);

        // Scale the font sizes and paddings
        scoreLabel.setFontScale(2.0f * scaleFactor);
        medalLabel.setFontScale(2.0f * scaleFactor);
        bronzeLabel.setFontScale(1.5f * scaleFactor);
        silverLabel.setFontScale(1.5f * scaleFactor);
        goldLabel.setFontScale(1.5f * scaleFactor);

        // Adjust padding within the table
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
