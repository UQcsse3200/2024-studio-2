package com.csse3200.game.particles;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class ParticleServiceTest {
    @BeforeEach
    public void setup() {
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(mock(RenderService.class));
        ServiceLocator.registerParticleService(new ParticleService());
    }

    @Test
    public void testMakeEffect() {
        ParticleEffect effect = ServiceLocator.getParticleService().makeEffect(ParticleService.ParticleType.BUBBLES);
        assertEquals("bubble.png", effect.getEmitters().get(0).getImagePaths().get(0));

        effect = ServiceLocator.getParticleService().makeEffect(ParticleService.ParticleType.DAMAGE10);
        assertEquals("-10.png", effect.getEmitters().get(0).getImagePaths().get(0));

        effect = ServiceLocator.getParticleService().makeEffect(ParticleService.ParticleType.SPARKS);
        assertEquals("particle-star.png", effect.getEmitters().get(0).getImagePaths().get(0));

        effect = ServiceLocator.getParticleService().makeEffect(ParticleService.ParticleType.FISH_EGG_SPARKLE);
        assertEquals("spark-colored.png", effect.getEmitters().get(0).getImagePaths().get(0));
    }
    @Test
    public void testFreeEffect() {
        ParticleEffectPool.PooledEffect effect = mock(ParticleEffectPool.PooledEffect.class);
        ServiceLocator.getParticleService().freeEffect(effect);
        verify(effect).free();
    }

    @Test
    public void testPlayEffect() {
        ServiceLocator.getParticleService().playEffect(ParticleService.ParticleType.DAMAGE10, 1.2f, 1.5f);
        verify(ServiceLocator.getRenderService()).register(any(ParticleEffectRenderer.class));
    }
}
