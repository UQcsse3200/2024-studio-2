package com.csse3200.game.minigames.maze.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.MazeTouchAttackComponent;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.csse3200.game.minigames.maze.components.npc.MazePlayerAnimationController;
import com.csse3200.game.minigames.maze.components.player.MazePlayerActions;
import com.csse3200.game.minigames.maze.components.player.MazePlayerStatsDisplay;
import com.csse3200.game.minigames.maze.components.tasks.MazeMovementUtils;
import com.csse3200.game.minigames.maze.entities.configs.MazePlayerConfig;
import com.csse3200.game.minigames.maze.physics.MazePhysicsUtils;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.rendering.FaceMoveDirectionXComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * MazePlayer represents the player entity in the maze minigame.
 * It defines the components and behavior specific to the player.
 */
public class MazePlayer extends Entity {

    /**
     * Constructs a MazePlayer entity with the given player configuration stats.
     *
     * @param stats The configuration stats for the player entity, including health and
     *              attack power.
     */
    public MazePlayer(MazePlayerConfig stats) {
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
                .addComponent(new MazeCombatStatsComponent(stats.health, stats.baseAttack))
                .addComponent(new MazeTouchAttackComponent(PhysicsLayer.NPC, 15f))
                .addComponent(inputComponent)
                .addComponent(new FaceMoveDirectionXComponent())
                .addComponent(new MazePlayerStatsDisplay());
        this.addComponent(new MazeEntityAnimationController());

        // Adjust physical properties
        this.getComponent(ColliderComponent.class).setDensity(3f);

        // Scale the AnimationRenderComponent, not TextureRenderComponent
        this.getComponent(AnimationRenderWithAudioComponent.class).scaleEntity();  // Scale the animation
        this.setScale(this.getScale().scl(0.2f));  // Adjust the overall entity scale
        MazePhysicsUtils.setScaledHitBox(this, 0.85f, 0.45f);


        // Use oval shape for player to remove ghost collisions
        PolygonShape clipped = new PolygonShape();
        Vector2[] vertices = new Vector2[8];
        Vector2 center = this.getScale().scl(0.5f);
        Vector2 delta = center.cpy().scl(0.85f, 0.45f);
        for (int i = 0; i < vertices.length; i++) {
            double angle = 2*Math.PI / vertices.length * i;
            vertices[i] = new Vector2((float) Math.cos(angle), (float) Math.sin(angle)).scl(delta).add(center);
        }
        clipped.set(vertices);
        this.getComponent(ColliderComponent.class).setShape(clipped);

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

