package com.csse3200.game.lighting.components;

import box2dLight.Light;
import box2dLight.ConeLight;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;

public class LightingComponent extends Component {
    Light light;

    /**
     * Create a component which places a light source on an entity.
     * @param light The light source.
     */
    public LightingComponent(Light light) {
        this.light = light;
    }

    /**
     * Update the position (and direction in future) of light to match the entity.
     */
    @Override
    public void update() {
        light.setPosition(entity.getCenterPosition());
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
}
