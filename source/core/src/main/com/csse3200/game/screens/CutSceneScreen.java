package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
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
    private Skin skin;
    private Sound cutSceneSound;
    private BitmapFont font;
    private Label label;

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

    private void loadAssets() {
        logger.debug("Loading CutScene assets");
        resourceService.loadAll();
        cutSceneTexture = new Texture(Gdx.files.internal("sounds/animal/Untitled design.png"));
        cutSceneSound = Gdx.audio.newSound(Gdx.files.internal("sounds/animal/harry_potter_theme.mp3"));

        // Load the font and set its color to white
        font = new BitmapFont(Gdx.files.internal("default.fnt"));
        font.setColor(Color.WHITE); // Set text color to white
    }

    private void createCutScene() {
        logger.debug("Creating CutScene UI");


        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = font;

        // Create the label with text
        label = new Label("Once upon a time, after humans mysteriously vanished, the animal kingdoms rose again!  \n With nature reclaiming the land, every creature big and small is determined to rule it all!\\n\" +\n" +
                "                \"In this exciting adventure, you are one of them! Choose your animal wisely, gather resources, and grow your strength. +\\n\" Will you lead your kingdom to victory and conquer them all?\\n\" +\n" +
                "                \"From the peaceful forests to the deep oceans and beyond, other animal kingdoms are standing in your way! +\\n\"But be warned... +\\n\" the final challenge lies with the last remnants of humanity. \\n\" +\n" +
                "                \"It’s time to unite the wild!  Let the battle for the kingdoms begin", labelStyle);
        label.setFontScale(2); // Set the font size
        label.setAlignment(Align.center);


        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(20);

        // Add the label to the table
        table.add(label).expandX().align(Align.top).padTop(100);

        stage.addActor(table);
    }

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
        cutSceneSound.dispose();
        font.dispose(); // Dispose of the font
    }
}
