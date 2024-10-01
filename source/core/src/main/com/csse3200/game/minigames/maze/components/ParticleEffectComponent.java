package com.csse3200.game.minigames.maze.components;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This class adds particle effects to NPC's in the game including sparkles on the fish eggs
 */
public class ParticleEffectComponent extends RenderComponent {

    protected final ParticleEffect effect;
    public static final int PARTICLE_LAYER = 1;

    public ParticleEffectComponent(String effectPath) {
        this(ServiceLocator.getResourceService().getAsset(effectPath, ParticleEffect.class));
    }

    public ParticleEffectComponent(ParticleEffectPool pool) {
        this(pool.obtain());
    }

    public ParticleEffectComponent(ParticleEffect effect) {
        super();
        effect.scaleEffect(0.02f); // reasonable scale from exporting defaults from libgdx particle editor
        this.effect = effect;
    }

    /**
     * Returns the effect used on the npc
     * @return the effect
     */
    public ParticleEffect getEffect() {
        return effect;
    }

    /**
     * TODO: James idk what to put here
     * @return the getZIndex
     */
    @Override
    public float getZIndex() {
        return super.getZIndex() - 1e6f; // TODO this should really be a layer below sprites, but above tiles.
    }

    /**
     * Get the layer that the particle effects are on. Should be below sprites
     * @return the particle layer
     */
    @Override
    public int getLayer() {
        return PARTICLE_LAYER;
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
        /*if (effect.isComplete()) {
            effect.reset(); // TODO, make on complete customisable
        }*/
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
