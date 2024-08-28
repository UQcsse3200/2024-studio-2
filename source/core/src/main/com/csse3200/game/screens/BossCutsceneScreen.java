package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.Overlays.Overlay;
import com.csse3200.game.Overlays.PauseOverlay;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;

public class BossCutsceneScreen extends ScreenAdapter {
    private static final float CUTSCENE_DURATION = 3.0f; // Cutscene lasts for 3 seconds
    private float timeElapsed = 0;
    private boolean transition;

    private static final Logger logger = LoggerFactory.getLogger(BossCutsceneScreen.class);
    private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
    private boolean isPaused = false;
    private final GdxGame game;
    private final Renderer renderer;
    private final PhysicsEngine physicsEngine;
    private final Screen oldScreen;
    private final ServiceContainer oldScreenServices;
    private final Entity enemy;
    private final Deque<Overlay> enabledOverlays = new LinkedList<>();

    public BossCutsceneScreen(GdxGame game, Screen screen, ServiceContainer container, Entity enemy) {
        this.game = game;
        this.oldScreen = screen;
        this.oldScreenServices = container;
        this.enemy = enemy;

        logger.debug("Initializing boss cutscene screen services");
        ServiceLocator.registerTimeSource(new GameTime());

        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        physicsEngine = physicsService.getPhysics();

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        ServiceLocator.registerEventService(new EventService());

        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

        this.transition = false;
        createUI();

        ServiceLocator.getEventService().getGlobalEventHandler().addListener("addOverlay", this::addOverlay);
        ServiceLocator.getEventService().getGlobalEventHandler().addListener("removeOverlay", this::removeOverlay);
        logger.debug("Initialising main game dup screen entities");
        TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
        //this.gameArea = new ForestGameArea(terrainFactory, game);
        //this.gameArea.create();
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {
            physicsEngine.update();
            ServiceLocator.getEntityService().update();
            renderer.render();

            timeElapsed += delta;
            if (timeElapsed >= CUTSCENE_DURATION && !transition) {
                transition = true;
                logger.info("Cutscene finished, transitioning to combat screen");
                // dispose();
                game.setScreen(new CombatScreen(game, oldScreen, oldScreenServices, enemy));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    @Override
    public void pause() {
        isPaused = true;
        //gameArea.pauseMusic();
        logger.info("Game paused");
    }

    @Override
    public void resume() {
        isPaused = false;
        //gameArea.playMusic();
        logger.info("Game resumed");
    }

    @Override
    public void dispose() {
        logger.debug("Disposing cutscene screen");

        renderer.dispose();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();
        ServiceLocator.getEventService().dispose();

        ServiceLocator.clear();
    }

    /**
     * Creates and sets up the cutscene UI elements, including motion effects for the text and image.
     * Debugged and Developed with ChatGPT
     */
    private void createUI() {
        logger.debug("Creating cutscene UI");
        Stage stage = ServiceLocator.getRenderService().getStage();

        // Create black bars
        Texture topBarTexture = new Texture("images/black_bar.png");
        Texture bottomBarTexture = new Texture("images/black_bar.png");
        Texture enemyImageTexture = new Texture("images/final_boss_kangaroo_idle.png");

        Image topBar = new Image(topBarTexture);
        Image bottomBar = new Image(bottomBarTexture);

        topBar.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 6f);
        bottomBar.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 6f);

        topBar.setPosition(0, Gdx.graphics.getHeight() - topBar.getHeight());
        bottomBar.setPosition(0, 0);

        BitmapFont defaultFont = new BitmapFont();
        defaultFont.getData().setScale(2.0f);

        // Create label style with the font
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = defaultFont;
        labelStyle.fontColor = Color.BLACK;

        Label enemyNameLabel = new Label("Kanga", labelStyle);
        enemyNameLabel.setAlignment(Align.center);

        Image enemyImage = new Image(enemyImageTexture);

        // Animate enemy name label (flash effect)
        enemyNameLabel.addAction(
                Actions.sequence(
                        Actions.alpha(0f),
                        Actions.repeat(5,
                                Actions.sequence(
                                        Actions.fadeIn(0.2f),
                                        Actions.fadeOut(0.2f)
                                )
                        ),
                        Actions.fadeIn(0.2f) // Finally, keep it visible
                )
        );

        // Centered positions
        float centerX = (Gdx.graphics.getWidth() - 500) / 2f;
        float centerY = (Gdx.graphics.getHeight() - 500) / 2f;

        // Initial positions for sliding animations
        enemyImage.setPosition(0, centerY); // Start from off-screen left
        enemyNameLabel.setPosition(Gdx.graphics.getWidth(), centerY - 80); // Start from off-screen right

        // Animate enemy image (slide-in effect)
        enemyImage.addAction(
                Actions.sequence(
                        Actions.moveTo(centerX, centerY, 2f, Interpolation.pow5Out)
                )
        );

        // Animate enemy name label (slide-in effect)
        enemyNameLabel.addAction(
                Actions.sequence(
                        Actions.moveTo(centerX + 250, centerY - 80, 2f, Interpolation.pow5Out)
                )
        );

        // Table layout for positioning
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(enemyImage).width(500).height(500);
        table.row();
        table.add(enemyNameLabel);

        // Add actors to stage
        stage.addActor(topBar);
        stage.addActor(bottomBar);
        stage.addActor(table);
    }

    public void addOverlay(Overlay.OverlayType overlayType) {
        logger.info("Adding Overlay {}", overlayType);
        if (enabledOverlays.isEmpty()) {
            this.rest();
        } else {
            enabledOverlays.getFirst().rest();
        }
        switch (overlayType) {
            case PAUSE_OVERLAY:
                enabledOverlays.addFirst(new PauseOverlay());
                break;
            default:
                logger.warn("Unknown Overlay type: {}", overlayType);
                break;
        }
    }

    public void removeOverlay() {
        logger.debug("Removing top Overlay");

        if (enabledOverlays.isEmpty()) {
            this.wake();
            return;
        }

        enabledOverlays.getFirst().remove();
        enabledOverlays.removeFirst();

        if (enabledOverlays.isEmpty()) {
            this.wake();
        } else {
            enabledOverlays.getFirst().wake();
        }
    }

    public void rest() {
        logger.info("Screen is resting");
        //gameArea.pauseMusic();
        ServiceLocator.getEntityService().restWholeScreen();
    }

    public void wake() {
        logger.info("Screen is Awake");
        //gameArea.playMusic();
        ServiceLocator.getEntityService().wakeWholeScreen();
    }
}
