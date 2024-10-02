package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.AirAnimalSelectionDisplay;
import com.csse3200.game.components.animal.AnimalSelectionDisplay;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;

public class AirAnimalSelectionScreen extends AnimalSelectionScreen {
    public AirAnimalSelectionScreen(GdxGame game) {
        super(game);
    }

    @Override
    protected AnimalSelectionDisplay createDisplay(Stage stage, Skin skin) {
        return new AirAnimalSelectionDisplay(stage, skin);
    }

    @Override
    protected void createUI(Skin skin) {
        super.createUI(skin);
    }

    protected AnimalSelectionActions createActions(AnimalSelectionDisplay display, PopUpHelper dialogHelper, GdxGame game) {
        return new AnimalSelectionActions(display, dialogHelper, game);
    }
}