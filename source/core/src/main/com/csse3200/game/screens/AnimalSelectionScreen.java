package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.components.animal.AnimalSelectionDisplay;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;

public abstract class AnimalSelectionScreen extends ScreenAdapter {
    protected Stage stage;
    protected AnimalSelectionDisplay display;
    protected AnimalSelectionActions actions;
    protected GdxGame game;
    private TextButton waterAnimalsButton;
    private TextButton airAnimalsButton;

    public AnimalSelectionScreen(GdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        PopUpHelper dialogHelper = new PopUpHelper(skin, stage);

        display = createDisplay(stage, skin);
        actions = new AnimalSelectionActions(display, dialogHelper, game);

        createUI(skin);

        actions.resetSelection();
    }

    protected abstract AnimalSelectionDisplay createDisplay(Stage stage, Skin skin);

    void createUI(Skin skin) {
        waterAnimalsButton = new TextButton("Water Animals", skin);
        airAnimalsButton = new TextButton("Air Animals", skin);

        addButtonToSwitchScreen(waterAnimalsButton, WaterAnimalSelectionScreen.class);
        addButtonToSwitchScreen(airAnimalsButton, AirAnimalSelectionScreen.class);

        updateButtonPositions();
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
        updateButtonPositions();
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
                    e.printStackTrace();
                }
            }
        });
        stage.addActor(button);
    }

    private void updateButtonPositions() {
        float buttonWidth = 200;
        float buttonHeight = 50;
        float padding = 20;

        float xPos = padding;
        float yPosWater = stage.getViewport().getScreenHeight() - buttonHeight - padding;
        float yPosAir = yPosWater - buttonHeight - padding;

        waterAnimalsButton.setBounds(xPos, yPosWater, buttonWidth, buttonHeight);
        airAnimalsButton.setBounds(xPos, yPosAir, buttonWidth, buttonHeight);
    }
}