package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.AnimalRouletteActions1;
import com.csse3200.game.components.animal.AnimalRouletteDisplay1;
import com.csse3200.game.ui.pop_up_dialog_box.PopUpHelper;

public class AnimalRouletteScreen1 extends ScreenAdapter {
    protected Stage stage;
    protected AnimalRouletteDisplay1 display;
    protected AnimalRouletteActions1 actions;
    protected GdxGame game;

    public AnimalRouletteScreen1(GdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        PopUpHelper dialogHelper = new PopUpHelper(skin, stage);

        display = new AnimalRouletteDisplay1(stage, skin);
        actions = new AnimalRouletteActions1(display, dialogHelper, game);

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