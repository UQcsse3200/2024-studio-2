package com.csse3200.game.lighting;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import box2dLight.RayHandler;
import box2dLight.PointLight;
import box2dLight.ConeLight;

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
        pl.setXray(true);
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
        return new ConeLight(rayHandler, RAY_COUNT, color, dist, x, y, dir, cone);
    }
}