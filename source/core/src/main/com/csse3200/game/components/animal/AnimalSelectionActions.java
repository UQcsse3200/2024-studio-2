package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.MainGameScreen;
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
                "images/animal1.png",
                "images/animal2.png",
                "images/animal3.png"
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
        Dialog dialog = new Dialog("Alert", display.getSkin()) {
            @Override
            protected void result(Object object) {
            }
        };

        dialog.text("Please select an animal first.");
        dialog.button("OK", true);
        dialog.show(display.getStage());
    }

    private void showAnimalDialog(int animalIndex) {
        String[] titles = {"Animal " + (animalIndex + 1), "Characteristics", "Abilities"};
        String[] content = {
                "You've selected Animal " + (animalIndex + 1) + ".",
                "This animal has unique characteristics.",
                "It possesses special abilities."
        };
        dialogHelper.displayDialog(titles, content);
    }
}
