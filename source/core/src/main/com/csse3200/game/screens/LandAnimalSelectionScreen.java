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
import com.csse3200.game.components.animal.LandAnimalSelectionDisplay;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;

public class LandAnimalSelectionScreen extends ScreenAdapter {
    private Stage stage;
    private LandAnimalSelectionDisplay display;
    private AnimalSelectionActions actions;

    private GdxGame game;

    /**
     * Constructor for AnimalSelectionScreen.
     *
     * @param game The main game instance.
     */
    public LandAnimalSelectionScreen(GdxGame game) {
        // Initialize the stage, which is the root container for all UI elements
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);  // Set -up of  stage as the input processor to handle user input
        this.game = game;

        // Load the skin for UI elements from the specified JSON file
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Initialize the dialog helper with the skin and stage
        PopUpHelper dialogHelper = new PopUpHelper(skin, stage);

        // Set up the display component for animal selection, passing in the stage and skin
        display = new LandAnimalSelectionDisplay(stage, skin);

        // Set up actions for handling UI interactions, passing the display, dialog helper, and game instance
        actions = new AnimalSelectionActions(display, dialogHelper, game);
        addButtonToSwitchScreen("Water Animals", WaterAnimalSelectionScreen.class, skin);

        actions.resetSelection(); // Reset the selection when the screen is created
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
    private void addButtonToSwitchScreen(String buttonText, final Class<? extends ScreenAdapter> screenClass, Skin skin) {
        TextButton button = new TextButton(buttonText, skin);
        button.setPosition(50, stage.getHeight() - 100 * (stage.getActors().size + 1));  // Position buttons dynamically
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
