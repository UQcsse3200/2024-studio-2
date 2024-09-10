package com.csse3200.game.components.minigames.birdieDash.collision;

import com.csse3200.game.components.minigames.birdieDash.entities.Bird;
import com.csse3200.game.components.minigames.birdieDash.entities.Coin;
import com.csse3200.game.components.minigames.birdieDash.entities.Pipe;
import com.csse3200.game.components.minigames.birdieDash.entities.Spike;

import java.util.List;

public class CollisionHandler{
    private final Bird bird;
    private final List<Pipe> pipes;
    private final List<Coin> coins;
    private final Spike spike;
    private int score;  // To keep track of the player's score
    final float epsilon = 5f;
    private boolean collisionDetected;
    private boolean overlapping;

    public CollisionHandler(Bird bird, List<Pipe> pipes, List<Coin> coins, Spike spike) {
        this.bird = bird;
        this.pipes = pipes;
        this.coins = coins;
        this.spike = spike;
        this.score = 0; // Initialize the score
        this.collisionDetected = false;
        this.overlapping = false;
    }

    public void checkCollisions() {
        checkPipes();
        checkCoin();
        //checkSpikes();
    }

    private void checkPipes() {
        overlapping = false;
        for (Pipe pipe : pipes) {
            if (bird.getBoundingBox().overlaps(pipe.getBottomPipe()) || bird.getBoundingBox().overlaps(pipe.getTopPipe())) {
                overlapping = true;
                if(!collisionDetected) {
                    float bottomPipeY = pipe.getPositionBottom().y + pipe.getBottomPipe().height;
                    float topPipeY = pipe.getPositionTop().y;
                    if (isApproximatelyEqual(bird.getPosition().y, bottomPipeY, epsilon)) {
                        bird.setCollidingTopPipe(bottomPipeY);
                        collisionDetected = true;
                        return;
                    } else if (isApproximatelyEqual(bird.getPosition().y + bird.getBirdHeight(),
                            topPipeY, epsilon)) {
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
            bird.unsetCollidingBottomPipe();
        }
    }

    private void checkCoin() {
        for (Coin coin : coins) {
            if (bird.getBoundingBox().overlaps(coin.getBoundary())) {
                // Increment the score and respawn the coin
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

    public Boolean checkBoundary() {
        //return bird.getBoundingBox().overlaps();
        return false;
    }

    private boolean isApproximatelyEqual(float a, float b, float epsilon) {
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
