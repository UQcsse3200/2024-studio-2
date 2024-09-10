package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;
import com.csse3200.game.overlays.Overlay;
import com.csse3200.game.overlays.PauseOverlay;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.factories.EnemyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Manages the cutscene for enemy NPCs displayed before transitioning to the combat screen.
 * Handles initialization, rendering, and disposal of cutscene elements.
 */
public class EnemyCutsceneScreen extends ScreenAdapter {
    private static final float CUTSCENE_DURATION = 3.0f; // Cutscene lasts for 3 seconds
    private float timeElapsed = 0;
    private boolean transition;

    private static final Logger logger = LoggerFactory.getLogger(EnemyCutsceneScreen.class);
    private static final Vector2 CAMERA_POSITION = new Vector2(7.5f, 7.5f);
    private boolean isPaused = false;
    private final GdxGame game;
    private final Renderer renderer;
    private final PhysicsEngine physicsEngine;
    private final Screen oldScreen;
    private final ServiceContainer oldScreenServices;
    private final Entity player;
    private final Entity enemy;
    private final Deque<Overlay> enabledOverlays = new LinkedList<>();

    /**
     * Creates a new cutscene screen.
     *
     * @param game the game instance
     * @param screen the previous screen
     * @param container services from the previous screen
     * @param player the player entity
     * @param enemy the enemy entity
     */
    public EnemyCutsceneScreen(GdxGame game, Screen screen, ServiceContainer container, Entity player, Entity enemy) {
        this.game = game;
        this.oldScreen = screen;
        this.oldScreenServices = container;
        this.player = player;
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

        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

        this.transition = false;
        createUI();

        logger.debug("Initialising main game dup screen entities");
        TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
    }

    /**
     * Renders the cutscene screen. Updates the physics engine and entity service,
     * and handles transitioning to the combat screen once the cutscene duration has passed.
     *
     * @param delta The time elapsed since the last frame, in seconds.
     */
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
                game.setScreen(new CombatScreen(game, oldScreen, oldScreenServices, player, enemy));
            }
        }
    }

    /**
     * Resizes the renderer when the screen size changes.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    /**
     * Pauses the cutscene screen. Disables updates and rendering when the game is paused.
     */
    @Override
    public void pause() {
        isPaused = true;
        logger.info("Game paused");
    }

    /**
     * Resumes the cutscene screen. Re-enables updates and rendering when the game is resumed.
     */
    @Override
    public void resume() {
        isPaused = false;
        logger.info("Game resumed");
    }

    /**
     * Disposes of the resources used by the cutscene screen, including renderer and services.
     * This method is called when the screen is no longer needed.
     */
    @Override
    public void dispose() {
        logger.debug("Disposing cutscene screen");

        renderer.dispose();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }

    /**
     * Sets up and animates the cutscene UI elements, including the enemy image and name.
     * This method configures the appearance and animations of UI components, such as:
     * - The enemy image, which slides into view.
     * - The enemy name label, which flashes and slides into view.
     * - Black bars at the top and bottom of the screen.
     */
    private void createUI() {
        logger.debug("Creating cutscene UI");
        Stage stage = ServiceLocator.getRenderService().getStage();

        Texture enemyImageTexture;
        BitmapFont defaultFont = new BitmapFont();
        defaultFont.getData().setScale(2.0f);

        // Create label style with the font
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = defaultFont;
        labelStyle.fontColor = Color.BLACK;

        Label enemyNameLabel;

        // Select enemy image and name based on enemy type
        switch (enemy.getEnemyType()) {
            case CHICKEN:
                enemyImageTexture = new Texture("images/chicken.png");
                enemyNameLabel = new Label("Chicken", labelStyle);
                break;
            case FROG:
                enemyImageTexture = new Texture("images/frog.png");
                enemyNameLabel = new Label("Frog", labelStyle);
                break;
            case MONKEY:
                enemyImageTexture = new Texture("images/monkey.png");
                enemyNameLabel = new Label("Monkey", labelStyle);
                break;
            default:
                enemyImageTexture = new Texture("images/final_boss_kangaroo_idle.png");
                enemyNameLabel = new Label("Kanga", labelStyle);
                break;
        }

        // Create and position black bars
        Texture topBarTexture = new Texture("images/black_bar.png");
        Texture bottomBarTexture = new Texture("images/black_bar.png");

        Image topBar = new Image(topBarTexture);
        Image bottomBar = new Image(bottomBarTexture);

        topBar.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 6f);
        bottomBar.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 6f);

        topBar.setPosition(0, Gdx.graphics.getHeight() - topBar.getHeight());
        bottomBar.setPosition(0, 0);

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

    /**
     * Pauses the screen's entities.
     */
    public void rest() {
        logger.info("Screen is resting");
        //gameArea.pauseMusic();
        ServiceLocator.getEntityService().restWholeScreen();
    }

    /**
     * Resumes the screen's entities.
     */
    public void wake() {
        logger.info("Screen is Awake");
        //gameArea.playMusic();
        ServiceLocator.getEntityService().wakeWholeScreen();
    }
}
