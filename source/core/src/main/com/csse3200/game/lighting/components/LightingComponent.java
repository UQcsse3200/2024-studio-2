package com.csse3200.game.lighting.components;

import box2dLight.PositionalLight;
import box2dLight.ConeLight;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class LightingComponent extends Component {
    private final List<PositionalLight> lights;
    private final List<Vector2> offsets;

    /**
     * Attaches a light source to the entity's center position.
     *
     * @param light The light source.
     */
    public LightingComponent attach(PositionalLight light) {
        return attach(light, Vector2.Zero);
    }

    /**
     * Attaches a light source to the entity with an offset from its center position.
     *
     * @param light The light source.
     * @param offset Relative offset from entity/physics body center.
     */
    public LightingComponent attach(PositionalLight light, Vector2 offset) {
        lights.add(light);
        offsets.add(offset);
        return this;
    }

    /**
     * Create a component which allows light sources to be attached to an entity.
     */
    public LightingComponent() {
        this.lights = new ArrayList<>();
        this.offsets = new ArrayList<>();
    }

    /**
     * Get the light source corresponding to this LightComponent
     * @return LightComponent Light source
     */
    public List<PositionalLight> getLights() {
        return lights;
    }

    /**
     * Detaches a light source from the entity.
     *
     * @param light The light source.
     * @return true if the light source was attached to the entity
     */
    public boolean detach(PositionalLight light) {
        int index = lights.indexOf(light);
        if (index != -1) {
            lights.remove(index);
            offsets.remove(index);
            return true;
        }
        return false;
    }

    /**
     * Get the offset of the light from the entity/physics body center
     * @return Light offset
     */
    public List<Vector2> getOffsets() {
        return offsets;
    }

    /**
     * Update the position (and direction in future) of light to match the entity.
     * This is only necessary if the entity doesn't have a body.
     */
    @Override
    public void update() {
        for (int i = 0; i < lights.size(); i++) {
            lights.get(i).setPosition(entity.getCenterPosition().add(offsets.get(i)));
        }
    }

    /**
     * Create a new point light (circle).
     *
     * @param dist The distance the light travels or radius if it's not blocked
     * @param color The color of the point light
     *
     * @return A reference to the created PointLight.
     */
    public static PointLight createPointLight(float dist, Color color) {
        return ServiceLocator
                .getLightingService()
                .getLighting()
                .createPointLight(0, 0, dist, color);
    }

    /**
     * Create a new cone light (sector).
     *
     * @param dist The distance the light travels or radius if it's not blocked
     * @param cone The sector angle in degrees
     * @param color The color of the point light
     *
     * @return A reference to the created ConeLight.
     */
    public static ConeLight createConeLight(float dist, float cone, Color color) {
        return ServiceLocator
                .getLightingService()
                .getLighting()
                .createConeLight(0, 0, dist, 0, cone, color);
    }

    /**
     * Remove light sources on dispose
     */
    @Override
    public void dispose() {
        for (PositionalLight light: getLights()) {
            light.remove(true);
        }
    }
}
