package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.combat.quicktimeevent.QuickTimeEventActions;
import com.csse3200.game.components.combat.quicktimeevent.QuickTimeEventDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A screen dedicated to practicing quick-time events
 * used in main-game combat
 */
public class QuickTimeEventScreen extends ResizableScreen {
    private static final Logger logger = LoggerFactory.getLogger(QuickTimeEventScreen.class);
    private final GdxGame game;

    public QuickTimeEventScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising QuickTimeEvent screen services");
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());

        renderer.getStage().setViewport(
                new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        
        loadAssets();
        createUI();
    }
    
    @Override
    public void dispose() {
        logger.debug("Disposing QuickTimeEvent screen");

        unloadAssets();
        
        super.dispose();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadSounds(new String[]{"sounds/victory.mp3"});
        resourceService.loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(new String[]{"sounds/victory.mp3"}); //hope this doesn't break anything, I dont
                                                                          //know how to get to the quick time events
    }
    /**
     * Creates the main menu's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForCombat();
        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
          .addComponent(new QuickTimeEventDisplay())
          .addComponent(inputComponent)
          .addComponent(new QuickTimeEventActions(game));
        ServiceLocator.getEntityService().register(ui);
    }
}
