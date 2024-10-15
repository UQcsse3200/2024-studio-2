package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound; // Import Sound
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.ui.CustomButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the cutscene screen that displays
 * a cutscene before transitioning to the next game state.
 */
public class CutSceneScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CutSceneScreen.class);
    private final GdxGame game;

    private RenderService renderService;
    private EntityService entityService;
    private ResourceService resourceService;
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Texture cutSceneTexture;
    private Skin skin;
    private Sound cutSceneSound; // Declare Sound variable

    public CutSceneScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initializing CutScene screen");

        this.spriteBatch = new SpriteBatch();
        this.stage = new Stage();
        this.renderService = new RenderService();
        this.entityService = new EntityService();
        this.resourceService = new ResourceService();
        renderService.setStage(stage);

        loadAssets();
        createCutScene();
        createContinueButton();

        Gdx.input.setInputProcessor(stage);

        // Play the sound when the screen is created
        cutSceneSound.play();
    }

    @Override
    public void render(float delta) {
        entityService.update();
        spriteBatch.begin();
        renderService.render(spriteBatch);
        spriteBatch.draw(cutSceneTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        renderService.getStage().getViewport().update(width, height, true);
    }

    /**
     * Loads assets required for the cutscene.
     */
    private void loadAssets() {
        logger.debug("Loading CutScene assets");
        resourceService.loadAll();
        cutSceneTexture = new Texture(Gdx.files.internal("images/BackgroundSplashBasic.png"));
        cutSceneSound = Gdx.audio.newSound(Gdx.files.internal("sounds/animal/harry_potter_theme.mp3")); // Load the sound
    }

    /**
     * Creates the cutscene UI elements.
     */
    private void createCutScene() {
        logger.debug("Creating CutScene UI");

    }

    /**
     * Creates the continue button for proceeding to the next screen.
     */
    private void createContinueButton() {
        logger.debug("Creating Continue button");
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        CustomButton continueButton = new CustomButton("Continue", skin);

        float buttonWidth = 200;
        float buttonHeight = 60;
        continueButton.setButtonSize(buttonWidth, buttonHeight);

        continueButton.setPosition(stage.getWidth() / 2 - buttonWidth / 2, 30);

        continueButton.addClickListener(() -> {
            logger.debug("Continue button clicked");
            game.setScreen(GdxGame.ScreenType.ANIMAL_SELECTION);
        });

        stage.addActor(continueButton);
    }

    @Override
    public void dispose() {
        logger.debug("Disposing CutScene screen");
        renderService.dispose();
        entityService.dispose();
        resourceService.loadAll();
        spriteBatch.dispose();
        stage.dispose();
        cutSceneTexture.dispose();
        cutSceneSound.dispose(); // Dispose of the sound
    }
}
