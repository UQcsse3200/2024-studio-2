package com.csse3200.game.minigames.maze.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.minigames.MiniGameNames;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.minigames.MiniGameConstants;

/**
 * Class to show the score and medal thresholds on the game screen
 */
public class MazePlayerScoreDisplay extends UIComponent {

    Table table;  // Table to display

    // All Labels to display
    private Label scoreLabel;
    private Label highScoreLabel;

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
        table.add(scoreLabel).center().padBottom(0).padTop(20).expandX().fillX().padLeft(0);
        table.row();
        if (GameState.minigame != null) {
            table.add(highScoreLabel).center().padBottom(20).padTop(0).expandX().fillX().padLeft(0);
            table.row();
        }
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
        scoreLabel.setFontScale(0.7f);

        if (GameState.minigame != null) {
            CharSequence highScore = String.format("High Score %d",
                    GameState.minigame.getHighScore(MiniGameNames.MAZE));
            highScoreLabel = new Label(highScore, skin, "large-white");
            highScoreLabel.setFontScale(0.7f);
        }
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
