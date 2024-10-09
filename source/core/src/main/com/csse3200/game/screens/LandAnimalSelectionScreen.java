package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.AnimalRouletteDisplay1;
import com.csse3200.game.components.animal.AnimalRouletteActions1;
import com.csse3200.game.components.animal.LandAnimalSelectionDisplay;
import com.csse3200.game.ui.pop_up_dialog_box.PopUpHelper;

public class LandAnimalSelectionScreen extends AnimalRouletteScreen1 {
    public LandAnimalSelectionScreen(GdxGame game) {
        super(game);
    }

    @Override
    protected AnimalRouletteDisplay1 createDisplay(Stage stage, Skin skin) {
        return new LandAnimalSelectionDisplay(stage, skin);
    }

    @Override
    protected void createUI(Skin skin) {
        super.createUI(skin);

        getLandAnimalsButton().setVisible(false);
    }

    @Override
    protected AnimalRouletteActions1 createActions(AnimalRouletteDisplay1 display, PopUpHelper dialogHelper, GdxGame game) {
        return new AnimalRouletteActions1(display, dialogHelper, game);
    }
}