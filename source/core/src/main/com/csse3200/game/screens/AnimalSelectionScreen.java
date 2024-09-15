package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        PopUpHelper dialogHelper = new PopUpHelper(skin, stage);

        display = createDisplay(stage, skin);
        actions = new AnimalSelectionActions(display, dialogHelper, game);

        // Add buttons to switch to other animal selection screens
        addButtonToSwitchScreen("Water Animals", WaterAnimalSelectionScreen.class, skin);
        addButtonToSwitchScreen("Air Animals", AirAnimalSelectionScreen.class, skin);

        actions.resetSelection();
    }

    // Abstract method to be implemented by subclasses
    protected abstract AnimalSelectionDisplay createDisplay(Stage stage, Skin skin);

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

    // Method to add buttons to switch between different screens
    protected void addButtonToSwitchScreen(String buttonText, final Class<? extends ScreenAdapter> screenClass, Skin skin) {
        TextButton button = new TextButton(buttonText, skin);

        // Define button dimensions
        float buttonWidth = 200;
        float buttonHeight = 50;
        float padding = 20;

        // Position buttons on the left side, bottom of the screen
        float xPos = padding;
        float yPos;

        if (buttonText.equals("Water Animals")) {
            yPos = padding;
        } else if (buttonText.equals("Air Animals")) {
            yPos = padding + buttonHeight + padding;
        } else {
            return;
        }

        button.setBounds(xPos, yPos, buttonWidth, buttonHeight);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    // Switch to the new screen
                    ScreenAdapter newScreen = screenClass.getConstructor(GdxGame.class).newInstance(game);
                    game.setScreen(newScreen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stage.addActor(button);
    }
}
