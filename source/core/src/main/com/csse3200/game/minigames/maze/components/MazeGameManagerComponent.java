package com.csse3200.game.minigames.maze.components;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;

/**
 * A class to manage the game including the score. This is attatched to the player
 */
public class MazeGameManagerComponent extends Component {

    private int score = 0;  // Tracks the score
    private Entity lastFishEgg;  // Tracks the last fish egg

    /**
     * Gets the current score
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * This is used ot increase the score
     * @param increment the amount to change the score by
     */
    public void incrementScore(int increment) {
        score += increment;
        entity.getEvents().trigger("UpdateScore", this.score);
        if (score == MazeGameArea.NUM_EGGS) {
            entity.getEvents().trigger("endGame", this.score);
        }
    }

    /**
     * Tracls the last fish egg for disposal (disposal must happen here to ensure no faulty
     * references witt physics component)
     * @param entity the fish egg entity
     */
    public void setLastFishEgg(Entity entity) {
        lastFishEgg = entity;
    }

    /**
     * Austomatically called as needed, used to update the score if there is a fish egg to be
     * disposed of
     */
    @Override
    public void  update() {
        if (lastFishEgg != null) {
            this.incrementScore(1);
            lastFishEgg.setEnabled(false);
            lastFishEgg.dispose();
            lastFishEgg = null;
        }
    }
}
