package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.ui.CustomButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CutSceneScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CutSceneScreen.class);
    private final GdxGame game;

    private RenderService renderService;
    private EntityService entityService;
    private ResourceService resourceService;
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Texture cutSceneTexture;
    private Texture logbookTexture;
    private Skin skin;

    public CutSceneScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initializing CutScene screen");

        this.spriteBatch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
        this.renderService = new RenderService();
        this.entityService = new EntityService();
        this.resourceService = new ResourceService();
        renderService.setStage(stage);

        loadAssets();
        createCutScene();
        createContinueButton();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        entityService.update();

        // Render background
        spriteBatch.begin();
        spriteBatch.draw(cutSceneTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        // Draw the stage containing UI elements
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        renderService.getStage().getViewport().update(width, height, true);
    }

    private void loadAssets() {
        logger.debug("Loading CutScene assets");
        resourceService.loadAll();
        cutSceneTexture = new Texture(Gdx.files.internal("images/BackgroundSplashBasic.png"));
        logbookTexture = new Texture(Gdx.files.internal("images/logbook/lb-bg.png"));
    }

    private void createCutScene() {
        logger.debug("Creating CutScene UI");

        // Create an image for the logbook background and center it with larger size
        Image logbookImage = new Image(logbookTexture);
        logbookImage.setSize(1600, 600); // Adjust size to make it larger
        logbookImage.setPosition(
                Gdx.graphics.getWidth() / 2f - logbookImage.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - logbookImage.getHeight() / 2f
        );
        stage.addActor(logbookImage);

        // Initialize the skin
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Add the title to the top center of the logbook image
        Label.LabelStyle titleStyle = skin.get(Label.LabelStyle.class);
        Label titleLabel = new Label("ATTACK ON ANIMALS", titleStyle);
        titleLabel.setAlignment(Align.center);
        titleLabel.setSize(1200, 100); // Adjust the size according to your preference
        titleLabel.setPosition(
                Gdx.graphics.getWidth() / 2f - titleLabel.getWidth() / 2f,
                logbookImage.getY() + logbookImage.getHeight() - 150 // Adjust vertical position to be at the top of the logbook
        );
        stage.addActor(titleLabel);

        // Add the text to the center of the logbook image
        addTextToLogbook(5); // Pass a vertical offset to adjust position
    }

    private void createContinueButton() {
        logger.debug("Creating Continue button");
        if (skin == null) {
            skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        }

        CustomButton continueButton = new CustomButton("Continue", skin);

        float buttonWidth = 200;
        float buttonHeight = 60;
        continueButton.setButtonSize(buttonWidth, buttonHeight);

        // Position the button at the bottom center of the screen
        continueButton.setPosition(
                Gdx.graphics.getWidth() / 2f - buttonWidth / 2f,
                30 // Position it 30 pixels above the bottom edge
        );

        continueButton.addClickListener(() -> {
            logger.debug("Continue button clicked");
            game.setScreen(GdxGame.ScreenType.ANIMAL_SELECTION);
        });

        stage.addActor(continueButton);
    }

    private void addTextToLogbook(int verticalOffset) {
        logger.debug("Adding text to logbook");

        // The text you want to display
        String text = "Set in a post-apocalyptic world reshaped by World War 3, 'Attack on Animals Kingdoms' is a game where animals have formed distinct kingdoms based on the elements of land, water, and air. " +
                "As the protagonist, a dog representing loyalty and courage, players embark on a journey across these realms, overcoming mini-bosses that embody the darker traits of their societies. " +
                "Each kingdom offers unique cultural insights and challenges, culminating in a final showdown against a formidable boss born from the chaos of the global flood. By uniting the strengths of " +
                "all kingdoms, players strive to restore harmony and heal a world torn apart, learning valuable lessons about humility, unity, and leadership along the way.";

        // Create a Label with the given text, using the skin
        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);
        Label logbookTextLabel = getLabel(text, labelStyle);
        logbookTextLabel.setSize(1200, 500); // Adjust the size for text field
        logbookTextLabel.setPosition(
                Gdx.graphics.getWidth() / 2f - logbookTextLabel.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - logbookTextLabel.getHeight() / 2f + verticalOffset // Adjust Y position based on offset
        );
        stage.addActor(logbookTextLabel);
    }

    private static Label getLabel(String text, Label.LabelStyle labelStyle) {
        Label logbookTextLabel = new Label(text, labelStyle);
        logbookTextLabel.setWrap(true);
        logbookTextLabel.setAlignment(Align.center);

        // Set the size of the label to fit within the logbook image
        float labelWidth = 1200;
        float labelHeight = 600;
        logbookTextLabel.setSize(labelWidth, labelHeight);

        // Position the label to be centered on the logbook image
        logbookTextLabel.setPosition(
                Gdx.graphics.getWidth() / 2f - labelWidth / 2f,
                Gdx.graphics.getHeight() / 2f - labelHeight / 2f + 50 // Adjust Y position
        );
        return logbookTextLabel;
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
        logbookTexture.dispose();
    }
}
