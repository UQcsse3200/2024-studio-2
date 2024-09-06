package com.csse3200.game.components.minigames.birdieDash;

import com.csse3200.game.components.minigames.MinigameRenderer;
import com.csse3200.game.components.minigames.birdieDash.entities.Pipe;
import com.csse3200.game.components.minigames.birdieDash.rendering.PipeRenderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class BirdieDashGame {

    private final int GAME_WIDTH = 1920;
    private final int GAME_HEIGHT = 1200;
    // Initial speed of the game
    private final float START_SPEED = 60;

    private final List<Pipe> pipes;
    private final MinigameRenderer renderer;
    // Just to test our things are appearing first
    public BirdieDashGame() {
        this.pipes = createPipes();
        this.renderer = new MinigameRenderer();
        initRenderers();
    }

    /**
     * Private method to set up game renderers
     */
    private void initRenderers() {
        ServiceLocator.registerResourceService(new ResourceService());
        renderer.addRenderable(new PipeRenderer(pipes, renderer));
        // Add all the other ones e.g. bird, coins etc.
    }
    /**
     * Private method to create pipes for the start of the game
     * @return a list containing all the pipe objects to be used.
     */
    private List<Pipe> createPipes() {
        List<Pipe> pipes = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            // Only need three pipes
            pipes.add(new Pipe(960 + 800 * i, START_SPEED));
        }
        return pipes;
    }

    /**
     * Private method to change all the pipe's positions based of the change in
     * game time
     * @param dt the change in game time
     */
    private void changePipePosition(float dt) {
        for(Pipe pipe : pipes) {
            pipe.changePosition(dt);
        }
    }

    /**
     * Private method to update all positions of entites in the game
     * @param dt the change in game time
     */
    private void updateGamePosition(float dt) {
        changePipePosition(dt);
        // Add all other change positions here e.g. bird, coins etc.
    }

    /**
     * public method to render the game objects
     * @param dt change in time
     */
    public void render(float dt) {
        updateGamePosition(dt);
        renderer.render();
    }
}
