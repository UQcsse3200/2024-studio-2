package com.csse3200.game.minigames.maze.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.ui.UIComponent;

public class MazePlayerScoreDisplay extends UIComponent {

    Table table;
    private Label scoreLabel;

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
     *
     * @see Table for positioning options
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(80f).padLeft(5f);

        // Score text
        int score = entity.getComponent(MazeGameManagerComponent.class).getScore();
        CharSequence scoreText = String.format("Score: %d", score);
        scoreLabel = new Label(scoreText, skin, "large-white");

        table.add(scoreLabel);
        stage.addActor(table);
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
