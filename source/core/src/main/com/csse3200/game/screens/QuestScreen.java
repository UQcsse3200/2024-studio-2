package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.quests.QuestDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The game screen containing the settings. */
public class QuestScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(QuestScreen.class);

    private final GdxGame game;
    private final Renderer renderer;
    private boolean showing = false;

    public QuestScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising Quest screen services");

        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(5f, 5f);

        createUI();
    }


    public void toggle() {
        if(showing){
            logger.info("Hiding quest game screen");
            renderer.dispose();
            ServiceLocator.getEntityService().hide();
            ServiceLocator.getRenderService().hide();
            ServiceLocator.getResourceService().hide();
            showing = false;
        }
        else {
            logger.info("Showing quest game screen");
            ServiceLocator.getResourceService().show();
            ServiceLocator.getEntityService().clearExtra();
            ServiceLocator.getEntityService().show();
            ServiceLocator.getRenderService().show();
            createUI();
            TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
            ForestGameArea forestGameArea = new ForestGameArea(terrainFactory);
            forestGameArea.create();
            showing = true;
        }
    }

    @Override
    public void pause() {
        logger.info("Game paused");
    }

    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();
    }

    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new QuestDisplay(game)).addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}
