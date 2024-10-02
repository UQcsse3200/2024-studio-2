package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Class representing the water animal selection screen.
 */
public class WaterAnimalSelectionDisplay extends AnimalSelectionDisplay {

    public WaterAnimalSelectionDisplay(Stage stage, Skin skin) {
        super(stage, skin);
    }

    @Override
    protected String getBackgroundImagePath() {
        return "images/animal/WaterAnimalSelectionBG.jpeg"; // Water background image
    }

    @Override
    protected String[] getAnimalImagePaths() {
        return new String[] {
                "images/croc.png", // Add actual water animal images like "images/whale.png", etc.
                "images/croc.png",
                "images/croc.png"
        };
    }
}
