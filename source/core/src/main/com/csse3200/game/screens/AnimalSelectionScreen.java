package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalSelectionScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AnimalSelectionScreen.class);
    private final GdxGame game;
    private Stage stage;
    private Table table;

    public AnimalSelectionScreen(GdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize stage and set input processor
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Create a Table to arrange UI elements
        table = new Table();
        table.setFillParent(true);

        // Load images for animal options
        Image animal1Image = new Image(new Texture("images/animal1.png"));
        Image animal2Image = new Image(new Texture("images/animal2.png"));

        // Load Skin for TextButton
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        TextButton selectButton = new TextButton("Select", skin);

        // Arrange UI elements in the Table
        table.add(animal1Image).pad(10);
        table.row();
        table.add(animal2Image).pad(10);
        table.row();
        table.add(selectButton).pad(10);

        // Add the Table to the Stage
        stage.addActor(table);

        // Add listener to handle button clicks
        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Animal selected");
                // Handle animal selection here
                // Example: game.setScreen(new NextScreen(game)); // Transition to another screen
            }
        });
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw the stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport when the window is resized
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        // Dispose of assets and resources
        stage.dispose();
    }
}
