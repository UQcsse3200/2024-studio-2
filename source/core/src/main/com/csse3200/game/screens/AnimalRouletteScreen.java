package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;

public class AnimalRouletteScreen extends ScreenAdapter {
    protected final GdxGame game;
    protected Stage stage;
    protected Skin skin;
    protected Image animalImage;
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
    protected int currentAnimalIndex = 0;
    protected PopUpHelper popUpHelper;

    // UI elements for dynamic positioning
    protected TextButton leftButton;
    protected TextButton rightButton;
    protected TextButton continueButton;
    protected TextButton backButton;
    protected TextButton waterAnimalsButton;
    TextButton airAnimalsButton;

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

    protected void createUI() {
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
        waterAnimalsButton = new TextButton("Water Animals", skin);
        airAnimalsButton = new TextButton("Air Animals", skin);

        // Add actors to stage
        stage.addActor(leftButton);
        stage.addActor(rightButton);
        stage.addActor(continueButton);
        stage.addActor(backButton);
        stage.addActor(waterAnimalsButton);
        stage.addActor(airAnimalsButton);

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

        waterAnimalsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Water Animals button clicked!"); // Log statement
                game.setScreen(new WaterAnimalSelectionScreen(game));
            }
        });


        airAnimalsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AirAnimalSelectionScreen(game));
            }
        });
    }

    protected void updateAnimalImage() {
        animalImage.setDrawable(new Image(new Texture(Gdx.files.internal(animalImages[currentAnimalIndex]))).getDrawable());
    }

    protected void showAnimalStats() {
        String title = animalNames[currentAnimalIndex];
        String content = animalDescriptions[currentAnimalIndex];

        popUpHelper.displayDialog(title, content, animalImages[currentAnimalIndex], 600, 400, currentAnimalIndex, () -> {
            game.setScreen(new StoryScreen(game, animalNames[currentAnimalIndex].toLowerCase()));
        });
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