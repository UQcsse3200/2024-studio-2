package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Class representing the air animal selection screen.
 */
public class AirAnimalSelectionDisplay extends AnimalSelectionDisplay {

    public AirAnimalSelectionDisplay(Stage stage, Skin skin) {
        super(stage, skin);
    }

    @Override
    protected String getBackgroundImagePath() {
        return "images/animal/SkyAnimalSelectionBG.jpeg"; // Air background image
    }

    @Override
    protected String[] getAnimalImagePaths() {
        return new String[] {
                "images/bird.png", // Add actual air animal images like "images/eagle.png", etc.
                "images/bird.png",
                "images/bird.png"
        };
    }
}
