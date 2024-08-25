package com.csse3200.game.components.animal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.ui.DialogueBox.DialogBox;
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

    public AnimalSelectionActions(AnimalSelectionDisplay display, DialogHelper dialogHelper, GdxGame game) {
        this.display = display;
        this.dialogHelper = dialogHelper;
        this.game = game;
        addListeners();
    }

    public static String getSelectedAnimalImagePath() {
        return selectedAnimalImagePath;
    }

    private void addListeners() {
        Image[] animalImages = display.getAnimalImages();
        TextButton[] animalButtons = display.getAnimalButtons();
        String[] animalImagePaths = {
                "images/dog.png",
                "images/croc.png",
                "images/bird.png"
        };

        for (int i = 0; i < animalImages.length; i++) {
            final int animalIndex = i;
            final String animalImagePath = animalImagePaths[i];

            animalImages[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    logger.debug("Animal {} image clicked", animalIndex + 1);
                    selectAnimal(animalImages[animalIndex], animalImagePath);
                    showAnimalDialog(animalIndex);
                }
            });

            animalButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    logger.debug("Animal {} button clicked", animalIndex + 1);
                    selectAnimal(animalImages[animalIndex], animalImagePath);
                    showAnimalDialog(animalIndex);
                }
            });
        }

        display.getSelectButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedAnimalImage != null) {
                    logger.debug("Select button clicked with animal selected");
                    game.setScreen(new MainGameScreen(game)); // Transition to the game screen
                } else {
                    logger.debug("No animal selected");
                    showSelectionAlert();
                }
            }
        });

        display.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.debug("Go back button clicked");
                game.setScreen(GdxGame.ScreenType.MAIN_MENU); // Transition back to the main menu
            }
        });
    }

    void selectAnimal(Image animalImage, String animalImagePath) {
        if (selectedAnimalImage != null) {
            selectedAnimalImage.setColor(1, 1, 1, 1);
        }
        selectedAnimalImage = animalImage;
        selectedAnimalImagePath = animalImagePath;
        selectedAnimalImage.setColor(1, 0, 0, 1);
        logger.debug("Animal selected: {}", animalImage.getName());
    }

    private void showSelectionAlert() {
        float dialogWidth = 400;  // Set desired width
        float dialogHeight = 200; // Set desired height
        float xPosition = (Gdx.graphics.getWidth() - dialogWidth) / 2f; // Centered horizontally
        float yPosition = (Gdx.graphics.getHeight() - dialogHeight) / 2f; // Centered vertically

        String[] titles = { "Alert" };
        String[] contents = { "Please select an animal first." };

        DialogBox dialogBox = new DialogBox(titles, contents, display.getSkin(), dialogWidth, dialogHeight, 2, 3);
        dialogBox.display(display.getStage());
    }

    private void showAnimalDialog(int animalIndex) {
        String title = "Animal " + (animalIndex + 1);
        String content = "You've selected Animal " + (animalIndex + 1) + ".\n" +
                "This animal has unique characteristics.\n" +
                "It possesses special abilities.";

        // Dialog box dimensions
        float dialogWidth = 1200;  // Width of the dialog box
        float dialogHeight = 400; // Height of the dialog box

        // Calculate center position
        float xPosition = (Gdx.graphics.getWidth() - dialogWidth) / 2f;
        float yPosition = (Gdx.graphics.getHeight() - dialogHeight) / 2f;

        // Define vertical offset to shift the dialog down
        float verticalOffset = 120f;  // Adjust this value as needed

        // Adjust yPosition to shift the dialog box down
        yPosition -= verticalOffset;

        // Display the dialog box
        dialogHelper.displayDialog(title, content, dialogWidth, dialogHeight, xPosition, yPosition);
    }



}
