package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;

import static java.lang.Math.atan2;

/**
 * Generalised animation controller for enemies.
 */
public class EnemyAnimationController extends Component {
    AnimationRenderComponent animator;
    private boolean direction = false; //left false, right true
    private boolean hasAngledAnimation;
    
    /**
     * @param hasAngledAnimation whether or not the animation has an angled (pointing to 45 degree) animation
     */
    public EnemyAnimationController(boolean hasAngledAnimation) {
        this.hasAngledAnimation = hasAngledAnimation;
    }
    
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
        entity.getEvents().addListener("animate", this::animate);
        entity.getEvents().addListener(WAIT, this::animateWait);
        entity.getEvents().addListener(RUNUP, this::animateRunUp);
        entity.getEvents().addListener(RUNDOWN, this::animateRunDown);
        entity.getEvents().addListener(RUNLEFT, this::animateRunLeft);
        entity.getEvents().addListener(RUNRIGHT, this::animateRunRight);
        if (hasAngledAnimation) {
            entity.getEvents().addListener(RUNLEFTUP, this::animateRunLeftUp);
            entity.getEvents().addListener(RUNRIGHTUP, this::animateRunRightUp);
            entity.getEvents().addListener(RUNLEFTDOWN, this::animateRunLeftDown);
            entity.getEvents().addListener(RUNRIGHTDOWN, this::animateRunRightDown);
        } else {
            entity.getEvents().addListener(RUNLEFTUP, this::animateRunLeft);
            entity.getEvents().addListener(RUNRIGHTUP, this::animateRunRight);
            entity.getEvents().addListener(RUNLEFTDOWN, this::animateRunLeft);
            entity.getEvents().addListener(RUNRIGHTDOWN, this::animateRunRight);
        }
    }
    
    private void animate(Vector2 targetPos, Vector2 startPos) {
        float deltaX = targetPos.x - startPos.x;
        float deltaY = targetPos.y - startPos.y;
        try {
            double angle = atan2(deltaX, deltaY);
            if (angle < -7 * Math.PI / 8) {
                animateRunDown();
            } else if (angle < -5 * Math.PI / 8) {
                direction = false;
                if (hasAngledAnimation) animateRunLeftDown();
                else animateRunLeft();
            } else if (angle < -3 * Math.PI / 8) {
                direction = false;
                animateRunLeft();
            } else if (angle < -1 * Math.PI / 8) {
                direction = false;
                if (hasAngledAnimation) animateRunLeftUp();
                else animateRunLeft();
            } else if (angle < 1 * Math.PI / 8) {
                animateRunUp();
            } else if (angle < 3 * Math.PI / 8) {
                direction = true;
                if (hasAngledAnimation) animateRunRightUp();
                else animateRunRight();
            } else if (angle < 5 * Math.PI / 8) {
                direction = true;
                animateRunRight();
            } else if (angle < 7 * Math.PI / 8) {
                direction = true;
                if (hasAngledAnimation) animateRunRightDown();
                else animateRunRight();
            } else {
                animateRunDown();
            }
        } catch (NullPointerException e) {
            throw new NullPointerException(entity.getEnemyType().toString() + "animation has not been started fro this type");
        }
    }
    
    private boolean isCurrentAnimation(String animationName) {
        return animator.getCurrentAnimation().equals(animationName) && !animator.getFlipX() && !animator.getFlipY();
    }
    
    private void animateRunDown() {
        if (isCurrentAnimation(RUNDOWN)) return;
        resetFlip();
        
        if (animator.hasAnimation(RUNDOWN)) {
            //animator has this animation, start playing it if it isn't already playing
            animator.startAnimation(RUNDOWN);
        } else {
            if (direction) animateRunRight();
            else animateRunLeft();
        }
    }
    
    private void animateRunUp() {
        if (isCurrentAnimation(RUNUP)) return;
        resetFlip();
        
        if (animator.hasAnimation(RUNUP)) {
            //animator has this animation, start playing it if it isn't already playing
            animator.startAnimation(RUNUP);
        } else {
            if (direction) animateRunRight();
            else animateRunLeft();
        }
    }
    
    private void animateRunLeft() {
        //prevent restarting the animation
        if (isCurrentAnimation(RUNLEFT)) return;
        if (animator.getFlipX() && animator.getCurrentAnimation().equals(RUNRIGHT)) return;
        
        resetFlip();
        
        if (animator.hasAnimation(RUNLEFT)) {
           
            animator.startAnimation(RUNLEFT);
        } else if (animator.hasAnimation(RUNRIGHT)) {
            //doesn't have animation but has opposite direction animation, start playing it if it isn't already playing
            animator.startAnimation(RUNRIGHT);
            animator.setFlipX(true);
        } else {
            throw new GdxRuntimeException("Does not have at least one directional animation");
        }
    }
    
    private void animateRunRight() {
        //prevent restarting the animation
        if (isCurrentAnimation(RUNRIGHT)) return;
        if (animator.getFlipX() && animator.getCurrentAnimation().equals(RUNLEFT)) return;
        
        resetFlip();
        
        if (animator.hasAnimation(RUNRIGHT)) {
            
            animator.startAnimation(RUNRIGHT);
        } else if (animator.hasAnimation(RUNLEFT)) {
            //doesn't have animation but has opposite direction animation, start playing it if it isn't already playing
            animator.startAnimation(RUNLEFT);
            animator.setFlipX(true);
        } else {
            throw new GdxRuntimeException("Does not have at least one directional animation");
        }
    }

    
    //terrible names and kinda ugly implementation, but needed as some sprite sheets dont have all directions
    //(maybe have 1 or 2, only some have all 4)
    private void animateDiagonal(String first, String second, String third) {
        if (animator.hasAnimation(first)) {
            animator.startAnimation(first);
            animator.setFlipX(true);
        } else if (animator.hasAnimation(second)) {
            animator.startAnimation(second);
            animator.setFlipY(true);
        } else {
            animator.startAnimation(third);
            animator.setFlipX(true);
            animator.setFlipY(true);
        }
    }
    
    private void animateRunLeftDown() {
        direction = false;
        if (isCurrentAnimation(RUNLEFTDOWN)) return;
        resetFlip();
        
        if (animator.hasAnimation(RUNLEFTDOWN)) {
            animator.startAnimation(RUNLEFTDOWN);
        } else {
            animateDiagonal(RUNRIGHTDOWN, RUNLEFTUP, RUNRIGHTUP);
        }
    }

    private void animateRunRightDown() {
        direction = true;
        if (isCurrentAnimation(RUNRIGHTDOWN)) return;
        resetFlip();
        
        if (animator.hasAnimation(RUNRIGHTDOWN)) {
            animator.startAnimation(RUNRIGHTDOWN);
        } else {
            animateDiagonal(RUNLEFTDOWN, RUNRIGHTUP, RUNLEFTUP);
        }
    }
    
    private void animateRunLeftUp() {
        direction = true;
        if (isCurrentAnimation(RUNLEFTUP)) return;
        resetFlip();
        
        if (animator.hasAnimation(RUNLEFTUP)) {
            animator.startAnimation(RUNLEFTUP);
        } else {
            animateDiagonal(RUNRIGHTUP, RUNLEFTDOWN, RUNRIGHTDOWN);
        }
    }
    
    private void animateRunRightUp() {
        direction = true;
        if (isCurrentAnimation(RUNRIGHTUP)) return;
        resetFlip();
        
        if (animator.hasAnimation(RUNRIGHTUP)) {
            animator.startAnimation(RUNRIGHTUP);
        } else {
            animateDiagonal(RUNLEFTUP, RUNRIGHTDOWN, RUNLEFTDOWN);
        }
    }
    
    private void animateWait() {
        animator.startAnimation(WAIT);
    }
    
    private void resetFlip() {
        animator.setFlipX(false);
        animator.setFlipY(false);
    }
    
}
