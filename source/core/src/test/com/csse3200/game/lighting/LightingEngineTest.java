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
/**
 * Unit tests for the LightingEngine class, covering light creation, rendering,
 * and management of default settings for various types of lights.
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class LightingEngineTest {

    @Mock
    RayHandler rayHandler;

    @Mock
    Camera camera;

    private static final double EPS = 1e-3;

    /**
     * Setup method that runs before each test. It mocks the RayHandler's internal light list
     * to prevent interference with other tests.
     */
    @BeforeEach
    void setup() throws IllegalAccessException {
        Field field = ReflectionUtils
                .findFields(RayHandler.class, f -> f.getName().equals("lightList"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .getFirst();

        field.setAccessible(true);
        field.set(rayHandler, new Array<>());
    }

    /**
     * Tests the rendering functionality of the LightingEngine, ensuring that it properly
     * begins and ends the SpriteBatch, updates the combined camera matrix, and renders lights.
     */
    @Test
    void testRenderLighting() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        SpriteBatch sb = mock(SpriteBatch.class);
        engine.render(sb);
        verify(sb).end();
        verify(sb).begin();
        verify(rayHandler).setCombinedMatrix(camera.combined);
        verify(rayHandler).updateAndRender();
    }

    /**
     * Tests that the correct RayHandler is returned by the LightingEngine.
     */
    @Test
    void testGetRayHandler() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        assertEquals(rayHandler, engine.getRayHandler());
    }

    /**
     * Tests that the correct lighting layer is returned by the LightingEngine.
     */
    @Test
    void testGetLayer() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        assertEquals(LIGHTING_LAYER, engine.getLayer());
    }

    /**
     * Tests that the correct Z-index (0) is returned by the LightingEngine.
     */
    @Test
    void testGetZIndex() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        assertEquals(0, engine.getZIndex());
    }

    /**
     * Tests that the RayHandler is properly disposed when the LightingEngine is disposed.
     */
    @Test
    void disposesRayHandler() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        engine.dispose();
        verify(rayHandler).dispose();
    }

    /**
     * Tests the creation of a PointLight with specified coordinates, distance, and color.
     */
    @Test
    void createsPointLight() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        float x = 1;
        float y = 2;
        Color color = Color.GREEN;
        float dist = 4;
        PointLight light = engine.createPointLight(x, y, dist, color);
        assertEquals(x, light.getX(), EPS);
        assertEquals(y, light.getY(), EPS);
        assertEquals(color, light.getColor());
        assertEquals(dist, light.getDistance());
    }

    /**
     * Tests the creation of a PointLight with the maximum possible float distance.
     */
    @Test
    void createsPointLightWithMaxDistance() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        float x = 1;
        float y = 2;
        float maxDist = Float.MAX_VALUE;  // Maximum possible distance
        Color color = Color.GREEN;
        PointLight light = engine.createPointLight(x, y, maxDist, color);
        assertEquals(maxDist, light.getDistance(), EPS, "PointLight distance should be max float value.");
    }

    /**
     * Tests the creation of a ConeLight with specified coordinates, distance, direction, and cone angle.
     */
    @Test
    void createsConeLight() {
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

    /**
     * Tests the creation of a ConeLight with a zero cone degree, ensuring it creates a straight line of light.
     */
    @Test
    void createsConeLightWithZeroConeDegree() {
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

    /**
     * Tests the default lighting settings applied to a PointLight, such as ignoring attached bodies
     * and setting the soft shadow length.
     */
    @Test
    void testDefaultLightSettings() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        PointLight light = engine.createPointLight(0, 0, 0, Color.BLACK);
        assertTrue(light.getIgnoreAttachedBody());
        assertEquals(3f, light.getSoftShadowLength(), EPS);
    }

    /**
     * Tests the creation of a ChainLight with specified vertices and distance.
     * Verifies that the default light settings are applied correctly.
     */
    @Test
    void createsChainLight() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        float[] chain = {0f, 0f, 2f, 2f};  // Define the vertices of the chain light
        float dist = 5f;
        int dir = 1;  // Direction of the rays (not testable directly)
        Color color = Color.BLUE;

        // Create the ChainLight
        ChainLight light = engine.createChainLight(chain, dist, dir, color);

        // Verify common properties of ChainLight
        assertEquals(dist, light.getDistance(), EPS);
        assertEquals(color, light.getColor());

        // Verify default settings
        assertTrue(light.getIgnoreAttachedBody(), "Light should ignore attached body");
        assertEquals(3f, light.getSoftShadowLength(), EPS, "Soft shadow length should be 3f");
    }

    /**
     * Tests that default lighting settings can be applied to any light source.
     */
    @Test
    void appliesDefaultLightingSettings() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        PointLight light = engine.createPointLight(0, 0, 0, Color.WHITE);
        LightingEngine.applyDefaultLightingSettings(light);
        assertTrue(light.getIgnoreAttachedBody(), "Light should ignore attached body");
        assertEquals(3f, light.getSoftShadowLength(), EPS, "Soft shadow length should be 3f");
    }

    /**
     * Tests the creation of a PointLight with a negative distance, ensuring that the distance is clamped to a small value.
     */
    @Test
    void createsLightWithNegativeDistance() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        PointLight light = engine.createPointLight(0, 0, -1, Color.RED);

        // Allow for small values near zero (e.g., 0.01)
        assertTrue(light.getDistance() < 0.1, "Negative distance should result in a very small or zero value");
    }

    /**
     * Tests the creation of multiple light sources in the LightingEngine.
     */
    @Test
    void createsMultipleLights() {
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

    /**
     * Tests the creation of a DirectionalLight with a negative direction value.
     */
    @Test
    void createsDirectionalLightWithNegativeDirection() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        float dir = -45;  // Negative direction
        Color color = Color.RED;
        DirectionalLight light = engine.createDirectionalLight(dir, color);
        assertEquals(dir, light.getDirection(), EPS, "Directional light should support negative directions.");
    }

    /**
     * Tests that the LightingEngine can render lights even when no lights are present.
     */
    @Test
    void rendersWithoutLights() {
        LightingEngine engine = new LightingEngine(rayHandler, camera);
        SpriteBatch sb = mock(SpriteBatch.class);
        engine.render(sb);

        verify(sb).end();
        verify(sb).begin();
        verify(rayHandler).updateAndRender();
    }
}
