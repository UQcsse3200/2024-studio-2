package com.csse3200.game.minigames.maze.entities.mazenpc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.csse3200.game.minigames.maze.entities.configs.MazeEntityConfig;
import com.csse3200.game.minigames.maze.physics.MazePhysicsUtils;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Jellyfish represents a non-playable character (NPC) in the maze minigame.
 * Unlike more aggressive entities, the Jellyfish only wanders around and does not chase the player.
 */
public class Jellyfish extends MazeEntity {

    /**
     * Constructs a Jellyfish entity with the given configuration.
     *
     * @param config The configuration stats for the Jellyfish entity, including health and
     *               attack power.
     */
    public Jellyfish(MazeEntityConfig config) {
        // Add AI tasks specific to Jellyfish (no chasing, only wandering)
        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false));

        // Add animations specific to Jellyfish in the future please
        AnimationRenderWithAudioComponent animator = new AnimationRenderWithAudioComponent(
                ServiceLocator.getResourceService().getAsset("images/minigames/Jellyfish.atlas", TextureAtlas.class));
        animator.addAnimation("Walk", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);

        this.addComponent(new MazeCombatStatsComponent(config.health, config.baseAttack, config.speed))
                .addComponent(animator)
                .addComponent(new MazeEntityAnimationController())
                .addComponent(new LightingComponent().attach(LightingComponent.createPointLight(.5f, Color.BLUE)))
                .addComponent(aiComponent);

        // Update entities speed
        this.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(this.getComponent(MazeCombatStatsComponent.class).getBaseSpeed());

        this.setScale(.3f, .3f);
        MazePhysicsUtils.setScaledColliderAndHitBox(this, 0.3f, 0.5f);
    }
}

