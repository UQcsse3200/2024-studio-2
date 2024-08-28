package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * Component class for spawning enemies when the player gets within a certain proximity.
 * Enemies exits before this, but are disabled until the player enters their proximity.
 */
public class ProximityComponent extends Component {
    private final Entity target;
    private final float proximityRange;

    /**
     * @param target the player entity, for detecting if it is in range
     * @param proximityRange the range in which the player will cause the entity to
     *                       trigger the spawn animation and become enabled.
     */
    public ProximityComponent(Entity target, float proximityRange) {
        this.target = target;
        this.proximityRange = proximityRange;
    }

    @Override
    public void update() {
        if (Vector2.dst(entity.getPosition().x, entity.getPosition().y,
                target.getPosition().x, target.getPosition().y) < proximityRange) {
            entity.getEvents().trigger("proximityTriggered");
            entity.setEnabled(true); // Enable the entity when in proximity
        }
    }

    private void setupSpawnAnimation(Entity entity) {
        entity.setEnabled(false); // Disable the entity until proximity is triggered
        entity.getEvents().addListener("proximityTriggered", () -> {
            AnimationRenderComponent animationComponent = entity.getComponent(AnimationRenderComponent.class);
            animationComponent.startAnimation("spawn");

            // Listen for when the spawn animation ends
            entity.getEvents().addListener("animationEnd", (String animationName) -> {
                if (animationName.equals("spawn")) {
                    animationComponent.startAnimation("walk"); // Transition to walking after spawn
                }
            });
        });
    }
}
