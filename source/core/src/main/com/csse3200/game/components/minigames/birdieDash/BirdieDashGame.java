package com.csse3200.game.components.minigames.birdieDash;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.minigames.MiniGameNames;
import com.csse3200.game.components.minigames.MinigameRenderer;
import com.csse3200.game.components.minigames.birdieDash.collision.CollisionHandler;
//import com.csse3200.game.components.minigames.birdieDash.controller.BirdieDashController;
import com.csse3200.game.components.minigames.birdieDash.entities.Bird;
import com.csse3200.game.components.minigames.birdieDash.entities.Coin;
import com.csse3200.game.components.minigames.birdieDash.entities.Pipe;
import com.csse3200.game.components.minigames.birdieDash.entities.Spike;
import com.csse3200.game.components.minigames.birdieDash.rendering.*;
import com.csse3200.game.screens.EndMiniGameScreen;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.GdxGameManager;

import java.util.ArrayList;
import java.util.List;

public class BirdieDashGame {

    private final int GAME_WIDTH = 1920;
    private final int GAME_HEIGHT = 1200;
    // Initial speed of the game
    private final float START_SPEED = 200;

    private final List<Pipe> pipes;
    private final List<Coin> coins;
    private final Bird bird;
    private final Spike spike;
    private final MinigameRenderer renderer;
   // private final BirdieDashController controller;
    private final CollisionHandler collisionHandler;
    private float speedMultiplier = 1.0f;
    private float accelerationRate = 0.05f;
    private Boolean isGameOver;

    public BirdieDashGame() {
        this.pipes = createPipes();
        this.coins = createCoins();
        this.bird = new Bird(920, 600);
        this.spike = new Spike(0);
        this.renderer = new MinigameRenderer();
        this.isGameOver = false;
      //  this.controller = new BirdieDashController();
        this.collisionHandler = new CollisionHandler(bird, pipes, coins, spike);
        initRenderers();
    }

    /**
     * Private method to set up game renderers
     */
    private void initRenderers() {
        ServiceLocator.registerResourceService(new ResourceService());
        renderer.addRenderable(new BackgroundRenderer(renderer));
        renderer.addRenderable(new PipeRenderer(pipes, renderer));
        renderer.addRenderable(new CoinRenderer(coins, renderer));
        renderer.addRenderable(new BirdRenderer(bird, renderer));
        renderer.addRenderable(new SpikeRenderer(spike, renderer));
        ServiceLocator.getResourceService().loadTextures(new String[]{"images/PauseOverlay/TitleBG.png",
                "images/PauseOverlay/Button.png",
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
            coins.add(new Coin(1920 + 960 * i, START_SPEED));
        }
        return coins;
    }

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
            pipes.add(new Pipe(1440 + 960 * i, START_SPEED));
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
        speedMultiplier += accelerationRate * dt;
        collisionHandler.checkCollisions();
        if (collisionHandler.checkSpikes() || collisionHandler.checkBoundary()) { // TODO || boundaryDetection
            isGameOver = true;
        }
        changePipePosition(dt * speedMultiplier);
        changeCoinPosition(dt * speedMultiplier);
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
     * @param dt change in time
     */
    public void render(float dt) {
        renderer.render();
    }

    /**
     * Public method to trigger the bird's flap action
     */
    public void flapBird() {
        bird.flapp();
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
