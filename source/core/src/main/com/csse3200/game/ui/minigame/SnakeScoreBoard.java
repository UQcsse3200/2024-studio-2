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

public class SnakeScoreBoard {

    private Stage stage;
    private Skin skin;
    private Label scoreLabel;
    private Label bronzeLabel;
    private Label silverLabel;
    private Label goldLabel;
    private Label medalLabel;
    private Table table;

    public SnakeScoreBoard(int initialScore) {
        this.stage = ServiceLocator.getRenderService().getStage();
        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

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

        // Add the score label to the table with increased padding
        table.add(scoreLabel).center().padTop(120).padBottom(40).expandX().fillX().padLeft(120);
        table.row(); // Move to the next row

        table.add(medalLabel).center().padTop(80).padBottom(20).expandX().fillX().padLeft(120);
        table.row(); // Move to the next row

        // Add the Bronze, Silver, Gold labels to the table with increased padding
        table.add(bronzeLabel).center().padTop(20).padBottom(20).expandX().fillX().padLeft(120);
        table.row(); // Move to the next row
        table.add(silverLabel).center().padTop(20).padBottom(20).expandX().fillX().padLeft(120);
        table.row(); // Move to the next row
        table.add(goldLabel).center().padTop(20).padBottom(20).expandX().fillX().padLeft(120);

        // Set the background image (optional)
        table.setBackground(new TextureRegionDrawable(new Texture(Gdx.files.internal("images/minigames/scoreboard.png"))));

        // Add the table to the stage; position will be relative to the screen size
        stage.addActor(table);

        // Set the table's size and position dynamically based on screen size
        updateTableSizeAndPosition();
    }

    // Call this method whenever the screen is resized
    private void updateTableSizeAndPosition() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Adjust size based on screen dimensions
        table.setSize(screenWidth * 0.22f, screenHeight * 0.5f); // 20% width, 50% height
        table.setPosition(screenWidth - table.getWidth() ,
                screenHeight - table.getHeight() - 20); // Right side, with 20px margin
    }

    // Update the score displayed on the scoreboard
    public void updateScore(int newScore) {
        scoreLabel.setText("Score: " + newScore);
    }

    // Call this method inside the render loop to handle screen resizing
    public void resize(int width, int height) {
        updateTableSizeAndPosition();
    }

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
}
