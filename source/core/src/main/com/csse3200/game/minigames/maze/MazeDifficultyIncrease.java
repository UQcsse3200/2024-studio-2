package com.csse3200.game.minigames.maze;

import box2dLight.Light;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.MiniGameConstants;
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
        // dim player lights by half throughout the game
        entity.getComponent(LightingComponent.class).getLights().forEach(
                light -> light.setDistance(light.getDistance() * (float) Math.pow(0.5, 1f / MiniGameConstants.MAZE_GOLD_THRESHOLD))
        );

        List<Entity> anglers = gameArea.getEnemies(Entity.EnemyType.MAZE_ANGLER);
        for (Entity angler : anglers) {
            // make angler faster from speed 0.6 -> 1.5
            Vector2 speed = angler.getComponent(MazeCombatStatsComponent.class).getBaseSpeed().scl(1 + 0.9f / (MiniGameConstants.MAZE_GOLD_THRESHOLD - 1) * score);
            angler.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(speed);
            for (Light light : angler.getComponent(LightingComponent.class).getLights()) {
                if (score >= MiniGameConstants.MAZE_BRONZE_THRESHOLD / 2) {
                    light.setColor(Color.ORANGE);
                }
                if (score >= MiniGameConstants.MAZE_BRONZE_THRESHOLD) {
                    light.setColor(Color.SCARLET);
                }
                if (score >= MiniGameConstants.MAZE_SILVER_THRESHOLD) {
                    light.setColor(Color.RED);
                }
            }
        }

        List<Entity> eels = gameArea.getEnemies(Entity.EnemyType.MAZE_EEL);
        for (Entity eel : eels) {
            // make eels a little bit faster from speed 0.8 -> 1.3
            Vector2 speed = eel.getComponent(MazeCombatStatsComponent.class).getBaseSpeed().scl(1 + 0.5f / (MiniGameConstants.MAZE_GOLD_THRESHOLD - 1) * score);
            eel.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(speed);
        }

        // spawn some extra jellyfish each time
        gameArea.spawnJellyfish(3, 3f);

        // dim jellyfish lights throughout the game and remove them past silver
        List<Entity> jellyfish = gameArea.getEnemies(Entity.EnemyType.MAZE_JELLYFISH);
        for (Entity jelly : jellyfish) {
            jelly.getComponent(LightingComponent.class).getLights().forEach(light ->
                    light.setDistance(light.getDistance() * (float) Math.pow(0.2, 1f / MiniGameConstants.MAZE_SILVER_THRESHOLD))
            );
            if (score >= MiniGameConstants.MAZE_SILVER_THRESHOLD) {
                jelly.getComponent(LightingComponent.class).getLights().forEach(light ->
                        light.setActive(false)
                );
            }
        }
    }
}
