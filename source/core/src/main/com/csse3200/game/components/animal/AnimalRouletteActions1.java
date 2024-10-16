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
    private final GdxGame game;
    private static String selectedAnimalImagePath;
    private int currentAnimalIndex = 0;
    private final String[] animalImagePaths;
    private int lastViewedAnimalIndex = -1;

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
                viewCurrentAnimal();
                showAnimalDialog();
            }
        });
    }

    private void showPreviousAnimal() {
        currentAnimalIndex = (currentAnimalIndex - 1 + animalImagePaths.length) % animalImagePaths.length;
        updateDisplayedAnimal();
        updateBackground();
    }

    private void showNextAnimal() {
        currentAnimalIndex = (currentAnimalIndex + 1) % animalImagePaths.length;
        updateDisplayedAnimal();
        updateBackground();
    }

    private void updateDisplayedAnimal() {
        display.updateAnimalImage(animalImagePaths[currentAnimalIndex]);
    }

    private void updateBackground() {
        display.updateBackground(currentAnimalIndex);
    }

    private void viewCurrentAnimal() {
        // Reset color of previously viewed animal if exists
        if (lastViewedAnimalIndex != -1) {
            display.getAnimalImage().setColor(1, 1, 1, 1);
        }

        lastViewedAnimalIndex = currentAnimalIndex;
        selectedAnimalImagePath = animalImagePaths[currentAnimalIndex];
        GameState.player.selectedAnimalPath = selectedAnimalImagePath;
        logger.debug("Animal viewed: {}", selectedAnimalImagePath);

        // Highlight the viewed animal
        display.getAnimalImage().setColor(1, 0, 0, 1);
    }

    private void switchToStoryScreen() {
        game.setScreen(new StoryScreen(game, display.getAnimalType(currentAnimalIndex)));
    }

    void showAnimalDialog(int animalIndex, String animalImagePath) {
        String title = "Animal " + (animalIndex + 1);
        String content = display.getAnimalDescription(animalIndex);
        dialogHelper.displayDialog(title, content, animalImagePath, 900f, 500f, animalIndex, () -> switchToStoryScreen());
    }

    private void showAnimalDialog() {
        showAnimalDialog(currentAnimalIndex, selectedAnimalImagePath);
    }

    public void resetSelection() {
        selectedAnimalImagePath = null;
        currentAnimalIndex = 0;
        lastViewedAnimalIndex = -1;
        updateDisplayedAnimal();
        updateBackground();
    }
}
