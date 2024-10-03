package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.AirAnimalSelectionDisplay;
import com.csse3200.game.components.animal.AnimalRouletteDisplay1;
import com.csse3200.game.components.animal.WaterAnimalSelectionDisplay;

public class AirAnimalSelectionScreen extends AnimalRouletteScreen1 {

    public AirAnimalSelectionScreen(GdxGame game) {
        super(game);
    }

    @Override
    protected AnimalRouletteDisplay1 createDisplay(Stage stage, Skin skin) {
        return new AirAnimalSelectionDisplay(stage, skin);
    }
}



