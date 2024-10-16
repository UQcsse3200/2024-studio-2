package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.AnimalRouletteActions1;
import com.csse3200.game.components.animal.AnimalRouletteDisplay1;
import com.csse3200.game.ui.pop_up_dialog_box.PopUpHelper;

public abstract class AnimalRouletteScreen1 extends ScreenAdapter {
    protected Stage stage;
    protected AnimalRouletteDisplay1 display;
    protected AnimalRouletteActions1 actions;
    protected GdxGame game;
    private TextButton waterAnimalsButton;
    private TextButton airAnimalsButton;
    private TextButton landAnimalsButton;

    protected AnimalRouletteScreen1(GdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        PopUpHelper dialogHelper = new PopUpHelper(skin, stage);

        display = createDisplay(stage, skin);
        actions = new AnimalRouletteActions1(display, dialogHelper, game);

        createUI(skin);

        actions.resetSelection();
    }

    protected abstract AnimalRouletteDisplay1 createDisplay(Stage stage, Skin skin);

    protected AnimalRouletteActions1 createActions(AnimalRouletteDisplay1 display, PopUpHelper dialogHelper, GdxGame game) {
        return new AnimalRouletteActions1(display, dialogHelper, game);
    }

    void createUI(Skin skin) {
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
        //updateButtonPositions();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void addButtonToSwitchScreen(TextButton button, final Class<? extends ScreenAdapter> screenClass) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    ScreenAdapter newScreen = screenClass.getConstructor(GdxGame.class).newInstance(game);
                    game.setScreen(newScreen);
                } catch (Exception e) {
                    throw new GdxRuntimeException("Tried to add screen " + screenClass.getName() + " to the screen via button: " + button.toString(), e);
                }
            }
        });
        stage.addActor(button);
    }

    //protected TextButton getWaterAnimalsButton() { return waterAnimalsButton; }
    //protected TextButton getAirAnimalsButton() { return airAnimalsButton; }
    //protected TextButton getLandAnimalsButton() { return landAnimalsButton; }

    private void updateButtonPositions() {
        float buttonWidth = 200;
        float buttonHeight = 50;
        float padding = 20;

        float xPos = padding;
        float yPosWater = stage.getViewport().getScreenHeight() - buttonHeight - padding;
        float yPosAir = yPosWater - buttonHeight - padding;
        float yPosLand = yPosAir - buttonHeight - padding;

        waterAnimalsButton.setBounds(xPos, yPosWater, buttonWidth, buttonHeight);
        airAnimalsButton.setBounds(xPos, yPosAir, buttonWidth, buttonHeight);
        landAnimalsButton.setBounds(xPos, yPosLand, buttonWidth, buttonHeight);

    }
}