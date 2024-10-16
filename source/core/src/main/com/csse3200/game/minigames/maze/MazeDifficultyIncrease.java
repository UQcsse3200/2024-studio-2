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

import static com.csse3200.game.lighting.LightingUtils.interpolateColorCycle;

/**
 * This class increases the Maze difficulty as the player gets closer to a high score
 */
public class MazeDifficultyIncrease extends Component {
    private final MazeGameArea gameArea;

    public MazeDifficultyIncrease(MazeGameArea gameArea) {
        this.gameArea = gameArea;
    }

    /**
     * Sets a listener for when the score has been updated
     */
    @Override
    public void create() {
        entity.getEvents().addListener("UpdateScore", this::setDifficulty);
    }

    /**
     * Sets the difficulty based on the score
     * @param score the current game score
     */
    private void setDifficulty(int score) {
        // dim player lights by half throughout the game
        entity.getComponent(LightingComponent.class).getLights().forEach(
                light -> light.setDistance(light.getDistance() * (float) Math.pow(0.5, 1f / MiniGameConstants.MAZE_GOLD_THRESHOLD))
        );

        List<Entity> anglers = gameArea.getEnemies(Entity.EnemyType.MAZE_ANGLER);
        for (Entity angler : anglers) {
            // make angler faster from speed 0.6 -> 1.38
            Vector2 speed = angler.getComponent(MazeCombatStatsComponent.class).getBaseSpeed().scl(1 + 1.3f / (MiniGameConstants.MAZE_GOLD_THRESHOLD - 1) * score);
            angler.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(speed);
            for (Light light : angler.getComponent(LightingComponent.class).getLights()) {
                light.setColor(interpolateColorCycle(new Color[]{Color.GREEN, Color.ORANGE, Color.SCARLET, Color.RED},
                        3f/4 * score / (MiniGameConstants.MAZE_GOLD_THRESHOLD - 1)));
            }
        }

        List<Entity> eels = gameArea.getEnemies(Entity.EnemyType.MAZE_EEL);
        for (Entity eel : eels) {
            // make eels a little bit faster from speed 0.8 -> 1.2
            Vector2 speed = eel.getComponent(MazeCombatStatsComponent.class).getBaseSpeed().scl(1 + 0.5f / (MiniGameConstants.MAZE_GOLD_THRESHOLD - 1) * score);
            eel.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(speed);
        }

        List<Entity> octopi = gameArea.getEnemies(Entity.EnemyType.MAZE_OCTOPUS);
        for (Entity octopus : octopi) {
            // make eels a little bit faster from speed 1 -> 1.4
            Vector2 speed = octopus.getComponent(MazeCombatStatsComponent.class).getBaseSpeed().scl(1 + 0.4f / (MiniGameConstants.MAZE_GOLD_THRESHOLD - 1) * score);
            octopus.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(speed);
        }

        // spawn some extra jellyfish each time
        gameArea.spawnJellyfish(1, 3f);
        gameArea.spawnGreenJellyfish(1, 3f);

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
