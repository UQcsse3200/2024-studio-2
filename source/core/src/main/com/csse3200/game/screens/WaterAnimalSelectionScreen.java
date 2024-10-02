package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.WaterAnimalSelectionDisplay;
import com.csse3200.game.components.animal.AnimalRouletteDisplay1;

public class WaterAnimalSelectionScreen extends AnimalRouletteScreen1 {

    public WaterAnimalSelectionScreen(GdxGame game) {
        super(game);
    }

    @Override
    protected AnimalRouletteDisplay1 createDisplay(Stage stage, Skin skin) {
        return new WaterAnimalSelectionDisplay(stage, skin);
    }
}