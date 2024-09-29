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
    public void createsPointLightWithMaxDistance() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        float x = 1;
        float y = 2;
        float maxDist = Float.MAX_VALUE;  // Maximum possible distance
        Color color = Color.GREEN;
        PointLight light = engine.createPointLight(x, y, maxDist, color);
        assertEquals(maxDist, light.getDistance(), EPS, "PointLight distance should be max float value.");
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
    public void createsConeLightWithZeroConeDegree() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        float x = 1;
        float y = 2;
        float dist = 4;
        float dir = 90; // Facing straight up
        float cone = 0;  // Zero cone degree should create a line of light
        Color color = Color.BLUE;
        ConeLight light = engine.createConeLight(x, y, dist, dir, cone, color);
        assertEquals(0, light.getConeDegree(), EPS, "Cone degree should be 0 for a straight light.");
    }

    @Test
    public void testDefaultLightSettings() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        PointLight light = engine.createPointLight(0, 0, 0, Color.BLACK);
        assertTrue(light.getIgnoreAttachedBody());
        assertEquals(3f, light.getSoftShadowLength(), EPS);
    }
    @Test
    public void createsChainLight() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        float[] chain = {0f, 0f, 2f, 2f};  // Define the vertices of the chain light
        float dist = 5f;
        int dir = 1;  // Direction of the rays (not testable directly)
        Color color = Color.BLUE;

        // Create the ChainLight
        ChainLight light = engine.createChainLight(chain, dist, dir, color);

        // We can still verify the common properties of the ChainLight via its Light superclass
        assertEquals(dist, light.getDistance(), EPS);
        assertEquals(color, light.getColor());

        // Verify that default settings are applied (which should still work for ChainLight)
        assertTrue(light.getIgnoreAttachedBody(), "Light should ignore attached body");
        assertEquals(3f, light.getSoftShadowLength(), EPS, "Soft shadow length should be 3f");
    }

    // New Test for Applying Default Settings to Lights
    @Test
    public void appliesDefaultLightingSettings() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        PointLight light = engine.createPointLight(0, 0, 0, Color.WHITE);
        LightingEngine.applyDefaultLightingSettings(light);
        assertTrue(light.getIgnoreAttachedBody(), "Light should ignore attached body");
        assertEquals(3f, light.getSoftShadowLength(), EPS, "Soft shadow length should be 3f");
    }

    // Edge Case: Create a light with negative distance
    @Test
    public void createsLightWithNegativeDistance() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        PointLight light = engine.createPointLight(0, 0, -1, Color.RED);

        // Allow for small values near zero (e.g., 0.01)
        assertTrue(light.getDistance() < 0.1, "Negative distance should result in a very small or zero value");
    }

    @Test
    public void createsMultipleLights() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        PointLight light1 = engine.createPointLight(1, 1, 4, Color.RED);
        PointLight light2 = engine.createPointLight(2, 2, 8, Color.GREEN);
        ConeLight light3 = engine.createConeLight(3, 3, 10, 45, 30, Color.BLUE);

        assertNotNull(light1);
        assertNotNull(light2);
        assertNotNull(light3);

        assertEquals(1, light1.getX(), EPS);
        assertEquals(2, light2.getX(), EPS);
        assertEquals(3, light3.getX(), EPS);
    }
    @Test
    public void createsDirectionalLightWithNegativeDirection() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        float dir = -45;  // Negative direction
        Color color = Color.RED;
        DirectionalLight light = engine.createDirectionalLight(dir, color);
        assertEquals(dir, light.getDirection(), EPS, "Directtional light should support negative directions.");
    }
    @Test
    public void rendersWithoutLights() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        SpriteBatch sb = mock(SpriteBatch.class);
        engine.render(sb);

        verify(sb).end();
        verify(sb).begin();
        verify(rayHandler).updateAndRender();
    }



}
