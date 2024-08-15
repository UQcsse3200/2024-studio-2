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
    private Table mainTable;

    public AnimalSelectionScreen(GdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        Image animal1Image = new Image(new Texture("images/animal1.png"));
        Image animal2Image = new Image(new Texture("images/animal2.png"));
        Image animal3Image = new Image(new Texture("images/animal3.png"));

        TextButton animal1Button = new TextButton("Animal 1", skin);
        TextButton animal2Button = new TextButton("Animal 2", skin);
        TextButton animal3Button = new TextButton("Animal 3", skin);

        Table animal1Table = new Table();
        animal1Table.add(animal1Image).pad(10);
        animal1Table.row();
        animal1Table.add(animal1Button).pad(10);

        Table animal2Table = new Table();
        animal2Table.add(animal2Image).pad(10);
        animal2Table.row();
        animal2Table.add(animal2Button).pad(10);

        Table animal3Table = new Table();
        animal3Table.add(animal3Image).pad(10);
        animal3Table.row();
        animal3Table.add(animal3Button).pad(10);

        mainTable.add(animal1Table).pad(10);
        mainTable.add(animal2Table).pad(10);
        mainTable.add(animal3Table).pad(10);
        mainTable.row();

        TextButton selectButton = new TextButton("Select", skin);
        TextButton backButton = new TextButton("Go Back", skin);

        mainTable.add(selectButton).pad(10).colspan(3);
        mainTable.row();
        mainTable.add(backButton).pad(10).colspan(3);

        animal1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Animal 1 selected");
                // Handle selection of Animal 1
            }
        });

        animal2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Animal 2 selected");
                // Handle selection of Animal 2
            }
        });

        animal3Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Animal 3 selected");
                // Handle selection of Animal 3
            }
        });

        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Select button clicked");
                // Handle selection of the chosen animal here
                // Example: game.setScreen(new NextScreen(game)); // Transition to another screen
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Go back button clicked");
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });
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
