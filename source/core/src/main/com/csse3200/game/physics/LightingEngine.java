package com.csse3200.game.physics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import box2dLight.RayHandler;

public class LightingEngine implements Disposable {
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
     * @return The box2dlights RayHandler used by the engine.
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
}