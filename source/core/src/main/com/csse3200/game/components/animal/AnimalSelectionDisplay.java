package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Texture;

/**
 * Base class that represents the common functionality for animal selection screens.
 * Manages the UI components for all types of animal selection.
 */
public abstract class AnimalSelectionDisplay {
    private final Stage stage;
    private final Skin skin;
    private final Image[] animalImages;
    private final TextButton[] animalButtons;
    private final TextButton selectButton;
    private final TextButton backButton;

    /**
     * Constructor for AnimalSelectionDisplay to initialize UI components and layout.
     * @param stage The stage where UI elements are added.
     * @param skin  The skin for styling UI elements.
     */
    public AnimalSelectionDisplay(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;

        // Initialize arrays to hold images and buttons for the animals
        this.animalImages = new Image[3];
        this.animalButtons = new TextButton[3];

        // Initialize the select and back buttons
        this.selectButton = new TextButton("Ready?", skin);
        this.backButton = new TextButton("Go Back", skin);

        // Set up the display of the selection screen
        initializeDisplay();
    }

    /**
     * Abstract method that must be implemented to provide the background image path for the screen.
     * @return The background image path.
     */
    protected abstract String getBackgroundImagePath();

    /**
     * Abstract method to provide the animal image paths for the screen.
     * @return Array of animal image paths.
     */
    protected abstract String[] getAnimalImagePaths();

    /**
     * Initializes the display by setting up the layout of images and buttons on the stage.
     */
    private void initializeDisplay() {
        // Add the background image to the stage
        BackgroundImage backgroundImage = new BackgroundImage(getBackgroundImagePath());
        stage.addActor(backgroundImage);

        // Create the main table layout for positioning UI elements
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().padTop(80);
        stage.addActor(mainTable);

        // Add images and buttons for each animal
        String[] animalImagePaths = getAnimalImagePaths();
        for (int i = 0; i < 3; i++) {
            animalImages[i] = new Image(new Texture(animalImagePaths[i]));
            animalButtons[i] = new TextButton("Animal " + (i + 1), skin);

            Table animalTable = new Table();
            animalTable.add(animalImages[i]).pad(20).padLeft(180);
            animalTable.row();
            animalTable.add(animalButtons[i]).pad(10).padLeft(180);

            mainTable.add(animalTable).pad(10).expandX();
        }

        mainTable.row();
        mainTable.add().expandY();

        // Create a table for the select and back buttons
        Table buttonTable = new Table();
        buttonTable.add(selectButton).padBottom(10).width(300).height(60).padRight(250);
        buttonTable.add(backButton).padBottom(10).width(300).height(60).padRight(380);

        mainTable.add(buttonTable).center().padBottom(60).colspan(60).bottom();
    }

    // Getters for accessing the UI elements
    public Image[] getAnimalImages() {
        return animalImages;
    }

    public TextButton[] getAnimalButtons() {
        return animalButtons;
    }

    public TextButton getSelectButton() {
        return selectButton;
    }

    public TextButton getBackButton() {
        return backButton;
    }

    public Skin getSkin() {
        return skin;
    }

    public Stage getStage() {
        return stage;
    }
}
