package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to the Water Spiral projectile's movement and plays the
 * corresponding animation when the event is triggered. The water spiral is an attack or projectile
 * used by certain entities, and the animation plays as the projectile moves.
 */
public class WaterSpiralAnimationController extends Component {
    // The animator responsible for rendering the water spiral animation.
    private AnimationRenderComponent animator;

    /**
     * Called when the component is created. This method retrieves the AnimationRenderComponent
     * and sets up an event listener for the water spiral's movement to trigger its animation.
     */
    @Override
    public void create() {
        super.create();
        // Get the AnimationRenderComponent associated with the water spiral projectile
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        // Listen for the "ProjectileMove" event to start the water spiral animation
        entity.getEvents().addListener("ProjectileMove", this::animateWaterSpiral);
    }

    /**
     * Plays the water spiral animation when the "ProjectileMove" event is triggered.
     */
    private void animateWaterSpiral() {
        animator.startAnimation("waterSpiral");
    }
}
