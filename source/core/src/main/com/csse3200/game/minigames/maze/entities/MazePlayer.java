package com.csse3200.game.minigames.maze.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.MazeDifficultyIncrease;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;
import com.csse3200.game.minigames.maze.components.*;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.csse3200.game.minigames.maze.components.player.MazePlayerActions;
import com.csse3200.game.minigames.maze.components.player.MazePlayerStatsDisplay;
import com.csse3200.game.minigames.maze.entities.configs.MazePlayerConfig;
import com.csse3200.game.minigames.maze.physics.MazePhysicsUtils;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.rendering.FaceMoveDirectionXComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * MazePlayer represents the player entity in the maze mini-game.
 * It defines the components and behavior specific to the player.
 */
public class MazePlayer extends Entity {

    /**
     * Constructs a MazePlayer entity with the given player configuration stats.
     *
     * @param stats The configuration stats for the player entity, including health and
     *              attack power.
     *
     * @param gameArea The maze game area the player is playing in.
     */
    public MazePlayer(MazePlayerConfig stats, MazeGameArea gameArea) {
        super();

        InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForPlayer();

        AnimationRenderWithAudioComponent animator = new AnimationRenderWithAudioComponent(
        ServiceLocator.getResourceService().getAsset("images/minigames/fish.atlas", TextureAtlas.class));
        animator.addAnimation("Walk", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Attack", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("Idle", 0.2f, Animation.PlayMode.LOOP);

        this.addComponent(animator)
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setFriction(0))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new MazePlayerActions())
                .addComponent(new MazeCombatStatsComponent(stats.health, stats.baseAttack, stats.speed))
                .addComponent(new MazeTouchAttackComponent(PhysicsLayer.NPC, 15f))
                .addComponent(inputComponent)
                .addComponent(new FaceMoveDirectionXComponent())
                .addComponent(new MazePlayerStatsDisplay())
                .addComponent(new MazePlayerScoreDisplay())
                .addComponent(new StatusEffectComponent().registerStatusEffect("stun", new PlayerStunStatusEffect()))
                .addComponent(new MazeEntityAnimationController())
                .addComponent(new MazeGameManagerComponent())
                .addComponent(new MazeDifficultyIncrease(gameArea));

        // Adjust physical properties
        this.getComponent(ColliderComponent.class).setDensity(3f);

        // Scale the AnimationRenderComponent, not TextureRenderComponent
        this.getComponent(AnimationRenderWithAudioComponent.class).scaleEntity();  // Scale the animation
        this.setScale(this.getScale().scl(0.2f));  // Adjust the overall entity scale
        MazePhysicsUtils.setScaledColliderAndHitBox(this, 0.85f, 0.45f);

        // Add lighting components
        addLightingComponents();
    }

    /**
     * Adds lighting components to the player entity. The player is given two point lights
     * of different ranges and properties for enhanced visual effects.
     */
    private void addLightingComponents() {
        Color lightColor = new Color(0.45f, 0.35f, 0.85f, 1);
        RayHandler rayHandler = ServiceLocator.getLightingService().getLighting().getRayHandler();
        PointLight pl1 = new PointLight(rayHandler, 1000, lightColor, 4f, 0, 0);
        PointLight pl2 = new PointLight(rayHandler, 300, lightColor, 1.5f, 0, 0);

        this.addComponent(new LightingComponent().attach(pl1).attach(pl2));

        pl1.setSoftnessLength(0f);
        pl1.setContactFilter(PhysicsLayer.DEFAULT, PhysicsLayer.NONE, PhysicsLayer.OBSTACLE);
        pl2.setXray(true);
    }


}

