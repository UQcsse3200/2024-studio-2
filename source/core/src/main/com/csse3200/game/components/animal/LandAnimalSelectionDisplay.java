package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Class representing the land animal selection screen.
 */
public class LandAnimalSelectionDisplay extends AnimalSelectionDisplay {

    public LandAnimalSelectionDisplay(Stage stage, Skin skin) {
        super(stage, skin);
    }

    @Override
    protected String getBackgroundImagePath() {
        return "images/animal/JungleAnimalSelectionBG.jpeg"; // Land background image
    }

    @Override
    protected String[] getAnimalImagePaths() {
        return new String[] {
                "images/dog.png",
                "images/croc.png",
                "images/bird.png"
        }; // Land animal images
    }
}
