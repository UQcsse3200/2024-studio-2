package com.csse3200.game.components.minigames.birdieDash.collision;

import com.csse3200.game.components.minigames.birdieDash.entities.Bird;
import com.csse3200.game.components.minigames.birdieDash.entities.Coin;
import com.csse3200.game.components.minigames.birdieDash.entities.Pipe;
import com.csse3200.game.components.minigames.birdieDash.entities.Spike;

import java.util.List;

/**
 * Class to detect and track collisions in birdie dash mini-game
 */
public class CollisionHandler{
    private final Bird bird; // The bird

    // Items to collide with
    private final List<Pipe> pipes;
    private final List<Coin> coins;
    private final Spike spike;

    private int score;  // To keep track of the player's score
    final float epsilon = 5f; // Used as to detect collision
    private boolean collisionDetected; // To track collision
    private boolean overlapping; // To track overlapping

    public CollisionHandler(Bird bird, List<Pipe> pipes, List<Coin> coins, Spike spike) {
        this.bird = bird;
        this.pipes = pipes;
        this.coins = coins;
        this.spike = spike;
        this.score = 0; // Initialize the score
        this.collisionDetected = false;
        this.overlapping = false;
    }

    /**
     * Checks if collisions with non-game terminating objects
     */
    public void checkCollisions() {
        checkPipes();
        checkCoin();
    }

    /**
     * Checks if the bird has collided with the pipes
     */
    private void checkPipes() {
        overlapping = false;
        for (Pipe pipe : pipes) {
            if (bird.getBoundingBox().overlaps(pipe.getBottomPipe()) || bird.getBoundingBox().overlaps(pipe.getTopPipe())) {
                overlapping = true;
                if(!collisionDetected) {
                    float bottomPipeY = pipe.getPositionBottom().y + pipe.getBottomPipe().height;
                    float topPipeY = pipe.getPositionTop().y;
                    if (isApproximatelyEqual(bird.getPosition().y, bottomPipeY)) {
                        bird.setCollidingTopPipe(bottomPipeY);
                        collisionDetected = true;
                        return;
                    } else if (isApproximatelyEqual(bird.getPosition().y + bird.getBirdHeight(),
                            topPipeY)) {
                        bird.setCollidingBottomPipe(topPipeY);
                        collisionDetected = true;
                        return;
                    }
                    collisionDetected = true;
                    bird.setCollidingPipe();
                    return;
                }
            }
        }
        if (!overlapping) {
            collisionDetected = false;
            bird.unsetCollidingPipe();
            bird.unsetCollidingTopPipe();
        }
    }

    /**
     * Checks if bird has collided with a coin
     */
    private void checkCoin() {
        for (Coin coin : coins) {
            if (bird.getBoundingBox().overlaps(coin.getBoundary())) {
                score++;
                coin.respawnCoin();
            }
        }
    }

    /**
     * Determines if the bird is touching the spikes
     * @return tru if bird is touching spikes otherwise false.
     */
    public Boolean checkSpikes() {
        return bird.getBoundingBox().overlaps(spike.getSpikeBoundary());
    }

    /**
     * Used for detection of collision
     * @param a item to check collision with
     * @param b item to check collision with
     * @return if the value is approximately equal to (true if so, otherwise false)
     */
    private boolean isApproximatelyEqual(float a, float b) {
        return Math.abs(a - b) < epsilon;
    }

    /**
     * Gets the current game score
     * @return the score
     */
    public int getScore() {
        return score;
    }
}
