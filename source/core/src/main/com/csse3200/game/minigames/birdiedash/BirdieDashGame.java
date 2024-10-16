package com.csse3200.game.minigames.birdiedash;

import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.birdiedash.collision.CollisionHandler;
import com.csse3200.game.minigames.birdiedash.entities.*;
import com.csse3200.game.minigames.birdiedash.rendering.*;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for the birdie dash game mechanics
 */
public class BirdieDashGame {

    // Initial speed of the game
    private final float startSpeed = 200;

    // Items on the screen
    private final List<Pipe> pipes;
    private final List<Coin> coins;
    private final Bird bird;
    private final Spike spike;
    private final Background background;
    private final BirdRenderer birdRenderer;

    private final MinigameRenderer renderer;  // Mini-game renderer
    private final CollisionHandler collisionHandler; // Collision detection
    private float speedMultiplier = 1.0f; // Multiplier to increase the speed
    private Boolean isGameOver; // Used to track if the game is over

    public BirdieDashGame() {
        this.pipes = createPipes();
        this.coins = createCoins();
        this.bird = new Bird(920, 1200);
        this.spike = new Spike(0);
        this.background = new Background(startSpeed / 3, 1920);
        this.renderer = new MinigameRenderer();
        this.birdRenderer = new BirdRenderer(bird, renderer);
        this.isGameOver = false;
        this.collisionHandler = new CollisionHandler(bird, pipes, coins, spike);
        initRenderers();
    }

    /**
     * Private method to set up game renderers
     */
    private void initRenderers() {
        ServiceLocator.registerResourceService(new ResourceService());
        renderer.addRenderable(new BackgroundRenderer(background, renderer));
        renderer.addRenderable(new PipeRenderer(pipes, renderer));
        renderer.addRenderable(new CoinRenderer(coins, renderer));
        renderer.addRenderable(birdRenderer);
        renderer.addRenderable(new SpikeRenderer(spike, renderer));
        ServiceLocator.getResourceService().loadTextures(new String[]{"images/PauseOverlay/TitleBG.png",
                "images/PauseOverlay/Button2.png",
                "images/QuestsOverlay/Quest_BG.png",
                "images/QuestsOverlay/Quest_SBG.png"});
        ServiceLocator.getResourceService().loadAll();
    }

    /**
     * Private method to create a list of coins
     * @return the list of coins
     */
    private List<Coin> createCoins() {
        List<Coin> coins = new ArrayList<>();
        for(int i= 0; i < 3; i++) {
            coins.add(new Coin((float) 1920 + 960 * i, startSpeed));
        }
        return coins;
    }

    /**
     * Change the coins position
     * @param dt time since last coin position
     */
    private void changeCoinPosition(float dt) {
        for(Coin coin : coins) {
            coin.changePosition(dt);
        }
    }

    /**
     * Private method to create pipes for the start of the game
     * @return a list containing all the pipe objects to be used.
     */
    private List<Pipe> createPipes() {
        List<Pipe> pipes = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            // Only need three pipes
            pipes.add(new Pipe((float) 1440 + 960 * i, startSpeed));
        }
        return pipes;
    }

    /**
     * Private method to change all the pipe's positions based on the change in
     * game time
     * @param dt the change in game time
     */
    private void changePipePosition(float dt) {
        for(Pipe pipe : pipes) {
            pipe.changePosition(dt);
        }
    }

    /**
     * Private method to update all positions of entities in the game
     * @param dt the change in game time
     */
    private void updateGamePosition(float dt) {
        // Mutiplier for acceleration of the bird
        float accelerationRate = 0.05f;
        speedMultiplier += accelerationRate * dt;
        collisionHandler.checkCollisions();
        if (collisionHandler.checkSpikes() || bird.touchingFloor()) {
            isGameOver = true;
        }
        changePipePosition(dt * speedMultiplier);
        changeCoinPosition(dt * speedMultiplier);
        background.update(dt * speedMultiplier);
        bird.update(dt, speedMultiplier);
        // Add all other change positions here e.g. bird, coins etc.
    }

    /**
     * Public method to update the game objects (i.e. physics)
     * @param dt change in time
     */
    public void update(float dt) {
        updateGamePosition(dt);
    }

    /**
     * Public method to render the game objects
     */
    public void render() {
        renderer.render();
    }

    /**
     * Public method to trigger the bird's flap action
     */
    public void flapBird() {
        bird.flap();
        birdRenderer.flap();
    }

    /**
     * Gets the game score
     * @return the game score
     */
    public int getScore() {
        return collisionHandler.getScore();
    }

    /**
     * Determines if the game is over
     * @return true if game is over, false otherwise
     */
    public Boolean getIsGameOver() {
        return isGameOver;
    }
}
