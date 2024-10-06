package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.AnimalRouletteActions1;
import com.csse3200.game.components.animal.AnimalRouletteDisplay1;
import com.csse3200.game.components.animal.WaterAnimalSelectionDisplay;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;

public class WaterAnimalSelectionScreen1 extends AnimalRouletteScreen1 {
    public WaterAnimalSelectionScreen1(GdxGame game) {
        super(game);
    }

    @Override
    protected AnimalRouletteDisplay1 createDisplay(Stage stage, Skin skin) {
        return new WaterAnimalSelectionDisplay(stage, skin);
    }

    @Override
    protected void createUI(Skin skin) {
        super.createUI(skin);

        getWaterAnimalsButton().setVisible(false);
    }

    @Override
    protected AnimalRouletteActions1 createActions(AnimalRouletteDisplay1 display, PopUpHelper dialogHelper, GdxGame game) {
        return new AnimalRouletteActions1(display, dialogHelper, game);
    }
}