package com.csse3200.game.particles.components;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.particles.ParticleEffectRenderer;
import com.csse3200.game.particles.ParticleService;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This class adds a particle effect to track an NPC in the game e.g. sparkles on the fish eggs
 */
public class ParticleEffectComponent extends RenderComponent {

    protected final ParticleEffect effect;

    public ParticleEffectComponent(ParticleService.ParticleType type) {
        super();
        this.effect = ServiceLocator.getParticleService().makeEffect(type);
    }

    /**
     * Returns the effect used on the npc
     * @return the effect
     */
    public ParticleEffect getEffect() {
        return effect;
    }

    /**
     * Gets the z coordinate of the particle effect
     * @return the z coordinate
     */
    @Override
    public float getZIndex() {
        return super.getZIndex() - 1e6f;
    }

    /**
     * Get the layer that the particle effects are on. Should be below sprites
     * @return the particle layer
     */
    @Override
    public int getLayer() {
        return ParticleEffectRenderer.PARTICLE_LAYER;
    }

    /**
     * Starts the particle effect animation
     */
    public void startEmitting() {
        effect.start();
    }

    /**
     * Stops the particle effect animation
     */
    public void stopEmitting() {
        effect.allowCompletion();
    }

    /**
     * draws the particles
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        Vector2 position = entity.getCenterPosition();
        //Setting the position of the ParticleEffect
        effect.setPosition(position.x, position.y);
        //Updating and Drawing the particle effect
        effect.draw(batch, ServiceLocator.getTimeSource().getDeltaTime());
    }

    /**
     * disposes of the particles
     */
    @Override
    public void dispose() {
        super.dispose();
        if (effect instanceof ParticleEffectPool.PooledEffect) {
            ((ParticleEffectPool.PooledEffect) effect).free();
        }
    }
}
