package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
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
    private Sound cutSceneSound;
    private BitmapFont font;
    private Label label;
    private RenderService renderService;
    private EntityService entityService;
    private ResourceService resourceService;
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Texture cutSceneTexture;
    private Skin skin;
    private String fullText;
    private StringBuilder displayedText;
    private float timeSinceLastCharacter; // Timer for character reveal
    private float characterRevealInterval = 0.05f; // Time between character reveals
    private int currentCharacterIndex; // Index of the next character to reveal

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

        // Play the cutscene sound
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
        timeSinceLastCharacter += delta;
        if (currentCharacterIndex < fullText.length() && timeSinceLastCharacter >= characterRevealInterval) {
            displayedText.append(fullText.charAt(currentCharacterIndex));
            label.setText(displayedText.toString());
            currentCharacterIndex++;
            timeSinceLastCharacter = 0; // Reset the timer
        }
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
        cutSceneTexture = new Texture(Gdx.files.internal("images/animal/Black and White Modern Forest Scenery At Night Desktop Wallpapaer.png"));

        // Load the cutscene sound
        cutSceneSound = Gdx.audio.newSound(Gdx.files.internal("sounds/animal/birds-and-animals-before-sunrise-246785.mp3"));
    }

    /**
     * Creates the cutscene UI elements.
     */
    private void createCutScene() {
        logger.debug("Creating CutScene UI");

        font = new BitmapFont(Gdx.files.internal("default.fnt"));
        font.setColor(Color.WHITE);


        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = font;


        fullText = "Once upon a time, after humans mysteriously vanished, the animal kingdoms rose again! " +
                "With nature reclaiming the land, every creature big and small is determined to rule it all!\n" +
                "In this exciting adventure, you are one of them! Choose your animal wisely, gather resources, and grow your strength. " +
                "Will you lead your kingdom to victory and conquer them all?\n" +
                "From the peaceful forests to the deep oceans and beyond, other animal kingdoms are standing in your way! " +
                "But be warned... the final challenge lies with the last remnants of humanity.\n" +
                "It’s time to unite the wild! Let the battle for the kingdoms begin.";

        displayedText = new StringBuilder(); // Initialize the displayed text
        currentCharacterIndex = 0; // Start at the first character

        // Create the label with the initial empty text
        label = new Label("", labelStyle);
        label.setFontScale(1.0f);
        label.setAlignment(Align.center);
        label.setWrap(true);

        // Set up the table for UI layout
        Table table = new Table();
        table.setFillParent(true);
        table.center().pad(50);
        table.add(label).expandX().width(Gdx.graphics.getWidth() - 100).center();

        // Add the table to the stage
        stage.addActor(table);
    }

    /**
     * Creates the continue button for proceeding to the next screen.
     */
    private void createContinueButton() {
        logger.debug("Creating Continue button");
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        CustomButton continueButton = new CustomButton("Continue", skin);
        continueButton.setSize(200, 60);
        continueButton.setPosition(stage.getWidth() / 2 - continueButton.getWidth() / 2, 30);

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.debug("Continue button clicked");
                game.setScreen(GdxGame.ScreenType.ANIMAL_ROULETTE);
            }
        });

        stage.addActor(continueButton);
    }

    @Override
    public void dispose() {
        logger.debug("Disposing CutScene screen");
        renderService.dispose();
        entityService.dispose();
        resourceService.dispose();
        spriteBatch.dispose();
        stage.dispose();
        cutSceneTexture.dispose();

        // Dispose of the sound
        if (cutSceneSound != null) {
            cutSceneSound.dispose();
        }
    }
}
