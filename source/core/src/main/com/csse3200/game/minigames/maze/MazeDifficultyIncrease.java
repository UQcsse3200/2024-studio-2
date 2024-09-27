package com.csse3200.game.minigames.maze;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;

import java.util.List;

public class MazeDifficultyIncrease extends Component {
    private final MazeGameArea gameArea;

    public MazeDifficultyIncrease(MazeGameArea gameArea) {
        this.gameArea = gameArea;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("UpdateScore", this::setDifficulty);
    }

    private void setDifficulty(int score) {
        List<Entity> anglers = gameArea.getEnemies(Entity.EnemyType.MAZE_ANGLER);
        for (Entity angler : anglers) {
            Vector2 speed = angler.getComponent(MazeCombatStatsComponent.class).getBaseSpeed().scl(1 + 0.15f * score);
            angler.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(speed);
        }
    }
}
