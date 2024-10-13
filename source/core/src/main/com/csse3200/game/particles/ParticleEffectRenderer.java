package com.csse3200.game.particles;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.Renderable;
import com.csse3200.game.services.ServiceLocator;

/**
 * Renders a particle effect and frees it when completed.
 */
public class ParticleEffectRenderer implements Renderable, Disposable {
    private final int layer;
    private ParticleEffectPool.PooledEffect pooledEffect;
    public static final int PARTICLE_LAYER = 1;

    /**
     * Initialise to render a given pooled particle effect on a specified layer.
     * Requires the render service to be initialised.
     * @param pooledEffect the particle effect to be rendered.
     * @param layer the layer to render to.
     */
    public ParticleEffectRenderer(ParticleEffectPool.PooledEffect pooledEffect, int layer) {
        this.pooledEffect = pooledEffect;
        this.layer = layer;
        ServiceLocator.getRenderService().register(this);
    }

    /**
     * Initialise to render a given pooled particle effect on PARTICLE_LAYER layer.
     * @param pooledEffect the particle effect to be rendered.
     */
    public ParticleEffectRenderer(ParticleEffectPool.PooledEffect pooledEffect) {
        this(pooledEffect, PARTICLE_LAYER);
    }

    /**
     * Updates and renders the particle effect. If the effect is complete, will dispose.
     * @param batch Batch to render to.
     */
    @Override
    public void render(SpriteBatch batch) {
        pooledEffect.draw(batch, ServiceLocator.getTimeSource().getDeltaTime());
        if (pooledEffect.isComplete()) {
            dispose();
        }
    }

    @Override
    public int compareTo(Renderable o) {
        return Float.compare(getZIndex(), o.getZIndex());
    }

    @Override
    public float getZIndex() {
        return -pooledEffect.getEmitters().get(0).getY() - 1e6f;
    }

    @Override
    public int getLayer() {
        return layer;
    }

    /**
     * Sets the position of the particle effect.
     * @param position the position to set to.
     */
    public void setPosition(Vector2 position) {
        setPosition(position.x, position.y);
    }

    /**
     * Sets the position of the particle effect.
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public void setPosition(float x, float y) {
        if (isValid()) {
            pooledEffect.setPosition(x, y);
        }
    }

    /**
     * Allows the completion of the particle effect.
     */
    public void allowCompletion() {
        if (isValid()) {
            pooledEffect.allowCompletion();
        }
    }

    /**
     * Returns whether the underlying particle effect has completed.
     * @return true if the particle effect has completed.
     */
    public boolean isValid() {
        return pooledEffect != null;
    }

    /**
     * Frees the particle effect from its pool and unregisters this from the render service.
     */
    @Override
    public void dispose() {
        pooledEffect.free();
        ServiceLocator.getRenderService().unregister(this);
        pooledEffect = null;
    }
}
