package com.csse3200.game.components.animal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class WaterAnimalSelectionDisplay extends AnimalRouletteDisplay1 {
    private Image[] animalImages;
    private TextButton[] animalButtons;
    public WaterAnimalSelectionDisplay(Stage stage, Skin skin) {
        super(stage, skin);
        initializeAnimalImagesAndButtons();
    }

    private void initializeAnimalImagesAndButtons() {
        String[] imagePaths = getAnimalImagePaths();
        animalImages = new Image[imagePaths.length];
        animalButtons = new TextButton[imagePaths.length];

        for (int i = 0; i < imagePaths.length; i++) {
            animalImages[i] = new Image(new Texture(imagePaths[i]));
            animalButtons[i] = new TextButton(getAnimalType(i), getSkin());
        }
    }

    @Override
    protected String getBackgroundImagePath() {
        return "images/animal/WaterAnimalSelectionBG.jpeg";
    }

    @Override
    public String[] getAnimalImagePaths() {
        return new String[] {
                "images/dog.png",
                "images/croc.png",
                "images/bird.png"
        };
    }

    @Override
    public String getAnimalDescription(int index) {
        String[] descriptions = {
                "The eagle is a powerful bird of prey with excellent vision.",
                "The owl is a nocturnal bird known for its wisdom and silent flight.",
                "The parrot is a colorful and intelligent bird capable of mimicking human speech."
        };
        return descriptions[index];
    }

    @Override
    public String getAnimalType(int index) {
        String[] types = {"Dog", "Croc", "Bird"};
        return types[index];
    }

    public Image[] getAnimalImages() {
        return animalImages;
    }

    public TextButton[] getAnimalButtons() {
        return animalButtons;
    }
}