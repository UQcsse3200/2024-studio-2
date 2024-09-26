package com.csse3200.game.minigames.maze.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.minigames.MiniGameConstants;

public class MazePlayerScoreDisplay extends UIComponent {

    Table table;
    private Label scoreLabel;
    private Label bronzeLabel;
    private Label silverLabel;
    private Label goldLabel;

    /**
     * Adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("UpdateScore", this::updatePlayerScoreUI);
    }

    /**
     * Creates actors and positions them on the stage using a table.
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(80f).padLeft(5f);

        makeLables();

        // Add labels to the table
        table.add(scoreLabel).center().padBottom(20).padTop(20).expandX().fillX().padLeft(0);
        table.row();
        table.add(bronzeLabel).center().expandX().fillX().padLeft(0);
        table.row();
        table.add(silverLabel).center().expandX().fillX().padLeft(0);
        table.row();
        table.add(goldLabel).center().expandX().fillX().padLeft(0);
        stage.addActor(table);
    }

    /**
     * Makes the labels to put on the screen
     */
    private void makeLables() {
        // Score text
        int score = entity.getComponent(MazeGameManagerComponent.class).getScore();
        CharSequence scoreText = String.format("Score: %d", score);
        scoreLabel = new Label(scoreText, skin, "large-white");

        // Medals
        CharSequence bronzeText = String.format("Bronze Medal %d", MiniGameConstants.MAZE_BRONZE_THRESHOLD);
        bronzeLabel = new Label(bronzeText, skin, "large-white");

        CharSequence silverText = String.format("Silver Medal %d", MiniGameConstants.MAZE_SILVER_THRESHOLD);
        silverLabel = new Label(silverText, skin, "large-white");

        CharSequence goldText = String.format("Gold Medal %d", MiniGameConstants.MAZE_GOLD_THRESHOLD);
        goldLabel = new Label(goldText, skin, "large-white");
    }

    /**
     * Draw is handles by the stage
     *
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    /**
     * Updates the player's score on the ui.
     *
     * @param score the players score
     */
    public void updatePlayerScoreUI(int score) {
        CharSequence text = String.format("Score: %d", score);
        scoreLabel.setText(text);
    }

    /**
     * Disposes of heart image and label
     */
    @Override
    public void dispose() {
        super.dispose();
        scoreLabel.remove();
    }
}
