package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to the Wind Gust projectile's movement and plays the
 * corresponding animation when the event is triggered. The wind gust is an attack or projectile
 * used by certain entities, and the animation plays as the projectile moves.
 */
public class WindGustAnimationController extends Component {
    // The animator responsible for rendering the wind gust animation.
    private AnimationRenderComponent animator;

    /**
     * Called when the component is created. This method retrieves the AnimationRenderComponent
     * and sets up an event listener for the wind gust's movement to trigger its animation.
     */
    @Override
    public void create() {
        super.create();
        // Get the AnimationRenderComponent associated with the wind gust projectile
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        // Listen for the "ProjectileMove" event to start the wind gust animation
        entity.getEvents().addListener("ProjectileMove", this::animateWindGust);
    }

    /**
     * Plays the wind gust animation when the "ProjectileMove" event is triggered.
     */
    private void animateWindGust() {
        animator.startAnimation("windGust");
    }
}
