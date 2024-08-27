package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Texture;


/**
 * Represents the display for selecting animals in the game.
 * The class manages the UI components related to
 * animal selection, including images, buttons, and their layout on the stage.
 */
public class AnimalSelectionDisplay {
    private final Stage stage;
    private final Skin skin;
    private final Image[] animalImages;
    private final TextButton[] animalButtons;
    private final TextButton selectButton;
    private final TextButton backButton;

    /**
     * Constructs a diplay for displaying and managing animal selection UI.
     * Initializes the arrays for animal images and buttons, as well as the select and back buttons.
     * This constructor sets up the stage and skin used for styling UI elements.
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
     * Initializes the display by setting up the layout of images and buttons on the stage.
     */
    private void initializeDisplay() {
        // Create the main table layout for positioning UI elements
        Table mainTable = new Table();
        mainTable.setFillParent(true); // Make the table fill the entire stage
        mainTable.top().padTop(80); // Align the table to the top with some padding
        stage.addActor(mainTable); // Add the table to the stage

        // Paths to the images of the animals
        String[] animalImagePaths = {
                "images/dog.png",
                "images/croc.png",
                "images/bird.png"
        };

        // Add images and buttons for each animal
        for (int i = 0; i < 3; i++) {
            animalImages[i] = new Image(new Texture(animalImagePaths[i])); // Load the animal image
            animalButtons[i] = new TextButton("Animal " + (i + 1), skin); // Create a button for the animal

            // Create a table for each animal's image and button
            Table animalTable = new Table();
            animalTable.add(animalImages[i]).pad(20).padLeft(180); // Position the image with padding
            animalTable.row(); // Move to the next row in the table
            animalTable.add(animalButtons[i]).pad(10).padLeft(180); // Position the button with padding

            // Add the animal table to the main table
            mainTable.add(animalTable).pad(10).expandX();
        }

        // Add space between the animal selection and buttons
        mainTable.row();
        mainTable.add().expandY();

        // Create a table for the select and back buttons
        Table buttonTable = new Table();
        buttonTable.add(selectButton).padBottom(10).width(300).height(60).padRight(250); // Position the select button
        buttonTable.add(backButton).padBottom(10).width(300).height(60).padRight(380); // Position the back button

        // Add the button table to the main table
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
