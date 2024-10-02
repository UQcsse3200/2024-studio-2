package com.csse3200.game.components.animal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LandAnimalSelectionDisplay extends AnimalRouletteDisplay {
    private Image[] animalImages;
    private TextButton[] animalButtons;
    public LandAnimalSelectionDisplay(Stage stage, Skin skin) {
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
        return "images/animal/JungleAnimalSelectionBG.jpeg";
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
                "The dog is a loyal and brave companion, known for its agility and combat prowess.",
                "The crocodile is a powerful reptile with incredible strength and tough scales.",
                "The bird is a versatile creature with the ability to fly and survey the battlefield from above."
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