package com.csse3200.game.minigames.maze.entities.mazenpc;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.csse3200.game.minigames.maze.components.npc.MazeOctopusAnimationController;
import com.csse3200.game.minigames.maze.components.tasks.MazeChaseTask;
import com.csse3200.game.minigames.maze.entities.configs.MazeEntityConfig;
import com.csse3200.game.minigames.maze.physics.MazePhysicsUtils;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * AnglerFish represents a non-playable character (NPC) in the maze mini-game.
 * It is a hostile entity that chases the player and attacks it.
 */
public class Octopus extends MazeEntity {

    /**
     * Constructs an AnglerFish entity with the given target and configuration.
     *
     * @param target The entity that this AnglerFish will chase (e.g., the player).
     * @param config The configuration stats for this NPC, such as health and attack power.
     */
    public Octopus(Entity target, MazeEntityConfig config) {
        // Add AI tasks specific to AnglerFish
        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false))
                .addTask(new MazeChaseTask(target, 10, 2f, 3f));

        AnimationRenderWithAudioComponent animator = new AnimationRenderWithAudioComponent(
                ServiceLocator.getResourceService().getAsset("images/minigames/octopus.atlas", TextureAtlas.class));
        animator.addAnimation("Walk", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addSound("sounds/minigames/octopus-move.mp3", "Walk", 3);

        // Set other unique components for Octopus
        this.addComponent(new MazeCombatStatsComponent(config.health, config.baseAttack, config.speed))
                .addComponent(animator)
                .addComponent(new MazeOctopusAnimationController())
                .addComponent(aiComponent);

        // Update entities speed
        this.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(this.getComponent(MazeCombatStatsComponent.class).getBaseSpeed());

        this.setScale(.3f, .3f);
        MazePhysicsUtils.setScaledColliderAndHitBox(this, 0.7f, 0.6f);
    }
}
