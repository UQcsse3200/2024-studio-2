package com.csse3200.game.lighting.components;

import box2dLight.Light;
import com.csse3200.game.components.Component;
import com.csse3200.game.lighting.DayNightCycle;
import com.csse3200.game.services.ServiceLocator;

/**
 * Component that fades out . Assumes the entity only has one light source attached and this light
 * source is not removed while the entity exists.
 * Note: this component changes the distance of light sources.
 * The maximum distance the light source will reach is the distance of the light when this component
 * is created.
 */
public class FadeLightsDayTimeComponent extends Component {
    private float maxDistance;
    private static final float fadeOutTime = 0.35f;
    private static final float fadeInTime = 0.72f;
    private static final float fadeDuration = 0.01f;

    private Light light;

    public FadeLightsDayTimeComponent() {
        super();
    }

    @Override
    public void create() {
        light = entity.getComponent(LightingComponent.class).getLights().getFirst();
        maxDistance = light.getDistance();
        update();
    }

    @Override
    public void update() {
        DayNightCycle dayNightCycle = ServiceLocator.getDayNightCycle();
        float timeOfDay = dayNightCycle.getTimeOfDay();
        float mid = (fadeInTime + fadeOutTime) / 2;
        float scaleY = 1/fadeDuration;
        float offsetY = - (fadeInTime - mid) * scaleY;
        light.setDistance(Math.max(Math.min(Math.abs(timeOfDay - mid) * scaleY + offsetY, maxDistance), 0f));
    }
}
