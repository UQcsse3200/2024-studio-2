package com.csse3200.game.components.animal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LandAnimalSelectionDisplay extends AnimalRouletteDisplay1 {
    public LandAnimalSelectionDisplay(Stage stage, Skin skin) {
        super(stage, skin);
        initializeAnimalImagesAndButtons();
    }
    
    @Override
    protected String getBackgroundImagePath() {
        return "images/animal/JungleAnimalSelectionBG.jpeg";
    }
}