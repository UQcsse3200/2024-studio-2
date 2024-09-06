package com.csse3200.game.components.minigames.birdieDash;

import com.csse3200.game.components.minigames.MinigameRenderer;
import com.csse3200.game.components.minigames.birdieDash.entities.Pipe;

import java.util.ArrayList;
import java.util.List;

public class BirdieDashGame {

    // Inital speed of the game
    private final float START_SPEED = 60;

    private final List<Pipe> pipes;
    private final MinigameRenderer renderer;
    // Just to test our things are appearing first
    public BirdieDashGame() {
        this.pipes = createPipes();
        this.renderer = new MinigameRenderer();
    }

    /**
     * Private method to create pipes for the start of the game
     * @return a list containing all the pipe objects to be used.
     */
    private List<Pipe> createPipes() {
        List<Pipe> pipes = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            // Only need three pipes
            pipes.add(new Pipe(400 + 300 * i, START_SPEED));
        }
        return pipes;
    }
}
