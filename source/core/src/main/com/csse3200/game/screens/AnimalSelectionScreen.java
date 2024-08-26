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

    /**
     * Constructor for AnimalSelectionScreen.
     *
     * @param game The main game instance.
     */
    public AnimalSelectionScreen(GdxGame game) {
        // Initialize the stage, which is the root container for all UI elements
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);  // Set the stage as the input processor to handle user input

        // Load the skin for UI elements from the specified JSON file
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Initialize the dialog helper with the skin and stage
        DialogHelper dialogHelper = new DialogHelper(skin, stage);

        // Set up the display component for animal selection, passing in the stage and skin
        display = new AnimalSelectionDisplay(stage, skin);

        // Set up actions for handling UI interactions, passing the display, dialog helper, and game instance
        actions = new AnimalSelectionActions(display, dialogHelper, game);
    }

    /**
     * Render method is called continuously to draw the screen.
     *
     * @param delta Time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Clear the screen to prevent overlapping frames
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw the stage (and all its actors) based on the current delta time
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    /**
     * Called when the screen is resized.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        // Update the stage's viewport to the new screen size, centering the stage
        stage.getViewport().update(width, height, true);
    }

    /**
     * Called when the screen is disposed to free resources.
     */
    @Override
    public void dispose() {
        // Dispose of the stage to free up resources
        stage.dispose();
    }
}
