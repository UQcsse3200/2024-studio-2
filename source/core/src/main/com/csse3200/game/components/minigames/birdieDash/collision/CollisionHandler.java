package com.csse3200.game.components.minigames.birdieDash.collision;

import com.csse3200.game.components.minigames.birdieDash.entities.Bird;
import com.csse3200.game.components.minigames.birdieDash.entities.Pipe;
import com.csse3200.game.components.minigames.birdieDash.entities.Spike;

import java.util.List;

public class CollisionHandler {
    private final Bird bird;
    private List<Pipe> pipes;
    private final Spike spike;
    final float epsilon = 5f;


    /**
     * Class to check and handle collisions.
     * TO DO: add the coins as well
     * @param bird the bird
     * @param pipes the pipes
     * @param spike the spikes
     */
    public CollisionHandler(Bird bird, List<Pipe> pipes, Spike spike) {
        this.bird = bird;
        this.pipes = pipes;
        this.spike = spike;
    }

    public void checkCollisions() {
        checkPipes();
        checkCoin();
        checkSpikes();
    }

    /**
     * Check collisions via overlap, then do something
     * Need to change as currently bird gets pushed back even if hits top of pipe.
     */
    private void checkPipes() {
        for (Pipe pipe : pipes) {
            if(bird.getBoundingBox().overlaps(pipe.getBottomPipe()) || bird.getBoundingBox().overlaps(pipe.getTopPipe())) {
                if(isApproximatelyEqual(bird.getPosition().y,
                        pipe.getPositionBottom().y+pipe.getBottomPipe().height, epsilon)) {
                    bird.setCollidingTopPipe();
                    return;
                }
                bird.setCollidingPipe();
                return;
            }
        }
        bird.unsetCollidingPipe();
        bird.unsetCollidingTopPipe();
    }

    boolean isApproximatelyEqual(float a, float b, float epsilon) {
        return Math.abs(a - b) < epsilon;
    }


    /**
     * Check collision for coin via overlap, then do something
     */
    private void checkCoin() {

    }

    /**
     * Check collision for spikes via overlap, then do something
     */
    private void checkSpikes() {

    }


}
