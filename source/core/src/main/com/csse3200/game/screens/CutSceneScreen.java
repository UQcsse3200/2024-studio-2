package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CutSceneScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CutSceneScreen.class);
    private final GdxGame game;

    // Local instances of services
    private RenderService renderService;
    private EntityService entityService;
    private ResourceService resourceService;
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Texture cutSceneTexture; // Texture for the cutscene background


    public CutSceneScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initializing CutScene screen");

        // Initialize the services
        this.spriteBatch = new SpriteBatch();
        this.stage = new Stage();
        this.renderService = new RenderService();  // No arguments in constructor
        this.entityService = new EntityService();
        this.resourceService = new ResourceService();

        // Set the stage in the render service
        renderService.setStage(stage);

        // Set input processor
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        // Update entities and render the screen
        entityService.update();
        spriteBatch.begin();
        renderService.render(spriteBatch); // Trigger rendering through RenderService
        spriteBatch.end();
        stage.act(delta); // Update the stage
        stage.draw(); // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        renderService.getStage().getViewport().update(width, height, true);
    }
    private void loadAssets() {
        logger.debug("Loading CutScene assets");

        // Load cutscene assets (images, sounds, etc.)
        resourceService.loadAll();

        // Load the cutscene background image
        cutSceneTexture = new Texture(Gdx.files.internal("images/BackgroundSplashBasic.png")); // Update the path
    }
    private void createCutScene() {
        logger.debug("Creating CutScene UI");
    }

    @Override
    public void dispose() {
        logger.debug("Disposing CutScene screen");

        // Dispose of all resources and services
        renderService.dispose();
        entityService.dispose();
        resourceService.loadAll();  // Might need to change this depending on how assets are loaded
        spriteBatch.dispose();
        stage.dispose();
    }

}
