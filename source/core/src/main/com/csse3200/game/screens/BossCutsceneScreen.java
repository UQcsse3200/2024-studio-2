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

/**
 * Manages the cutscene for Boss NPCs displayed before transitioning to the combat screen.
 * Handles initialization, rendering, and disposal of cutscene elements.
 */
public class BossCutsceneScreen extends ScreenAdapter {
    private static final float CUTSCENE_DURATION = 5.0f; // Cutscene lasts for 3 seconds
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
    private final Entity player;
    private final Entity enemy;

    /**
     * Creates a new cutscene screen.
     *
     * @param game the game instance
     * @param screen the previous screen
     * @param container services from the previous screen
     * @param player the player entity
     * @param enemy the enemy entity
     */
    public BossCutsceneScreen(GdxGame game, Screen screen, ServiceContainer container, Entity player, Entity enemy) {
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
                game.setScreen(new CombatScreen(game, oldScreen, oldScreenServices, player, enemy));
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
        logger.info("Game paused");
    }

    @Override
    public void resume() {
        isPaused = false;
        logger.info("Game resumed");
    }

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
        Texture backgroundTexture;
        switch (enemy.getEnemyType()) {
            case WATER_BOSS:
                backgroundTexture = new Texture("images/Water_Transition.jpg"); // Water boss background
                break;
            case AIR_BOSS:
                backgroundTexture = new Texture("images/Air_Transition.jpg"); // Air boss background
                break;
            case KANGAROO:
            default:
                backgroundTexture = new Texture("images/transitionBg.jpg"); // Default background
                break;
        }
        Image backgroundImage = new Image(backgroundTexture);

        // Set background image to cover the whole screen
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create black bars
        Texture topBarTexture = new Texture("images/black_bar.png");
        Texture bottomBarTexture = new Texture("images/black_bar.png");

        Image topBar = new Image(topBarTexture);
        Image bottomBar = new Image(bottomBarTexture);

        topBar.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 6f);
        bottomBar.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 6f);

        topBar.setPosition(0, Gdx.graphics.getHeight() - topBar.getHeight());
        bottomBar.setPosition(0, 0);

        // Add actors to stage
        stage.addActor(backgroundImage);
        stage.addActor(topBar);
        stage.addActor(bottomBar);
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
            case WATER_BOSS:
                enemyImageTexture = new Texture("images/water_boss_idle.png");
                enemyNameLabel = new Label("Abyssdrake, the World Devourer", labelStyle);
                break;
            case AIR_BOSS:
                enemyImageTexture = new Texture("images/air_boss_idle.png");
                enemyNameLabel = new Label("Skylash, the Stormbound Sentinel", labelStyle);
                break;
            default:
                enemyImageTexture = new Texture("images/final_boss_kangaroo_idle.png");
                enemyNameLabel = new Label("Kanga, the Thunderstrider", labelStyle);
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
}
