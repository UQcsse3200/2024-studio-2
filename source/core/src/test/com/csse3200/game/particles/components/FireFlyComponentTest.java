package com.csse3200.game.particles.components;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.lighting.DayNightCycle;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.particles.ParticleService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class FireFlyComponentTest {
    Entity firefly;
    @BeforeEach
    void setup() throws IllegalAccessException {
        ServiceLocator.registerResourceService(new ResourceService());

        RayHandler rayHandler = mock(RayHandler.class);
        LightingEngine engine = new LightingEngine(rayHandler, mock(OrthographicCamera.class));
        LightingService mockLightingService = mock(LightingService.class);
        when(mockLightingService.getLighting()).thenReturn(engine);
        ServiceLocator.registerLightingService(mockLightingService);

        Field field = ReflectionUtils
                .findFields(RayHandler.class, f -> f.getName().equals("lightList"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .getFirst();

        field.setAccessible(true);
        field.set(rayHandler, new Array<>());

        field = ReflectionUtils
                .findFields(RayHandler.class, f -> f.getName().equals("disabledLights"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .getFirst();

        field.setAccessible(true);
        field.set(rayHandler, new Array<>());

        ServiceLocator.registerTimeSource(mock(GameTime.class));
        ServiceLocator.registerRenderService(mock(RenderService.class));
        ServiceLocator.registerParticleService(new ParticleService());

        firefly = NPCFactory.createFirefly();
        firefly.create();
    }

    @Test
    void testFireflyActive() {
        firefly.update();
        assertEquals(4f, firefly.getComponent(LightingComponent.class).getLights().getFirst().getDistance(), 0.05f);
    }

    @Test
    void testFireflyInActive() {
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 2);
        firefly.update();
        assertEquals(0f, firefly.getComponent(LightingComponent.class).getLights().getFirst().getDistance(), 0.05f);
    }
}
