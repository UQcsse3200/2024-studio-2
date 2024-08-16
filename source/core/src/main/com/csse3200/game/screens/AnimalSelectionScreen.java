package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalSelectionScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AnimalSelectionScreen.class);
    private final GdxGame game;
    private Stage stage;
    private Table mainTable;
    private Image selectedAnimalImage;
    private TextButton selectButton;

    public AnimalSelectionScreen(GdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().padTop(80); // Adjust padding to move images slightly down from the top
        stage.addActor(mainTable);

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        Image animal1Image = new Image(new Texture("images/animal1.png"));
        Image animal2Image = new Image(new Texture("images/animal2.png"));
        Image animal3Image = new Image(new Texture("images/animal3.png"));

        // Adjust image scale to make them larger but not too big
        float imageScale = 1.2f;
        animal1Image.setScale(imageScale);
        animal2Image.setScale(imageScale);
        animal3Image.setScale(imageScale);

        TextButton animal1Button = new TextButton("Animal 1", skin);
        TextButton animal2Button = new TextButton("Animal 2", skin);
        TextButton animal3Button = new TextButton("Animal 3", skin);

        selectButton = new TextButton("Ready?", skin);
        selectButton.getLabel().setFontScale(1.5f); // Make the "Ready?" button text larger
        TextButton backButton = new TextButton("Go Back", skin);

        // Adjust buttons size to make them bigger and elongated
        selectButton.setSize(500, 60);
        backButton.setSize(500, 60);

        Table animal1Table = new Table();
        animal1Table.add(animal1Image).pad(20);
        animal1Table.row();
        animal1Table.add(animal1Button).pad(10).center();

        Table animal2Table = new Table();
        animal2Table.add(animal2Image).pad(20).padLeft(190);
        animal2Table.row();
        animal2Table.add(animal2Button).pad(10).center().padLeft(190);

        Table animal3Table = new Table();
        animal3Table.add(animal3Image).pad(20);
        animal3Table.row();
        animal3Table.add(animal3Button).pad(10).center();

        mainTable.add(animal1Table).pad(10).expandX(); // Spread the images across the width
        mainTable.add(animal2Table).pad(10).expandX();
        mainTable.add(animal3Table).pad(10).expandX();
        mainTable.row();

        // Empty row to create space between images and buttons
        mainTable.add().expandY();

        // Place the "Ready?" and "Go Back" buttons side by side at the bottom center of the screen
        Table buttonTable = new Table();
        buttonTable.add(selectButton).padBottom(10).width(300).height(60).padRight(250);
        buttonTable.add(backButton).padBottom(10).width(300).height(60).padRight(380);

        // Center the buttonTable at the bottom of the screen
        mainTable.add(buttonTable).center().padBottom(60).colspan(60).bottom();

        animal1Image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.debug("Animal 1 image clicked");
                selectAnimal(animal1Image);
            }
        });

        animal2Image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.debug("Animal 2 image clicked");
                selectAnimal(animal2Image);
            }
        });

        animal3Image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.debug("Animal 3 image clicked");
                selectAnimal(animal3Image);
            }
        });

        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedAnimalImage != null) {
                    logger.debug("Select button clicked with animal selected");
                    game.setScreen(new MainGameScreen(game)); // Transition to the main game screen
                } else {
                    logger.debug("No animal selected");
                    showSelectionAlert(); // Show the popup if no animal is selected
                }
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

    private void selectAnimal(Image animalImage) {
        if (selectedAnimalImage != null) {
            selectedAnimalImage.setColor(1, 1, 1, 1); // Reset previous selection color
        }
        selectedAnimalImage = animalImage;
        selectedAnimalImage.setColor(1, 0, 0, 1); // Highlight selected animal
        logger.debug("Animal selected: {}", animalImage.getName());
    }

    private void showSelectionAlert() {
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        Dialog dialog = new Dialog("Alert", skin) {
            @Override
            protected void result(Object object) {
                // Handle dialog result if needed
            }
        };

        dialog.text("Please select an animal first.");
        dialog.button("OK", true);
        dialog.show(stage);
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
