package com.csse3200.game.minigames.maze.components;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;

public class MazeGameManagerComponent extends Component {

    private int score = 0;
    private Entity lastFishEgg;

    public int getScore() {
        return score;
    }

    public void incrementScore(int increment) {
        score += increment;
        entity.getEvents().trigger("UpdateScore", this.score);
        if (score == MazeGameArea.NUM_EGGS) {
            entity.getEvents().trigger("endGame", this.score);
        }
    }


    public void setLastFishEgg(Entity entity) {
        lastFishEgg = entity;
    }

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
