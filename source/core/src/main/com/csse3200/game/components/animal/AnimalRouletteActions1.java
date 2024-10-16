package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.screens.StoryScreen;
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
    private int lastViewedAnimalIndex = -1;
    private int selectedAnimalIndex;  // Add this line to declare the variable

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
        display.updateBackground(currentAnimalIndex);
    }

    private void selectCurrentAnimal() {
        if (selectedAnimalImage != null) {
            selectedAnimalImage.setColor(1, 1, 1, 1);
        }
        selectedAnimalImage = display.getAnimalImage();
        selectedAnimalImage.setColor(1, 0, 0, 1);
        selectedAnimalImagePath = animalImagePaths[currentAnimalIndex];
        GameState.player.selectedAnimalPath = selectedAnimalImagePath;
        logger.debug("Animal selected: {}", selectedAnimalImagePath);
    }

    private void showAnimalDialog() {
        String title = "Animal " + (currentAnimalIndex + 1);
        String content = display.getAnimalDescription(currentAnimalIndex);
        String confirmButtonText = getConfirmButtonText(currentAnimalIndex);
        selectedAnimalIndex = currentAnimalIndex;  // Store the current index
        dialogHelper.displayDialog(
                title,
                content,
                selectedAnimalImagePath,
                900f,
                500f,
                currentAnimalIndex,
                confirmButtonText,
                this::switchToStoryScreen  // Use method reference
        );

        // Update last viewed animal
        if (lastViewedAnimalIndex != -1) {
            display.resetAnimalColor(lastViewedAnimalIndex);
        }
        lastViewedAnimalIndex = currentAnimalIndex;
        display.highlightAnimal(currentAnimalIndex);
    }

    private void switchToStoryScreen() {
        String animalType = display.getAnimalType(selectedAnimalIndex);
        game.setScreen(new StoryScreen(game, animalType));
    }

    private String getConfirmButtonText(int index) {
        String[] confirmTexts = {"Enter Land Kingdom", "Enter Water Kingdom", "Enter Air Kingdom"};
        return confirmTexts[index];
    }


    public void resetSelection() {
        if (selectedAnimalImage != null) {
            selectedAnimalImage.setColor(1, 1, 1, 1);
        }
        selectedAnimalImage = null;
        selectedAnimalImagePath = null;
        currentAnimalIndex = 0;
        updateDisplayedAnimal();
    }
}
