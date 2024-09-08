package com.csse3200.game.components.minigames.birdieDash.collision;

import com.csse3200.game.components.minigames.birdieDash.entities.Bird;
import com.csse3200.game.components.minigames.birdieDash.entities.Pipe;
import com.csse3200.game.components.minigames.birdieDash.entities.Spike;

import java.util.List;

public class CollisionHandler {
    private final Bird bird;
    private List<Pipe> pipes;
    private final Spike spike;

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
                bird.setCollidingPipe();
                return;
            }
        }
        bird.unsetCollidingPipe();
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
