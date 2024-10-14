package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.screens.StoryScreen;
import com.csse3200.game.ui.AlertBox;
import com.csse3200.game.ui.pop_up_dialog_box.PopUpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalRouletteActions1 {
    private static final Logger logger = LoggerFactory.getLogger(AnimalRouletteActions1.class);
    private final AnimalRouletteDisplay1 display;
    private final PopUpHelper dialogHelper;
    private static Image selectedAnimalImage;
    private final GdxGame game;
    private static String selectedAnimalImagePath;
    private int currentAnimalIndex = 0;
    private final String[] animalImagePaths;

    public AnimalRouletteActions1(AnimalRouletteDisplay1 display, PopUpHelper dialogHelper, GdxGame game) {
        this.display = display;
        this.dialogHelper = dialogHelper;
        this.game = game;
        this.animalImagePaths = display.getAnimalImagePaths();
        addListeners();
    }

    public static String getSelectedAnimalImagePath() {
        return selectedAnimalImagePath;
    }

    private void addListeners() {
        display.getSelectButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedAnimalImage != null) {
                    logger.debug("Select button clicked with animal selected");
                    switchToStoryScreen();
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
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        display.getLeftButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.debug("Left button clicked");
                showPreviousAnimal();
            }
        });

        display.getRightButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.debug("Right button clicked");
                showNextAnimal();
            }
        });

        display.getAnimalImage().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.debug("Animal image clicked");
                selectCurrentAnimal();
                showAnimalDialog();
            }
        });
    }

    private void showPreviousAnimal() {
        currentAnimalIndex = (currentAnimalIndex - 1 + animalImagePaths.length) % animalImagePaths.length;
        updateDisplayedAnimal();
    }

    private void showNextAnimal() {
        currentAnimalIndex = (currentAnimalIndex + 1) % animalImagePaths.length;
        updateDisplayedAnimal();
    }

    private void updateDisplayedAnimal() {
        display.updateAnimalImage(animalImagePaths[currentAnimalIndex]);
    }

    /**
     * This method selects the current animal and highlights it by setting its color.
     * It resets the color of the previously selected animal if any.
     */
    private void selectCurrentAnimal() {
        if (selectedAnimalImage != null) {
            // Reset the previous selection
            selectedAnimalImage.setColor(1, 1, 1, 1); // Reset to white
        }

        // Select the new animal and highlight it
        selectedAnimalImage = display.getAnimalImage();
        selectedAnimalImage.setColor(1, 0, 0, 1); // Set to red to indicate highlight
        selectedAnimalImagePath = animalImagePaths[currentAnimalIndex];
        GameState.player.selectedAnimalPath = selectedAnimalImagePath;

        logger.debug("Animal selected: {}", selectedAnimalImagePath);
    }

    private void showSelectionAlert() {
        AlertBox alertBox = new AlertBox("Please select an animal first.", display.getSkin(), 400f, 200f);
        alertBox.display(display.getStage());
    }

    void showAnimalDialog(int animalIndex, String animalImagePath) {
        String title = "Animal " + (animalIndex + 1);
        String content = display.getAnimalDescription(animalIndex);
        dialogHelper.displayDialog(title, content, animalImagePath, 900f, 500f, animalIndex);
    }

    private void showAnimalDialog() {
        showAnimalDialog(currentAnimalIndex, selectedAnimalImagePath);
    }

    private void switchToStoryScreen() {
        game.setScreen(new StoryScreen(game, display.getAnimalType(currentAnimalIndex)));
    }

    public void resetSelection() {
        if (selectedAnimalImage != null) {
            // Reset the color of the previously selected animal
            selectedAnimalImage.setColor(1, 1, 1, 1); // Reset to white
        }
        selectedAnimalImage = null;
        selectedAnimalImagePath = null;
        currentAnimalIndex = 0;
        updateDisplayedAnimal();
    }
}
