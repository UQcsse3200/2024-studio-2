package com.csse3200.game.minigames.maze.entities.mazenpc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;
import com.csse3200.game.minigames.maze.components.ParticleEffectComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * FishEgg represents an entity in the maze mini-game that can be collected or interacted with.
 * It does not actively chase or attack the player.
 */
public class FishEgg extends Entity {
    private static ParticleEffectPool particleEffectPool;

    /**
     * Constructs a FishEgg entity with the given configuration.
     */
    public FishEgg() {
        // Add lighting component
        this.addComponent(new LightingComponent().attach(LightingComponent.createPointLight(1f, Color.ORANGE)))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setGroupIndex((short) -1)) // don’t collide with NPCs (although can still get affected by knockback)
                .addComponent(new HitboxComponent().setLayer((short) (PhysicsLayer.PLAYER | PhysicsLayer.NPC)))
                .addComponent(new TextureRenderComponent("images/minigames/fishegg.png"))
                .addComponent(new ParticleEffectComponent(particleEffectPool));

        // Initialize scale
        float scaleX = 0.1f; // Adjust size of FishEgg in X direction
        float scaleY = 0.1f; // Adjust size of FishEgg in Y direction
        this.setScale(scaleX, scaleY);
    }

    public static void resetParticlePool() {
        ParticleEffect effect = ServiceLocator.getResourceService().getAsset("images/minigames/starlight.p", ParticleEffect.class);
        particleEffectPool = new ParticleEffectPool(effect, MazeGameArea.NUM_EGGS, MazeGameArea.NUM_EGGS * 2);
    }
}
