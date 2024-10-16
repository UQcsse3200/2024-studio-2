package com.csse3200.game.lighting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import box2dLight.*;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.rendering.Renderable;

public class LightingEngine implements Disposable, Renderable {
    private static final int RAY_COUNT = 128;
    private final RayHandler rayHandler;
    private final OrthographicCamera camera;
    public static final int LIGHTING_LAYER = 3;

    public LightingEngine(RayHandler rayHandler, OrthographicCamera camera) {
        this.camera = camera;
        this.rayHandler = rayHandler;
        RayHandler.useDiffuseLight(true);
    }

    public LightingEngine(World world, OrthographicCamera camera) {
        this(new RayHandler(world), camera);
    }

    @Override
    public void render(SpriteBatch sb){
        // need to make sure everything is done drawing first
        sb.end();
        render();
        sb.begin();
    }

    /**
     * Render the lighting effects.
     */
    public void render(){
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }

    /**
     * @return The RayHandler used by the engine.
     */
    public RayHandler getRayHandler() {
        return rayHandler;
    }


    @Override
    public int compareTo(Renderable o) {
        return Float.compare(getZIndex(), o.getZIndex());
    }

    @Override
    public int getLayer() {
        return LIGHTING_LAYER;
    }

    @Override
    public float getZIndex() {
        // lighting is all treated equally and can be rendered in any order
        return 0;
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
     * @param color The color of the light
     *
     * @return A reference to the created ConeLight.
     */
    public ConeLight createConeLight(float x, float y, float dist, float dir, float cone, Color color) {
        ConeLight cl = new ConeLight(rayHandler, RAY_COUNT, color, dist, x, y, dir, cone);
        applyDefaultLightingSettings(cl);
        return cl;
    }

    /**
     * Create a new directional light. Infinite distance light source coming from a certain direction.
     *
     * @param dir The direction the light should face in degrees
     * @param color The color of the light
     *
     * @return A reference to the created DirectionalLight.
     */
    public DirectionalLight createDirectionalLight(float dir, Color color) {
        DirectionalLight dl = new DirectionalLight(rayHandler, RAY_COUNT, color, dir);
        applyDefaultLightingSettings(dl);
        return dl;
    }

    /**
     * Create a new chain light. Infinite distance light source coming from a certain direction.
     *
     * @param chain float array of (x, y) vertices from which rays will be evenly distributed
     * @param dist The distance the light travels or radius if it's not blocked
     * @param dir The direction of rays: 1 is left, -1 is right
     * @param color The color of the light
     *
     * @return A reference to the created ChainLight.
     */
    public ChainLight createChainLight(float[] chain, float dist, int dir, Color color) {
        ChainLight cl = new ChainLight(rayHandler, RAY_COUNT, color, dist, dir, chain);
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
        light.setContactFilter(PhysicsLayer.DEFAULT, PhysicsLayer.NONE, PhysicsLayer.NONE);
    }
}