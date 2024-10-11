package com.csse3200.game.particles;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.csse3200.game.rendering.Renderable;
import com.csse3200.game.services.ServiceLocator;

public class ParticleEffectRenderer implements Renderable, Disposable {
    private final int layer;
    private final ParticleEffectPool.PooledEffect pooledEffect;
    private static final int PARTICLE_LAYER = 1;

    public ParticleEffectRenderer(ParticleEffectPool.PooledEffect pooledEffect, int layer) {
        this.pooledEffect = pooledEffect;
        this.layer = layer;
    }
    public ParticleEffectRenderer(ParticleEffectPool.PooledEffect pooledEffect) {
        this(pooledEffect, PARTICLE_LAYER);
    }

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

    @Override
    public void dispose() {
        ServiceLocator.getParticleService().freeEffect(pooledEffect);
        ServiceLocator.getRenderService().unregister(this);
    }
}
