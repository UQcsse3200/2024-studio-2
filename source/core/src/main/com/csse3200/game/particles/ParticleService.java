package com.csse3200.game.particles;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;
import com.csse3200.game.services.ServiceLocator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a global access point to create particle effects.
 */
public class ParticleService {
    // use pools to optimise using garbage collector usage
    private final Map<ParticleType, ParticleEffectPool> pools;
    // directory path where images used by particle effects are stored
    private static final String IMAGES_DIR = "particles/images";

    /**
     * Initialise the particle service and load particle effect assets. Requires
     * the resource service to be registered in the service locator.
     * All particle effects are scaled by 0.02f so they can be a reasonable size in the
     * particle effect editor.
     */
    public ParticleService() {
        pools = new HashMap<>();
        String[] paths = Arrays.stream(ParticleType.values()).map(type -> type.path).toArray(String[]::new);
        ServiceLocator.getResourceService().loadParticleEffects(paths, IMAGES_DIR);
        ServiceLocator.getResourceService().loadAll();
        for (ParticleType type : ParticleType.values()) {
            ServiceLocator.getResourceService().getAsset(type.path, ParticleEffect.class);
            ParticleEffect effect = ServiceLocator.getResourceService().getAsset(type.path, ParticleEffect.class);
            effect.scaleEffect(0.02f);
            pools.put(type, new ParticleEffectPool(
                    effect,
                    type.initialCapacity,
                    type.max
            ));
        }
    }

    /**
     * Create a particle effect of a certain type. The created particle effect must be freed when
     * it is no longer used by using the freeEffect function. Calls the effect start() function.
     * @param type the type of particle effect to make.
     * @param layer the render layer to make the particle.
     * @return reference to the created particle effect.
     */
    public ParticleEffectRenderer makeEffect(ParticleType type, int layer) {
        ParticleEffectPool.PooledEffect effect = pools.get(type).obtain();
        effect.reset(false);
        effect.start();
        return new ParticleEffectRenderer(effect, layer);
    }

    /**
     * Plays a particle effect to completion at a given x,y location. Rendering and freeing the
     * particle effect is handled by the particle service. Requires the render service to be
     * registered in the service locator. Initialises the ParticleEffectRenderer to display above
     * entities.
     * @param type the type of particle effect to play.
     * @param position the world position of the particle effect.
     */
    public void playEffect(ParticleType type, Vector2 position) {
        ParticleEffectRenderer effect = makeEffect(type, 2);
        effect.setPosition(position);
    }

    /**
     * Enum of the different types of particle effects that can be played.
     */
    public enum ParticleType {
        SPARKS("particles/electricparticles.p", MazeGameArea.NUM_EELS, MazeGameArea.NUM_EELS*2),
        FISH_EGG_SPARKLE("particles/starlight.p", MazeGameArea.NUM_EGGS, MazeGameArea.NUM_EGGS*2),
        BUBBLES("particles/trail.p", 2, 4),
        DAMAGE5("particles/damage_5.p", 10, 20),
        DAMAGE10("particles/damage_10.p", 5, 10),
        DAMAGE15("particles/damage_15.p", 5, 7),
        DAMAGE20("particles/damage_20.p", 5, 5),
        SCORE1("particles/score_1.p", 2, 16);

        final String path;
        final int initialCapacity;
        final int max;

        /**
         * Add a new particle effect type.
         * @param path path to the particle effect .p file
         * @param initialCapacity initial capacity for the internal ParticleEffectPool
         * @param max max number of particle effects of this type that could be active at once.
         */

        ParticleType(String path, int initialCapacity, int max) {
            this.path = path;
            this.initialCapacity = initialCapacity;
            this.max = max;
        }
    }
}
