package com.csse3200.game.particles;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderable;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.csse3200.game.particles.ParticleEffectRenderer.PARTICLE_LAYER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class ParticleEffectRendererTest {
    ParticleEffectRenderer effectRenderer;
    private static final int layer = 2;
    @BeforeEach
    public void setup() {
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(mock(RenderService.class));
        ServiceLocator.registerParticleService(spy(new ParticleService()));
        ParticleEffectPool.PooledEffect effect = ServiceLocator.getParticleService().makeEffect(
                ParticleService.ParticleType.DAMAGE10);
        effect.setPosition(0, 0);
        effect.start();
        effectRenderer = new ParticleEffectRenderer(effect, layer);
        ServiceLocator.registerTimeSource(mock(GameTime.class));
    }

    @Test
    public void testDefaultLayerConstructor() {
        ParticleEffectRenderer renderer = new ParticleEffectRenderer(mock(ParticleEffectPool.PooledEffect.class));
        assertEquals(PARTICLE_LAYER, renderer.getLayer());
    }

    @Test
    public void shouldRegisterOnConstructor() {
        verify(ServiceLocator.getRenderService()).register(effectRenderer);
    }

    @Test
    public void shouldDisposeOnCompleteDuringRender() {
        effectRenderer = spy(effectRenderer);
        SpriteBatch sb = mock(SpriteBatch.class);
        when(ServiceLocator.getTimeSource().getDeltaTime()).thenReturn(10000f);
        for (int i = 0; i < 4; i++) {
            effectRenderer.render(sb);
        }
        verify(effectRenderer).dispose();
    }

    @Test
    public void testCompareTo() {
        Renderable other = mock(Renderable.class);
        when(other.getZIndex()).thenReturn(0f);
        assertEquals(-1, effectRenderer.compareTo(other));
    }

    @Test
    public void testGetZIndex() {
        assertEquals(-1e6f, effectRenderer.getZIndex(), 1f);
    }
    @Test
    public void testGetLayer() {
        assertEquals(layer, effectRenderer.getLayer());
    }

    @Test
    public void shouldUnregisterOnDispose() {
        effectRenderer.dispose();
        verify(ServiceLocator.getRenderService()).unregister(effectRenderer);
    }

    @Test
    public void shouldFreeOnDispose() {
        effectRenderer.dispose();
        verify(ServiceLocator.getParticleService()).freeEffect(any());
    }
}
