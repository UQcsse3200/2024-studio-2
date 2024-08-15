package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnakeScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SnakeScreen.class);

    private final GdxGame game;
    private final Renderer renderer;
    private Texture imageTexture;

    public SnakeScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initializing Snake Screen services");

        // Register essential services
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        // Create renderer
        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(0f, 0f);

        createUI();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }



    @Override
    public void dispose() {
        logger.debug("Disposing Snake screen");


        if (imageTexture != null) {
            imageTexture.dispose();
        }

        renderer.dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.clear();
    }

    /**
     * Creates the UI for the Snake screen.
     */
    private void createUI() {
        logger.debug("Creating Snake screen UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        stage.clear();


        imageTexture = new Texture("images/SnakeImage.jpg");


        Image image = new Image(imageTexture);


        image.setSize(stage.getWidth(), stage.getHeight());
        image.setPosition(0, 0);


        stage.addActor(image);


    }
}

