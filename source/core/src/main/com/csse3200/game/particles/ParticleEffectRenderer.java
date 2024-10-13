package com.csse3200.game.particles;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.csse3200.game.rendering.Renderable;
import com.csse3200.game.services.ServiceLocator;

/**
 * Renders a particle effect and frees it when completed.
 */
public class ParticleEffectRenderer implements Renderable, Disposable {
    private final int layer;
    private final ParticleEffectPool.PooledEffect pooledEffect;
    public static final int PARTICLE_LAYER = 1;

    /**
     * Initialise to render a given pooled particle effect on a specified layer.
     * @param pooledEffect the particle effect to be rendered.
     * @param layer the layer to render to.
     */
    public ParticleEffectRenderer(ParticleEffectPool.PooledEffect pooledEffect, int layer) {
        this.pooledEffect = pooledEffect;
        this.layer = layer;
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
        if (pooledEffect.isComplete()) {
            dispose();
        }
        pooledEffect.draw(batch, ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public int compareTo(Renderable o) {
        return Float.compare(getZIndex(), o.getZIndex());
    }

    @Override
    public float getZIndex() {
        return -pooledEffect.getBoundingBox().getCenterY() - 1e6f;
    }

    @Override
    public int getLayer() {
        return layer;
    }

    /**
     * Frees the particle effect from its pool and unregisters this from the render service.
     */
    @Override
    public void dispose() {
        ServiceLocator.getParticleService().freeEffect(pooledEffect);
        ServiceLocator.getRenderService().unregister(this);
    }
}
