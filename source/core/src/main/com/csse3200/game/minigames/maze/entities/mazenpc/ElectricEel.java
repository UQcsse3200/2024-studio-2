package com.csse3200.game.minigames.maze.entities.mazenpc;

import box2dLight.Light;
import box2dLight.PositionalLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.npc.EelLightingEffect;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.csse3200.game.minigames.maze.components.tasks.MazeChaseTask;
import com.csse3200.game.minigames.maze.entities.configs.MazeEntityConfig;
import com.csse3200.game.minigames.maze.physics.MazePhysicsUtils;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Eel represents a non-playable character (NPC) in the maze mini-game.
 * The Eel will chase the player and cause a stun affect
 */
public class ElectricEel extends MazeEntity {

    /**
     * Constructs an Eel entity with the given target and configuration.
     *
     * @param target The entity that this AnglerFish will chase (e.g., the player).
     * @param config The configuration stats for this NPC, such as health and attack power.
     */
    public ElectricEel(Entity target, MazeEntityConfig config) {

        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false))
                .addTask(new MazeChaseTask(target, 10, 2f, 3f));

        AnimationRenderWithAudioComponent animator = new AnimationRenderWithAudioComponent(
                ServiceLocator.getResourceService().getAsset("images/minigames/eels.atlas", TextureAtlas.class));
        animator.addAnimation("Walk", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addSound("sounds/minigames/eel-electricity.mp3", "Attack", 0);

        PositionalLight light = LightingComponent.createPointLight(.5f, new Color(1f, .85f, .7f, 1f));

        this.addComponent(new MazeCombatStatsComponent(config.health, config.baseAttack, config.speed))
                .addComponent(animator)
                .addComponent(new MazeEntityAnimationController())
                .addComponent(new LightingComponent().attach(light))
                .addComponent(aiComponent)
                .addComponent(new EelLightingEffect(light));

        // Update entities speed
        this.getComponent(PhysicsMovementComponent.class).changeMaxSpeed(this.getComponent(MazeCombatStatsComponent.class).getBaseSpeed());

        this.getComponent(AnimationRenderWithAudioComponent.class).scaleEntity();
        this.setScale(.3f, .3f);
        MazePhysicsUtils.setScaledColliderAndHitBox(this, 0.8f, 0.25f);
    }
}

