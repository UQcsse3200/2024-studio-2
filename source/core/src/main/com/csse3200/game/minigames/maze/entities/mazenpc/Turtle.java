package com.csse3200.game.minigames.maze.entities.mazenpc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.csse3200.game.minigames.maze.components.npc.TurtleCarryComponent;
import com.csse3200.game.minigames.maze.entities.configs.MazeEntityConfig;
import com.csse3200.game.minigames.maze.physics.MazePhysicsUtils;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Turtle represents a non-playable character (NPC) in the maze minigame.
 * Turtles are not aggressive, but instead carry fish eggs away through the maze.
 */
public class Turtle extends MazeEntity {

    /**
     * Constructs a Turtle entity with the given configuration.
     *
     * @param config The configuration stats for the Turtle entity, including health and
     *               attack power.
     */
    public Turtle(Entity carry, MazeEntityConfig config) {
        // Add AI tasks specific to Turtle (no chasing, only wandering)
        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new WanderTask(new Vector2(1f, 1f), .2f, false));

        AnimationRenderWithAudioComponent animator = new AnimationRenderWithAudioComponent(
                ServiceLocator.getResourceService().getAsset("images/minigames/turtle.atlas", TextureAtlas.class));
        animator.addAnimation("Walk", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);

        this.addComponent(new MazeCombatStatsComponent(config.health, config.baseAttack, config.speed))
                .addComponent(animator)
                .addComponent(new MazeEntityAnimationController())
                .addComponent(new TurtleCarryComponent(carry))
                .addComponent(aiComponent);

        // Update entities speed
        this.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(this.getComponent(MazeCombatStatsComponent.class).getBaseSpeed());

        this.setScale(.35f, .35f);
        MazePhysicsUtils.setScaledColliderAndHitBox(this, 0.7f, 0.4f);
    }
}

