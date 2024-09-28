package com.csse3200.game.lighting;

import box2dLight.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static com.csse3200.game.lighting.LightingEngine.LIGHTING_LAYER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class LightingEngineTest {
    @Mock
    RayHandler rayHandler;

    @Mock
    Camera camera;

    private static double EPS = 1e-3;

    @BeforeEach
    public void setup() throws IllegalAccessException {
        Field field = ReflectionUtils
                .findFields(RayHandler.class, f -> f.getName().equals("lightList"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);

        field.setAccessible(true);
        field.set(rayHandler, new Array<>());
    }

    @Test
    public void testRenderLighting(){
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        SpriteBatch sb = mock(SpriteBatch.class);
        engine.render(sb);
        verify(sb).end();
        verify(sb).begin();
        verify(rayHandler).setCombinedMatrix(camera.combined);
        verify(rayHandler).updateAndRender();
    };

    @Test
    public void testGetRayHandler() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        assertEquals(rayHandler, engine.getRayHandler());
    }

    @Test
    public void testGetLayer() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        assertEquals(LIGHTING_LAYER, engine.getLayer());
    }

    @Test
    public void testGetZIndex() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        assertEquals(0, engine.getZIndex());
    }

    @Test
    public void disposesRayHandler() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        engine.dispose();
        verify(rayHandler).dispose();
    }

    @Test
    public void createsPointLight() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        float x = 1;
        float y = 2;
        Color color = Color.GREEN;
        float dist = 4;
        double EPS = 1e-3;
        PointLight light = engine.createPointLight(x, y, dist, color);
        assertEquals(x, light.getX(), EPS);
        assertEquals(y, light.getY(), EPS);
        assertEquals(color, light.getColor());
        assertEquals(dist, light.getDistance());
    }

    @Test
    public void createsConeLight() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        float x = 1;
        float y = 2;
        Color color = Color.GREEN;
        float dir = 45;
        float cone = 30;
        float dist = 4;
        ConeLight light = engine.createConeLight(x, y, dist, dir, cone, color);
        assertEquals(x, light.getX(), EPS);
        assertEquals(y, light.getY(), EPS);
        assertEquals(color, light.getColor());
        assertEquals(dist, light.getDistance());
        assertEquals(cone, light.getConeDegree(), EPS);
        assertEquals(dir, light.getDirection(), EPS);
    }

    @Test
    public void testDefaultLightSettings() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        PointLight light = engine.createPointLight(0, 0, 0, Color.BLACK);
        assertTrue(light.getIgnoreAttachedBody());
        assertEquals(3f, light.getSoftShadowLength(), EPS);
    }
}
