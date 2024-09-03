package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.WaterAnimalSelectionDisplay;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;

public class WaterAnimalSelectionScreen extends ScreenAdapter {
    private Stage stage;
    private WaterAnimalSelectionDisplay display;
    private AnimalSelectionActions actions;

    public WaterAnimalSelectionScreen(GdxGame game) {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        PopUpHelper dialogHelper = new PopUpHelper(skin, stage);

        display = new WaterAnimalSelectionDisplay(stage, skin);
        actions = new AnimalSelectionActions(display, dialogHelper, game);

        actions.resetSelection();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
