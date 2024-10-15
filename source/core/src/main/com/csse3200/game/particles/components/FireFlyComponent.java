package com.csse3200.game.particles.components;

import box2dLight.Light;
import com.csse3200.game.components.Component;
import com.csse3200.game.lighting.DayNightCycle;
import com.csse3200.game.lighting.components.LightingComponent;

/**
 * Component for firefly visual effects. Firefly lighting and particles should only visible during night time.
 */
public class FireFlyComponent extends Component {
    private float maxDistance;
    private static final float fadeOutTime = 0.2f;
    private static final float fadeInTime = 0.8f;
    private static final float fadeDuration = 0.01f;

    private Light light;
    ParticleEffectComponent particles;

    public FireFlyComponent() {
        super();
    }

    @Override
    public void create() {
        light = entity.getComponent(LightingComponent.class).getLights().getFirst();
        particles = entity.getComponent(ParticleEffectComponent.class);
        maxDistance = light.getDistance();
        update();
    }

    /**
     * Updates the visual effects of the firefly using the current time of day from the DayNightCycle.
     */
    @Override
    public void update() {
        float timeOfDay = DayNightCycle.getTimeOfDay();
        float mid = (fadeInTime + fadeOutTime) / 2;
        float scaleY = 1/fadeDuration;
        float offsetY = - (fadeInTime - mid) * scaleY;
        float dist = Math.max(Math.min(Math.abs(timeOfDay - mid) * scaleY + offsetY, maxDistance), 0f);
        light.setDistance(dist);
        if (dist < 0.1f) {
            particles.allowCompletion();
        } else {
            particles.emit();
        }
    }
}
