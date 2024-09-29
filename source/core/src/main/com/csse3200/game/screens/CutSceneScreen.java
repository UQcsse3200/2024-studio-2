package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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
}
