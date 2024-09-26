package com.csse3200.game.minigames.maze.components;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

public class MazeGameManagerComponent extends Component {

    private int score;
    private Entity lastFishEgg;

    public void MazeCombatStatsComponent () {
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore(int increment) {
        score += increment;
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
