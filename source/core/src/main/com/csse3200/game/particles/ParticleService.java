package com.csse3200.game.particles;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.Pool;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;
import com.csse3200.game.minigames.maze.components.ParticleEffectComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a global access point to the lighting engine. This is necessary for lights to be
 * attached to entities.
 * The lighting engine should be updated each frame by adding it to the renderer or calling
 * the render method in the game screen.
 */
public class ParticleService {
    private final Map<ParticleType, ParticleEffectPool> pools;
    private static final String IMAGES_DIR = "particles/images";

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

    public ParticleEffectPool.PooledEffect makeEffect(ParticleType type) {
        ParticleEffectPool.PooledEffect effect = pools.get(type).obtain();
        effect.reset(false);
        return effect;
    }

    public void freeEffect(ParticleEffectPool.PooledEffect effect) {
        effect.free();
    }
    public void playEffect(ParticleType type, float x, float y) {
        ParticleEffectPool.PooledEffect effect = makeEffect(type);
        ParticleEffectRenderer renderer = new ParticleEffectRenderer(effect, 2);
        ServiceLocator.getRenderService().register(renderer);
        effect.start();
        effect.setPosition(x, y);
    }

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
