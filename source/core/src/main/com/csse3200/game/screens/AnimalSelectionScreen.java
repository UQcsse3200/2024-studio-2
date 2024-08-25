package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.AnimalSelectionDisplay;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.ui.DialogueBox.DialogHelper;

public class AnimalSelectionScreen extends ScreenAdapter {
    private Stage stage;
    private AnimalSelectionDisplay display;
    private AnimalSelectionActions actions;

    public AnimalSelectionScreen(GdxGame game) {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        DialogHelper dialogHelper = new DialogHelper(skin, stage); // Corrected order here

        display = new AnimalSelectionDisplay(stage, skin);
        actions = new AnimalSelectionActions(display, dialogHelper, game);  // Passed the game instance to actions
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
