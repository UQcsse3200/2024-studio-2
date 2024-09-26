package com.csse3200.game.minigames.maze.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays the name of the mini-game maze game area.
 */
public class MazeGameAreaDisplay extends UIComponent {

    // Used for game title
    private final String gameAreaName;
    private Label title;

    public MazeGameAreaDisplay(String gameAreaName) {
        this.gameAreaName = gameAreaName;
    }

    /**
     * Makes the maze game area
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Makes the actor for the maze game area
     */
    private void addActors() {
        title = new Label(this.gameAreaName, skin, "large-white");
        stage.addActor(title);
    }

    /**
     * Method to draw sprites onto the game?
     *
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        int screenHeight = Gdx.graphics.getHeight();
        float offsetX = 10f;
        float offsetY = 30f;

        title.setPosition(offsetX, screenHeight - offsetY);
    }

    /**
     * Disposes of the maze game area
     */
    @Override
    public void dispose() {
        super.dispose();
        title.remove();
    }
}
