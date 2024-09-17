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
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.overlays.Overlay;
import com.csse3200.game.overlays.PauseOverlay;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Manages the cutscene for enemy NPCs displayed before transitioning to the combat screen.
 * Handles initialization, rendering, and disposal of cutscene elements.
 */
public class EnemyCutsceneScreen extends ScreenAdapter {
    private static final float CUTSCENE_DURATION = 5.0f; // Cutscene lasts for 3 seconds
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
     * Sets up the cutscene UI components, including background, black bars at the top and bottom of the screen,
     * and initial configurations. This method ensures that the static elements of the UI are in place before
     * any interactive elements are added or animations are applied.
     */
    private void setupUIComponents() {
        logger.debug("Setting up cutscene UI components");
        Stage stage = ServiceLocator.getRenderService().getStage();

        // Load background texture and create Image actor
        Texture backgroundTexture = new Texture("images/transitionBg.jpg");
        Image backgroundImage = new Image(backgroundTexture);

        // Set background image to cover the whole screen
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Add actors to stage
        stage.addActor(backgroundImage);
//
    }

    /**
     * Configures and adds the enemy image, enemy name label, and stats labels to the stage.
     * This method handles the creation of dynamic UI elements based on the enemy type and its combat stats,
     * including setting up the layout for these elements in a table.
     */
    private void configureAndAddUIElements() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        BitmapFont defaultFont = new BitmapFont();
        defaultFont.getData().setScale(2.0f);

        // Create label style with the font
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = defaultFont;
        labelStyle.fontColor = Color.BLACK;

        Label enemyNameLabel;
        Texture enemyImageTexture;

        // Select enemy image and name based on enemy type
        switch (enemy.getEnemyType()) {
            case CHICKEN:
                enemyImageTexture = new Texture("images/chicken_idle.png");
                enemyNameLabel = new Label("Chicken", labelStyle);
                break;
            case FROG:
                enemyImageTexture = new Texture("images/frog_idle.png");
                enemyNameLabel = new Label("Frog", labelStyle);
                break;
            case MONKEY:
                enemyImageTexture = new Texture("images/monkey_idle.png");
                enemyNameLabel = new Label("Monkey", labelStyle);
                break;
            case BEAR:
                enemyImageTexture = new Texture("images/bear_idle.png");
                enemyNameLabel = new Label("Bear", labelStyle);
                break;
            case PIRANHA:
                enemyImageTexture = new Texture("images/piranha_idle.png");
                enemyNameLabel = new Label("Piranha", labelStyle);
                break;
            default:
                enemyImageTexture = new Texture("images/final_boss_kangaroo_idle.png");
                enemyNameLabel = new Label("Kanga", labelStyle);
                break;
        }

        Image enemyImage = new Image(enemyImageTexture);

        // Create labels for stats
        CombatStatsComponent stats = enemy.getComponent(CombatStatsComponent.class);
        Label healthLabel = new Label("Health: " + stats.getHealth() + "/" + stats.getMaxHealth(), labelStyle);
        Label hungerLabel = new Label("Hunger: " + stats.getHunger() + "/" + stats.getMaxHunger(), labelStyle);
        Label strengthLabel = new Label("Strength: " + stats.getStrength(), labelStyle);
        Label defenseLabel = new Label("Defense: " + stats.getDefense(), labelStyle);
        Label speedLabel = new Label("Speed: " + stats.getSpeed(), labelStyle);
        Label experienceLabel = new Label("Experience: " + stats.getExperience() + "/" + stats.getMaxExperience(), labelStyle);

        // Table layout for positioning
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Add elements to the table with spacing
        table.add(enemyImage).width(500).height(400);
        table.row();
        table.add(enemyNameLabel).padBottom(5);
        table.row();
        table.add(healthLabel).padBottom(5);
        table.row();
        table.add(hungerLabel).padBottom(5);
        table.row();
        table.add(strengthLabel).padBottom(5);
        table.row();
        table.add(defenseLabel).padBottom(5);
        table.row();
        table.add(speedLabel).padBottom(5);
        table.row();
        table.add(experienceLabel).padBottom(5);

        // Add table to the stage
        stage.addActor(table);

        // Set names for elements to easily find them later for animations
        enemyImage.setName("enemyImage");
        enemyNameLabel.setName("enemyNameLabel");
        table.setName("table");
    }

    /**
     * Adds animations to the cutscene UI components, including sliding and fading effects.
     * This method animates the entry of the enemy image and name label into view, and applies a flash effect
     * to the table containing the stats labels to enhance the visual appeal of the cutscene.
     */
    private void addUIAnimations() {
        logger.debug("Adding animations to cutscene UI components");
        Stage stage = ServiceLocator.getRenderService().getStage();

        // Centered positions
        float centerX = (Gdx.graphics.getWidth() - 500) / 2f;
        float centerY = (Gdx.graphics.getHeight() - 150) / 2f;

        // Access UI elements by their names
        Image enemyImage = (Image) stage.getRoot().findActor("enemyImage");
        Label enemyNameLabel = (Label) stage.getRoot().findActor("enemyNameLabel");
        Table table = (Table) stage.getRoot().findActor("table");

        // Initial positions for sliding animations
        enemyImage.setPosition(0, centerY); // Start from off-screen left
        enemyNameLabel.setPosition(Gdx.graphics.getWidth(), centerY - 3000); // Start from off-screen right

        // Animate enemy image (slide-in effect)
        enemyImage.addAction(
                Actions.sequence(
                        Actions.moveTo(centerX, centerY, 2f, Interpolation.pow5Out)
                )
        );

        // Animate enemy name label (slide-in effect)
        enemyNameLabel.addAction(
                Actions.sequence(
                        Actions.moveTo(centerX + 200, centerY - 10, 2f, Interpolation.pow5Out)
                )
        );

        // Add fade-in and flash effect to the table
        table.addAction(
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
    }

    /**
     * Sets up and animates the cutscene UI elements, including the enemy image, name, and stats.
     * This method orchestrates the entire UI setup by calling the methods responsible for
     * setting up UI components, configuring and adding UI elements, and applying animations.
     */
    private void createUI() {
        setupUIComponents();
        configureAndAddUIElements();
        addUIAnimations();
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