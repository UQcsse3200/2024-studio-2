package com.csse3200.game.minigames.maze.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.MazeTouchAttackComponent;
import com.csse3200.game.minigames.maze.components.player.MazePlayerActions;
import com.csse3200.game.minigames.maze.components.player.MazePlayerStatsDisplay;
import com.csse3200.game.minigames.maze.entities.configs.MazePlayerConfig;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
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

        this.addComponent(new TextureRenderComponent("images/box_boy_leaf.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new MazePlayerActions())
                .addComponent(new MazeCombatStatsComponent(stats.health, stats.baseAttack))
                .addComponent(new MazeTouchAttackComponent(PhysicsLayer.NPC, 15f))
                .addComponent(inputComponent)
                .addComponent(new MazePlayerStatsDisplay());

        // Adjust physical properties
        this.getComponent(ColliderComponent.class).setDensity(3f);
        this.getComponent(TextureRenderComponent.class).scaleEntity();
        this.setScale(this.getScale().scl(0.2f));
        PhysicsUtils.setScaledCollider(this, 1f, 1f);

        // Add lighting components
        addLightingComponents();
    }

    /**
     * Adds lighting components to the player entity. The player is given two point lights
     * of different ranges and properties for enhanced visual effects.
     */
    private void addLightingComponents() {
        Color lightColor = new Color(0.55f, 0.45f, 0.75f, 1);
        RayHandler rayHandler = ServiceLocator.getLightingService().getLighting().getRayHandler();
        PointLight pl1 = new PointLight(rayHandler, 1000, lightColor, 4f, 0, 0);
        PointLight pl2 = new PointLight(rayHandler, 300, lightColor, 1.5f, 0, 0);

        this.addComponent(new LightingComponent().attach(pl1).attach(pl2));

        pl1.setSoftnessLength(0f);
        pl1.setContactFilter(PhysicsLayer.DEFAULT, PhysicsLayer.NONE, PhysicsLayer.OBSTACLE);
        pl2.setXray(true);
    }
}

