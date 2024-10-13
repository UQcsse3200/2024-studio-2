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
    ParticleEffectRenderer effect;

    @BeforeEach
    public void setup() {
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(mock(RenderService.class));
        ServiceLocator.registerParticleService(mock(ParticleService.class));
        effect = spy(new ParticleService().makeEffect(ParticleService.ParticleType.DAMAGE10, 1));
        when(ServiceLocator.getParticleService().makeEffect(any(), anyInt())).thenReturn(effect);
        component = new ParticleEffectComponent(ParticleService.ParticleType.DAMAGE10);
        entity = new Entity().addComponent(component);
        ServiceLocator.registerTimeSource(mock(GameTime.class));
        entity.create();
    }
    @Test
    public void testEmit() {
        component.emit();
        verify(ServiceLocator.getParticleService(), atMostOnce()).makeEffect(any(), anyInt());
    }

    @Test
    public void testEmitShouldRestartIfComplete() {
        SpriteBatch sb = mock(SpriteBatch.class);
        when(ServiceLocator.getTimeSource().getDeltaTime()).thenReturn(10000f);
        for (int i = 0; i < 2; i++) {
            effect.render(sb);
        }
        component.emit();
        verify(ServiceLocator.getParticleService(), times(2)).makeEffect(any(), anyInt());
    }

    @Test
    public void testForceEmit() {
        component.forceEmit();
        verify(ServiceLocator.getParticleService(), times(2)).makeEffect(any(), anyInt());
    }

    @Test
    public void testAllowCompletion() {
        component.allowCompletion();
        verify(effect).allowCompletion();
    }

    @Test
    public void testStop() {
        component.stop();
        verify(effect).dispose();
    }

    @Test
    public void shouldSetPositionOnUpdate() {
        Vector2 position = new Vector2(3f, 1.2f);
        entity.setPosition(position);
        component.update();
        verify(effect).setPosition(position.x + entity.getScale().x/2, position.y + entity.getScale().y/2);
    }

    @Test
    public void shouldFreeOnDispose() {
        component.dispose();
        verify(effect).dispose();
    }
}
