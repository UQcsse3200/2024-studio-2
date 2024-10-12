package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * Generalised animation controller for enemies.
 */
public class EnemyAnimationController extends Component {
    AnimationRenderComponent animator;
    
    public static final String RUNDOWN = "runDown";
    public static final String RUNUP = "runUp";
    public static final String RUNLEFT = "runLeft";
    public static final String RUNRIGHT = "runRight";
    public static final String RUNLEFTUP = "runLeftUp";
    public static final String RUNRIGHTUP = "runRightUp";
    public static final String RUNLEFTDOWN = "runLeftDown";
    public static final String RUNRIGHTDOWN = "runRightDown";
    public static final String WAIT = "wait";
    
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(RUNDOWN, this::animateRunDown);
        entity.getEvents().addListener(RUNUP, this::animateRunUp);
        entity.getEvents().addListener(RUNLEFT, this::animateRunLeft);
        entity.getEvents().addListener(RUNRIGHT, this::animateRunRight);
        entity.getEvents().addListener(RUNLEFTDOWN, this::animateRunLeftDown);
        entity.getEvents().addListener(RUNRIGHTDOWN, this::animateRunRightDown);
        entity.getEvents().addListener(RUNLEFTUP, this::animateRunLeftUp);
        entity.getEvents().addListener(RUNRIGHTUP, this::animateRunRightUp);
        entity.getEvents().addListener(WAIT, this::animateWait);
    }
    
    //The following methods will be updated with new animations in future sprints,
    //so are currently placeholders
    
    private void animateRunDown() {
        if (animator.hasAnimation(RUNDOWN)) {
            animator.startAnimation(RUNDOWN);
        } else {
        
        }
    }
    
    private void animateRunUp() {
        animator.startAnimation(RUNUP);
    }
    
    private void animateRunLeft() {
        if (animator.hasAnimation(RUNLEFT)) {
            //animator has this animation, start playing it if it isn't already playing
            if (animator.getCurrentAnimation().equals(RUNLEFT)) return;
            animator.startAnimation(RUNLEFT);
        } else if (animator.hasAnimation(RUNRIGHT)) {
            //doesn't have animation but has opposite direction animation, start playing it if it isn't already playing
            if (animator.getCurrentAnimation().equals(RUNRIGHT)) return;
            animator.startAnimation(RUNRIGHT);
            animator.setFlipX(true);
        }
    }
    
    private void animateRunRight() {
        if (animator.hasAnimation(RUNRIGHT)) {
            //animator has this animation, start playing it if it isn't already playing
            if (animator.getCurrentAnimation().equals(RUNRIGHT)) return;
            animator.startAnimation(RUNRIGHT);
        } else if (animator.hasAnimation(RUNLEFT)) {
            //doesn't have animation but has opposite direction animation, start playing it if it isn't already playing
            if (animator.getCurrentAnimation().equals(RUNLEFT)) return;
            animator.startAnimation(RUNLEFT);
            animator.setFlipX(true);
        }
    }
    
    private void animateRunLeftDown() {
        animator.startAnimation(RUNLEFTDOWN);
    }
    
    private void animateRunRightDown() {
        animator.startAnimation(RUNRIGHTDOWN);
    }
    
    private void animateRunLeftUp() {
        animator.startAnimation(RUNLEFTUP);
    }
    
    private void animateRunRightUp() {
        animator.startAnimation(RUNRIGHTUP);
    }
    
    private void animateWait() {
        animator.startAnimation(WAIT);
    }
    
}
