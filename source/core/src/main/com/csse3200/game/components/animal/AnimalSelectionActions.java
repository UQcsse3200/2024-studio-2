package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.LoadingScreen;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalSelectionActions {
    private static final Logger logger = LoggerFactory.getLogger(AnimalSelectionActions.class);
    final AnimalSelectionDisplay display;
    private final PopUpHelper dialogHelper;
    static Image selectedAnimalImage;
    private final GdxGame game;
    static String selectedAnimalImagePath;

    // Constructor to initialize the AnimalSelectionActions class
    public AnimalSelectionActions(AnimalSelectionDisplay display, PopUpHelper dialogHelper, GdxGame game) {
        this.display = display;
        this.dialogHelper = dialogHelper;
        this.game = game;
        addListeners(); // Add listeners to handle user interactions
    }

    // Getter method to retrieve the path of the selected animal image
    public static String getSelectedAnimalImagePath() {
        return selectedAnimalImagePath;
    }

    // Method to add click listeners to the animal images, buttons, and other UI elements
    private void addListeners() {
        Image[] animalImages = display.getAnimalImages();
        TextButton[] animalButtons = display.getAnimalButtons();
        String[] animalImagePaths = {
                "images/dog.png",
                "images/croc.png",
                "images/bird.png"
        };

        // Add listeners to animal images and buttons
        for (int i = 0; i < animalImages.length; i++) {
            final int animalIndex = i;
            final String animalImagePath = animalImagePaths[i];

            animalImages[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    logger.debug("Animal {} image clicked", animalIndex + 1);
                    selectAnimal(animalImages[animalIndex], animalImagePath);
                    showAnimalDialog(animalIndex, animalImagePath);
                }
            });

            animalButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    logger.debug("Animal {} button clicked", animalIndex + 1);
                    selectAnimal(animalImages[animalIndex], animalImagePath);
                    showAnimalDialog(animalIndex, animalImagePath);
                }
            });
        }

        // Add listener to the "Select" button to proceed with the selected animal
        display.getSelectButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedAnimalImage != null) {
                    logger.debug("Select button clicked with animal selected");
                    game.setScreen(new LoadingScreen(game));
                } else {
                    logger.debug("No animal selected");
                    showSelectionAlert(); // Show an alert if no animal is selected
                }
            }
        });

        // Add listener to the "Back" button to return to the main menu
        display.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.debug("Go back button clicked");
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });
    }

    // Method to highlight the selected animal image and store its path
    void selectAnimal(Image animalImage, String animalImagePath) {
        if (selectedAnimalImage != null) {
            selectedAnimalImage.setColor(1, 1, 1, 1); // Reset color of the previously selected image
        }

        selectedAnimalImage = animalImage;
        selectedAnimalImagePath = animalImagePath;
        selectedAnimalImage.setColor(1, 0, 0, 1); // Highlight the selected image

        logger.debug("Animal selected: {}", animalImage.getName());
    }

    // Method to show an alert if the "Select" button is clicked without an animal selected
    private void showSelectionAlert() {
        Dialog dialog = new Dialog("Alert", display.getSkin()) {
            @Override
            protected void result(Object object) {
                // No specific action required after dismissing the alert
            }
        };

        dialog.text("Please select an animal first.");
        dialog.button("OK", true);
        dialog.show(display.getStage());
    }

    // Method to display a dialog with information about the selected animal
    void showAnimalDialog(int animalIndex, String animalImagePath) {
        String title = "Animal " + (animalIndex + 1);
        String content = "You've selected Animal " + (animalIndex + 1) + ".\n" +
                "This animal has unique characteristics.\n" +
                "It possesses special abilities.";

        dialogHelper.displayDialog(title, content, animalImagePath, 900f, 400f);
    }
}
