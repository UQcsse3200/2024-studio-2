package com.csse3200.game.particles.components;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.particles.ParticleEffectRenderer;
import com.csse3200.game.particles.ParticleService;
import com.csse3200.game.services.ServiceLocator;

/**
 * This class adds a particle effect to track an NPC in the game e.g. sparkles on the fish eggs
 */
public class ParticleEffectComponent extends Component {

    private final ParticleService.ParticleType type;
    protected ParticleEffectRenderer effect;

    public ParticleEffectComponent(ParticleService.ParticleType type) {
        super();
        this.type = type;
        resetEffect();
    }

    /**
     * Creates a new effect if none have been created yet or the previous one finished.
     */
    private void resetEffect() {
        this.effect = ServiceLocator.getParticleService().makeEffect(type, ParticleEffectRenderer.PARTICLE_LAYER);
    }

    /**
     * Starts the particle effect animation no matter what
     */
    public void forceEmit() {
        if (effect.isValid()) {
            effect.allowCompletion();
        }
        resetEffect();
    }

    /**
     * Starts the particle effect animation if it has completed
     */
    public void emit() {
        if (!effect.isValid()) {
            resetEffect();
        }
    }

    /**
     * Allows the particle effect animation to complete
     */
    public void allowCompletion() {
        effect.allowCompletion();
    }

    /**
     * Stops the particle effects completely.
     */
    public void stop() {
        if (effect.isValid()) {
            effect.dispose();
        }
    }

    /**
     * makes the particle effects follow the entity
     */
    @Override
    public void update() {
        Vector2 position = entity.getCenterPosition();
        //Setting the position of the ParticleEffect
        effect.setPosition(position);
    }

    /**
     * disposes of the particles
     */
    @Override
    public void dispose() {
        super.dispose();
        effect.dispose();
    }
}
