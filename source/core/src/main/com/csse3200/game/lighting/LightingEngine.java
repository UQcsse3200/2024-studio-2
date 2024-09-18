package com.csse3200.game.lighting;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import box2dLight.RayHandler;
import box2dLight.PointLight;
import box2dLight.ConeLight;
import box2dLight.Light;
import com.csse3200.game.physics.PhysicsLayer;

public class LightingEngine implements Disposable {
    private static final int RAY_COUNT = 128;
    private final RayHandler rayHandler;
    private final Camera camera;
    World world;
    public LightingEngine(World world, Camera camera) {
        this.world = world;
        this.camera = camera;
        rayHandler = new RayHandler(world);
        RayHandler.useDiffuseLight(true);
    }

    /**
     * Render the lighting effects.
     */
    public void render(){
        rayHandler.setCombinedMatrix(camera.combined);
        rayHandler.updateAndRender();
    };

    /**
     * @return The RayHandler used by the engine.
     */
    public RayHandler getRayHandler() {
        return rayHandler;
    }

    /**
     * Disposes
     */
    public void dispose() {
        rayHandler.dispose();
    }

    /**
     * Create a new point light (circle).
     *
     * @param x The game world x position of the light
     * @param y The game world y position of the light
     * @param dist The distance the light travels or radius if it's not blocked
     * @param color The color of the point light
     *
     * @return A reference to the created PointLight.
     */
    public PointLight createPointLight(float x, float y, float dist, Color color) {
        PointLight pl = new PointLight(rayHandler, RAY_COUNT, color, dist, x, y);
        applyDefaultLightingSettings(pl);
        return pl;
    }

    /**
     * Create a new cone light (sector).
     *
     * @param x The game world x position of the light
     * @param y The game world y position of the light
     * @param dist The distance the light travels or radius if it's not blocked
     * @param dir The direction the light should face in degrees
     * @param cone The sector angle in degrees
     * @param color The color of the point light
     *
     * @return A reference to the created ConeLight.
     */
    public ConeLight createConeLight(float x, float y, float dist, float dir, float cone, Color color) {
        ConeLight cl = new ConeLight(rayHandler, RAY_COUNT, color, dist, x, y, dir, cone);
        applyDefaultLightingSettings(cl);
        return cl;
    }

    /**
     * Applies default lighting settings to a light source.
     * The default settings are:
     * - setIgnoreAttachedBody(true) which will ensure light does not collide with fixtures
     * of the body it is attached to
     * - setSoftnessLength(3f) which will mean the light will bleed through objects to some degree
     * - setContactFilter(PhysicsLayer.DEFAULT, PhysicsLayer.NONE, PhysicsLayer.ALL) which will
     * make the light collide with all physics bodies.
     */
    public static void applyDefaultLightingSettings(Light light) {
        light.setIgnoreAttachedBody(true);
        light.setSoftnessLength(3f);
        light.setContactFilter(PhysicsLayer.DEFAULT, PhysicsLayer.NONE, PhysicsLayer.ALL);
    }
}