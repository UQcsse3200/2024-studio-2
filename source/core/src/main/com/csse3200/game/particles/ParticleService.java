package com.csse3200.game.particles;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
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
     * it is no longer used by using the freeEffect function.
     * @param type the type of particle effect to make.
     * @return reference to the created particle effect.
     */
    public ParticleEffectPool.PooledEffect makeEffect(ParticleType type) {
        ParticleEffectPool.PooledEffect effect = pools.get(type).obtain();
        effect.reset(false);
        return effect;
    }

    /**
     * Frees the particle effect returning it to the internal pool of particle effects.
     * @param effect the effect to free.
     */
    public void freeEffect(ParticleEffectPool.PooledEffect effect) {
        effect.free();
    }

    /**
     * Plays a particle effect to completion at a given x,y location. Rendering and freeing the
     * particle effect is handled by the particle service. Requires the render service to be
     * registered in the service locator.
     * @param type the type of particle effect to play.
     * @param x the x world coordinate
     * @param y the y world coordinate
     */
    public void playEffect(ParticleType type, float x, float y) {
        ParticleEffectPool.PooledEffect effect = makeEffect(type);
        ParticleEffectRenderer renderer = new ParticleEffectRenderer(effect, 2);
        ServiceLocator.getRenderService().register(renderer);
        effect.start();
        effect.setPosition(x, y);
    }

    /**
     * Enum of the different types of particle effects that can be played.
     */
    public enum ParticleType {
        SPARKS("particles/electricparticles.p", MazeGameArea.NUM_EELS, MazeGameArea.NUM_EELS*2),
        FISH_EGG_SPARKLE("particles/starlight.p", MazeGameArea.NUM_EGGS, MazeGameArea.NUM_EGGS*2),
        BUBBLES("particles/trail.p", 2, 4),
        DAMAGE10("particles/damage_10.p", 5, 10);

        final String path;
        final int initialCapacity;
        final int max;

        ParticleType(String path, int initialCapacity, int max) {
            this.path = path;
            this.initialCapacity = initialCapacity;
            this.max = max;
        }
    }
}
