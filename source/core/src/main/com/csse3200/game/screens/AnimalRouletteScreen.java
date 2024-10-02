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
import com.csse3200.game.components.animal.AnimalRouletteActions;
import com.csse3200.game.components.animal.AnimalRouletteDisplay;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;

public abstract class AnimalRouletteScreen extends ScreenAdapter {
    protected Stage stage;
    protected AnimalRouletteDisplay display;
    protected AnimalRouletteActions actions;
    protected GdxGame game;
    private TextButton waterAnimalsButton;
    private TextButton airAnimalsButton;
    private TextButton landAnimalsButton;

    public AnimalRouletteScreen(GdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        PopUpHelper dialogHelper = new PopUpHelper(skin, stage);

        display = createDisplay(stage, skin);
        actions = new AnimalRouletteActions(display, dialogHelper, game);

        createUI(skin);

        actions.resetSelection();
    }

    protected abstract AnimalRouletteDisplay createDisplay(Stage stage, Skin skin);

    protected AnimalRouletteActions createActions(AnimalRouletteDisplay display, PopUpHelper dialogHelper, GdxGame game) {
        return new AnimalRouletteActions(display, dialogHelper, game);
    }

    void createUI(Skin skin) {
        waterAnimalsButton = new TextButton("Water Animals", skin);
        airAnimalsButton = new TextButton("Air Animals", skin);
        landAnimalsButton = new TextButton("Land Animals", skin);

        addButtonToSwitchScreen(waterAnimalsButton, WaterAnimalSelectionScreen.class);
        addButtonToSwitchScreen(airAnimalsButton, AirAnimalSelectionScreen.class);
        addButtonToSwitchScreen(landAnimalsButton, LandAnimalSelectionScreen.class);

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

    protected TextButton getWaterAnimalsButton() { return waterAnimalsButton; }
    protected TextButton getAirAnimalsButton() { return airAnimalsButton; }
    protected TextButton getLandAnimalsButton() { return landAnimalsButton; }

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