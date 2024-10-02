package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.AirAnimalSelectionDisplay;
import com.csse3200.game.components.animal.AnimalRouletteDisplay;
import com.csse3200.game.components.animal.AnimalRouletteActions;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;

public class AirAnimalSelectionScreen extends AnimalRouletteScreen {
    public AirAnimalSelectionScreen(GdxGame game) {
        super(game);
    }

    @Override
    protected AnimalRouletteDisplay createDisplay(Stage stage, Skin skin) {
        return new AirAnimalSelectionDisplay(stage, skin);
    }

    @Override
    protected void createUI(Skin skin) {
        super.createUI(skin);

        getAirAnimalsButton().setVisible(false);
    }

    protected AnimalRouletteActions createActions(AnimalRouletteDisplay display, PopUpHelper dialogHelper, GdxGame game) {
        return new AnimalRouletteActions(display, dialogHelper, game);
    }
}