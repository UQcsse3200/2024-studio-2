package com.csse3200.game.particles;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
        ParticleEffectRenderer effect = ServiceLocator.getParticleService().makeEffect(ParticleService.ParticleType.BUBBLES, 1);
        assertNotEquals(null, effect);

        effect = ServiceLocator.getParticleService().makeEffect(ParticleService.ParticleType.DAMAGE10, 1);
        assertNotEquals(null, effect);

        effect = ServiceLocator.getParticleService().makeEffect(ParticleService.ParticleType.SPARKS, 1);
        assertNotEquals(null, effect);

        effect = ServiceLocator.getParticleService().makeEffect(ParticleService.ParticleType.FISH_EGG_SPARKLE, 1);
        assertNotEquals(null, effect);
    }

    @Test
    public void testPlayEffect() {
        ServiceLocator.getParticleService().playEffect(ParticleService.ParticleType.DAMAGE10, new Vector2(1.2f, 1.5f));
        verify(ServiceLocator.getRenderService()).register(any(ParticleEffectRenderer.class));
    }
}
