package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.combat.quicktimeevent.QuickTimeEventActions;
import com.csse3200.game.components.combat.quicktimeevent.QuickTimeEventDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A screen dedicated to practicing quick-time events
 * used in main-game combat
 */
public class QuickTimeEventScreen extends ResizableScreen {
    private static final Logger logger = LoggerFactory.getLogger(QuickTimeEventScreen.class);
    private GdxGame game;
    private final Renderer renderer;
    private Screen oldScreen = null ;
    private ServiceContainer oldScreenServices = null;
    private Entity player = null;
    private Entity enemy = null;
    private final PhysicsEngine physicsEngine;




    public QuickTimeEventScreen(GdxGame game, Screen screen, ServiceContainer container, Entity player, Entity enemy)

    {
        this.game = game;
        this.oldScreen = oldScreen;
        this.oldScreenServices = oldScreenServices;
        this.player = player;
        this.enemy = enemy;
        logger.debug("Initialising QuickTimeEvent screen services");
        ServiceLocator.registerTimeSource(new GameTime());
        physicsEngine = ServiceLocator.getPhysicsService().getPhysics();
        // Rendering
        renderer = RenderFactory.createRenderer();
        renderer.getStage().setViewport(
                new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        );
        loadAssets();
        createUI();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
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
    public void dispose() {
        logger.debug("Disposing QuickTimeEvent screen");

        renderer.dispose();
        unloadAssets();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        ServiceLocator.clear();
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
        resourceService.unloadAssets(new String[]{"sounds/victory.mp3"});

    }
    /**
     * Creates the main menu's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        QuickTimeEventDisplay QteDisplay = new QuickTimeEventDisplay();

        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForCombat();
        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
          .addComponent(QteDisplay)
          .addComponent(inputComponent)
          .addComponent(new QuickTimeEventActions(game,oldScreen,oldScreenServices,player,enemy, QteDisplay));
        ServiceLocator.getEntityService().register(ui);
        ui.getComponent(QuickTimeEventActions.class).count = 4;
        ui.getComponent(QuickTimeEventActions.class).update();
        int QTE_exitScore = ui.getComponent(QuickTimeEventDisplay.class).getQTEScore();
    }
}
