package com.csse3200.game.particles.components;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.particles.ParticleEffectRenderer;
import com.csse3200.game.particles.ParticleService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class ParticleEffectComponentTest {
    Entity entity;
    ParticleEffectComponent component;
    ParticleEffectPool.PooledEffect effect;

    @BeforeEach
    public void setup() {
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(mock(RenderService.class));
        ServiceLocator.registerParticleService(mock(ParticleService.class));
        effect = spy(new ParticleService().makeEffect(ParticleService.ParticleType.BUBBLES));
        when(ServiceLocator.getParticleService().makeEffect(any())).thenReturn(effect);
        component = new ParticleEffectComponent(ParticleService.ParticleType.BUBBLES);
        entity = new Entity().addComponent(component);
        ServiceLocator.registerTimeSource(mock(GameTime.class));
        entity.create();
    }

    @Test
    public void testGetEffect() {
        assertEquals(effect, component.getEffect());
    }


    @Test
    public void testGetZIndex() {
        assertEquals(-1e6f, component.getZIndex(), 1f);
    }

    @Test
    public void testGetLayer() {
        assertEquals(ParticleEffectRenderer.PARTICLE_LAYER, component.getLayer());
    }

    @Test
    public void testStartEmitting() {
        component.startEmitting();
        verify(effect).start();
    }

    @Test
    public void testStopEmitting() {
        component.stopEmitting();
        verify(effect).allowCompletion();
    }

    @Test
    public void shouldSetPositionOnDraw() {
        Vector2 position = new Vector2(3f, 1.2f);
        entity.setPosition(position);
        component.draw(mock(SpriteBatch.class));
        verify(effect).setPosition(position.x + entity.getScale().x/2, position.y + entity.getScale().y/2);
    }

    @Test
    public void shouldFreeOnDispose() {
        component.dispose();
        verify(effect).free();
    }
}
