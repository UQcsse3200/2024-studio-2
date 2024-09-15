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
    private final double proximityRange;


    /**
     * @param target         the player entity, for detecting if it is in range
     * @param proximityRange the range in which the player will cause the entity to
     *                       trigger the spawn animation and become enabled.
     */
    public ProximityComponent(Entity target, double proximityRange) {
        this.target = target;
        this.proximityRange = proximityRange;
    }

    /**
     * Updates the task's state based on the entity's position relative to the target.
     * Triggers the "proximityTriggered" event when the entity is within the proximity range.
     */
    @Override
    public void update() {
        if (Vector2.dst(entity.getPosition().x, entity.getPosition().y,
                target.getPosition().x, target.getPosition().y) < proximityRange) {
            this.entity.getEvents().trigger("proximityTriggered");
            this.entity.setEnabled(true); // Enable the entity when in proximity

        }
    }

    /**
     * Sets up the spawn animation for the given entity.
     * Disables the entity until proximity is triggered, then starts the spawn animation and transitions to walking after completion.
     *
     * @param entity the entity to set up the spawn animation for
     */

//    void setupSpawnAnimation(Entity entity) {
////        entity.setEnabled(false); // Disable the entity until proximity is triggered
//
//        entity.getEvents().addListener("proximityTriggered", () -> {
//            AnimationRenderComponent animationComponent = entity.getComponent(AnimationRenderComponent.class);
//            animationComponent.startAnimation("spawn");
////            entity.update();
//
////             Listen for when the spawn animation ends
//            entity.getEvents().addListener("animationEnd", (String animationName) -> {
//                if (animationName.equals("spawn")) {
//                   animationComponent.startAnimation("walk");
//                }
//            });
//        });
//    }
}
