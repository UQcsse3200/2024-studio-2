package com.csse3200.game.lighting.components;

import box2dLight.PositionalLight;
import box2dLight.ConeLight;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

public class LightingComponent extends Component {
    private final PositionalLight light;
    private Vector2 offset;
    private final boolean attachToPhysicsBody;

    /**
     * Create a component which places a light source on an entity.
     * @param light The light source.
     */
    public LightingComponent(PositionalLight light) {
        this(light, Vector2.Zero, false);
    }

    /**
     * Create a component which places a light source on an entity.
     * Optionally attaches that light to the entity's physics body.
     *
     * @param light The light source.
     * @param offset Relative offset of light from entity center.
     */
    public LightingComponent(PositionalLight light, Vector2 offset) {
        this(light, offset, false);
    }

    /**
     * Create a component which places a light source on an entity.
     * Optionally attaches that light to the entity's physics body.
     *
     * @param light The light source.
     * @param offset Relative offset from entity/physics body center.
     * @param attachToPhysicsBody Whether to attach the light source to the entity physics body.
     */
    public LightingComponent(PositionalLight light, Vector2 offset, boolean attachToPhysicsBody) {
        this.light = light;
        this.offset = offset;
        this.attachToPhysicsBody = attachToPhysicsBody;
    }

    /**
     * Create a component which places a light source on an entity.
     * Optionally attaches that light to the entity's physics body.
     *
     * @param light The light source.
     * @param attachToPhysicsBody Whether to attach the light source to the entity physics body.
     */
    public LightingComponent(PositionalLight light, boolean attachToPhysicsBody) {
        this(light, Vector2.Zero, attachToPhysicsBody);
    }

    /**
     * Get the light source corresponding to this LightComponent
     * @return LightComponent Light source
     */
    public PositionalLight getLight() {
        return light;
    }

    /**
     * Get the offset of the light from the entity/physics body center
     * @return Light offset
     */
    public Vector2 getOffset() {
        return offset.cpy();
    }

    /**
     * Update the offset of the light from the entity/physics body center
     * @param offset the new offset to be used
     */
    public void setOffset(Vector2 offset) {
        this.offset = offset;
        create();
    }

    /**
     * Attach the light to the entity's physics body if that option was specified.
     */
    @Override
    public void create() {
        if (attachToPhysicsBody) {
            light.attachToBody(entity.getComponent(PhysicsComponent.class).getBody(), offset.x, offset.y, 0);
        }
    }

    /**
     * Update the position (and direction in future) of light to match the entity.
     * This is only necessary if the entity doesn't have a body.
     */
    @Override
    public void update() {
        if (!attachToPhysicsBody) {
            light.setPosition(entity.getCenterPosition().add(offset));
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
}
