package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ui.pop_up_dialog_box.PopUpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalRouletteScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AnimalRouletteScreen.class);
    private final GdxGame game;
    private Stage stage;
    private Skin skin;
    private Image animalImage;
    private final String[] animalImages = {
            "images/dog.png",
            "images/croc.png",
            "images/bird.png"
    };
    private final String[] animalNames = {"Dog", "Crocodile", "Bird"};
    private final String[] animalDescriptions = {
            "The Dog is loyal, brave, and agile. It excels in combat with its speed and determination.",
            "The Crocodile is strong, cunning, and resilient. It possesses incredible defensive and offensive capabilities.",
            "The Bird is fast, intelligent, and free. It can outmaneuver opponents and attack from the skies."
    };
    private int currentAnimalIndex = 0;
    private PopUpHelper popUpHelper;

    // UI elements for dynamic positioning
    private TextButton leftButton;
    private TextButton rightButton;
    private TextButton continueButton;
    private TextButton backButton;
    private TextButton waterAnimalsButton;
    private TextButton airAnimalsButton;

    public AnimalRouletteScreen(GdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        popUpHelper = new PopUpHelper(skin, stage);

        createUI();
    }

    private void createUI() {
        // Set background
        Image background = new Image(new Texture(Gdx.files.internal("images/animal/JungleAnimalSelectionBG.jpeg")));
        background.setFillParent(true);
        stage.addActor(background);

        // Create animal image
        animalImage = new Image(new Texture(Gdx.files.internal(animalImages[currentAnimalIndex])));
        animalImage.setScale(2); // Make the animal image larger
        stage.addActor(animalImage);

        // Create buttons
        leftButton = new TextButton("<", skin);
        rightButton = new TextButton(">", skin);
        continueButton = new TextButton("Continue", skin);
        backButton = new TextButton("Go Back", skin);

        // Add actors to stage
        stage.addActor(leftButton);
        stage.addActor(rightButton);
        stage.addActor(continueButton);
        stage.addActor(backButton);

        // Add listeners
        addListeners();

        // Initial positioning
        updateButtonPositions();
    }

    private void addListeners() {
        leftButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentAnimalIndex = (currentAnimalIndex - 1 + animalImages.length) % animalImages.length;
                updateAnimalImage();
            }
        });

        rightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentAnimalIndex = (currentAnimalIndex + 1) % animalImages.length;
                updateAnimalImage();
            }
        });

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAnimalStats();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });
    }

    private void updateAnimalImage() {
        animalImage.setDrawable(new Image(new Texture(Gdx.files.internal(animalImages[currentAnimalIndex]))).getDrawable());
    }

    private void showAnimalStats() {
        String title = animalNames[currentAnimalIndex];
        String content = animalDescriptions[currentAnimalIndex];

        popUpHelper.displayDialog(title, content, animalImages[currentAnimalIndex], 600, 400, currentAnimalIndex, () ->
                game.setScreen(new StoryScreen(game, animalNames[currentAnimalIndex].toLowerCase())));
    }

    private void updateButtonPositions() {
        float screenWidth = stage.getWidth();
        float screenHeight = stage.getHeight();

        // Position animal image in the center
        float animalSize = Math.min(screenWidth, screenHeight) / 4; // 1/3 of the screen size
        animalImage.setSize(animalSize, animalSize);
        animalImage.setPosition((screenWidth - animalSize) / 3, (screenHeight - animalSize) / 3);

        // Position left and right buttons
        float arrowButtonSize = 50;
        leftButton.setBounds(screenWidth / 4 - arrowButtonSize / 2, screenHeight / 2 - arrowButtonSize / 2, arrowButtonSize, arrowButtonSize);
        rightButton.setBounds(3 * screenWidth / 4 - arrowButtonSize / 2, screenHeight / 2 - arrowButtonSize / 2, arrowButtonSize, arrowButtonSize);

        // Position continue and back buttons
        float bottomButtonWidth = 300;
        float bottomButtonHeight = 60;
        float bottomButtonY = 60;
        continueButton.setBounds(screenWidth / 2 - bottomButtonWidth - 10, bottomButtonY, bottomButtonWidth, bottomButtonHeight);
        backButton.setBounds(screenWidth / 2 + 10, bottomButtonY, bottomButtonWidth, bottomButtonHeight);

        // Position water and air buttons
        float sideButtonWidth = 200;
        float sideButtonHeight = 50;
        float sideButtonX = 20;
        waterAnimalsButton.setBounds(sideButtonX, screenHeight - sideButtonHeight - 20, sideButtonWidth, sideButtonHeight);
        airAnimalsButton.setBounds(sideButtonX, screenHeight - 2 * sideButtonHeight - 30, sideButtonWidth, sideButtonHeight);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        updateButtonPositions();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}