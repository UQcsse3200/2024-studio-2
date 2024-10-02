package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AirAnimalSelectionDisplay extends AnimalRouletteDisplay {
    public AirAnimalSelectionDisplay(Stage stage, Skin skin) {
        super(stage, skin);
    }

    @Override
    protected String getBackgroundImagePath() {
        return "images/animal/SkyAnimalSelectionBG.jpeg";
    }

    @Override
    public String[] getAnimalImagePaths() {
        return new String[] {
                "images/bird.png",
                "images/bird.png",
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
        String[] types = {"Bird", "Bird", "Bird"};
        return types[index];
    }
}