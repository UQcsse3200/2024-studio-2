package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.LoadingScreen;
import com.csse3200.game.ui.DialogueBox.DialogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalSelectionActions {
    private static final Logger logger = LoggerFactory.getLogger(AnimalSelectionActions.class);
    private final AnimalSelectionDisplay display;
    private final DialogHelper dialogHelper;
    private Image selectedAnimalImage;
    private final GdxGame game;
    private static String selectedAnimalImagePath;

    /**
     * Constructor to initialize AnimalSelectionActions.
     *
     * @param display       The display component for animal selection.
     * @param dialogHelper  Helper for displaying dialogs.
     * @param game          Reference to the main game instance.
     */
    public AnimalSelectionActions(AnimalSelectionDisplay display, DialogHelper dialogHelper, GdxGame game) {
        this.display = display;
        this.dialogHelper = dialogHelper;
        this.game = game;
        addListeners();
    }

    /**
     * Getter for the selected animal image path.
     *
     * @return Path to the selected animal's image.
     */
    public static String getSelectedAnimalImagePath() {
        return selectedAnimalImagePath;
    }

    /**
     * Adds click listeners to the animal images, buttons, and other UI elements.
     */
    private void addListeners() {
        Image[] animalImages = display.getAnimalImages();
        TextButton[] animalButtons = display.getAnimalButtons();
        String[] animalImagePaths = {
                "images/dog.png",
                "images/croc.png",
                "images/bird.png"
        };

        // Add listeners to each animal image and button
        for (int i = 0; i < animalImages.length; i++) {
            final int animalIndex = i;
            final String animalImagePath = animalImagePaths[i];

            // Listener for animal images
            animalImages[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    logger.debug("Animal {} image clicked", animalIndex + 1);
                    selectAnimal(animalImages[animalIndex], animalImagePath);
                    showAnimalDialog(animalIndex);
                }
            });

            // Listener for animal buttons
            animalButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    logger.debug("Animal {} button clicked", animalIndex + 1);
                    selectAnimal(animalImages[animalIndex], animalImagePath);
                    showAnimalDialog(animalIndex);
                }
            });
        }

        // Listener for the "Select" button
        display.getSelectButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedAnimalImage != null) {
                    logger.debug("Select button clicked with animal selected");

                    // Transition to the loading screen instead of directly to the game screen
                    game.setScreen(new LoadingScreen(game));

                } else {
                    logger.debug("No animal selected");
                    showSelectionAlert();
                }
            }
        });

        // Listener for the "Back" button
        display.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.debug("Go back button clicked");
                game.setScreen(GdxGame.ScreenType.MAIN_MENU); // Transition back to the main menu
            }
        });
    }

    /**
     * Handles the selection of an animal. Updates the selected image and path.
     *
     * @param animalImage     The image of the selected animal.
     * @param animalImagePath The file path of the selected animal's image.
     */
    void selectAnimal(Image animalImage, String animalImagePath) {
        // Reset the previous selection
        if (selectedAnimalImage != null) {
            selectedAnimalImage.setColor(1, 1, 1, 1);
        }

        // Highlight the new selection
        selectedAnimalImage = animalImage;
        selectedAnimalImagePath = animalImagePath;
        selectedAnimalImage.setColor(1, 0, 0, 1);

        logger.debug("Animal selected: {}", animalImage.getName());
    }

    /**
     * Displays an alert dialog if no animal is selected when "Select" is clicked.
     */
    private void showSelectionAlert() {
        Dialog dialog = new Dialog("Alert", display.getSkin()) {
            @Override
            protected void result(Object object) {
                // No action needed here
            }
        };

        dialog.text("Please select an animal first.");
        dialog.button("OK", true);
        dialog.show(display.getStage());
    }

    /**
     * Shows a dialog with details about the selected animal.
     *
     * @param animalIndex The index of the selected animal.
     */
    private void showAnimalDialog(int animalIndex) {
        String title = "Animal " + (animalIndex + 1);
        String content = "You've selected Animal " + (animalIndex + 1) + ".\n" +
                "This animal has unique characteristics.\n" +
                "It possesses special abilities.";

        // Set dialog width, height, and image position
        float imageWidth = 100;  // Adjust as needed
        float imageHeight = 100; // Adjust as needed
        float imageX = 50;       // Adjust as needed
        float imageY = 150;      // Adjust as needed

        dialogHelper.displayDialog(title, content, imageWidth, imageHeight, imageX, imageY);
    }
}
