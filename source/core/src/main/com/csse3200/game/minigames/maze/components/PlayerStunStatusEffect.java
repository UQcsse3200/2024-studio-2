package com.csse3200.game.minigames.maze.components;

import box2dLight.PositionalLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.components.player.MazePlayerActions;
import com.csse3200.game.particles.components.ParticleEffectComponent;

/**
 * Class for the NPC stun affect
 */
public class PlayerStunStatusEffect implements StatusEffect {
    Entity player;
    PositionalLight light;

    /**
     * Create the status effect and attach it to the entity.
     *
     * @param entity Entity to attach to
     */
    public void create(Entity entity) {
        player = entity;
    }

    /**
     * Start the effects of this status. Called whenever the status goes from inactive to active.
     */
    @Override
    public void start() {
        player.getComponent(MazePlayerActions.class).setEnabled(false);
        light = LightingComponent.createPointLight(1f, Color.WHITE);
        player.getComponent(LightingComponent.class).attach(light);
        player.getComponent(ParticleEffectComponent.class).allowCompletion();
    }

    /** Stop the effects of this status immediately. This can be called at any time. */
    @Override
    public void stop() {
        player.getComponent(MazePlayerActions.class).setEnabled(true);
        player.getComponent(LightingComponent.class).detach(light);
        light.remove(true);
    }

    /**
     * unused at the moment
     */
    @Override
    public void update() {
        light.setDistance(MathUtils.random() * 5f);
    }
}
