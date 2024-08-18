package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ui.DialogueBox.DialogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalSelectionScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AnimalSelectionScreen.class);
    private static final int NUM_ANIMALS = 3;
    private static final float IMAGE_SCALE = 1.2f;
    private final GdxGame game;
    private Stage stage;
    private Table mainTable;
    private Image selectedAnimalImage;
    private TextButton selectButton;
    private DialogHelper dialogHelper;

    public AnimalSelectionScreen(GdxGame game) {
        this.game = game;
        initializeUI();
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        this.dialogHelper = new DialogHelper(stage, skin);
        logger.info("AnimalSelectionScreen initialized");
    }

    private void initializeUI() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().padTop(80);
        stage.addActor(mainTable);

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        String[] animalImagePaths = {
                "images/animal1.png",
                "images/animal2.png",
                "images/animal3.png"
        };

        Image[] animalImages = new Image[NUM_ANIMALS];
        TextButton[] animalButtons = new TextButton[NUM_ANIMALS];

        for (int i = 0; i < NUM_ANIMALS; i++) {
            animalImages[i] = new Image(new Texture(animalImagePaths[i]));
            animalImages[i].setScale(IMAGE_SCALE);

            final int animalIndex = i;
            animalButtons[i] = new TextButton("Animal " + (i + 1), skin);

            Table animalTable = new Table();

            if (i == 1) {
                animalTable.add(animalImages[i]).pad(20).padLeft(240);
                animalTable.row();
                animalTable.add(animalButtons[i]).pad(10).center().padLeft(240);
            } else {
                animalTable.add(animalImages[i]).pad(20);
                animalTable.row();
                animalTable.add(animalButtons[i]).pad(10).center();
            }

            mainTable.add(animalTable).pad(10).expandX();

            animalImages[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    logger.debug("Animal {} image clicked", animalIndex + 1);
                    selectAnimal(animalImages[animalIndex]);
                    showAnimalDialog(animalIndex);
                }
            });

            animalButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    logger.debug("Animal {} button clicked", animalIndex + 1);
                    selectAnimal(animalImages[animalIndex]);
                    showAnimalDialog(animalIndex);
                }
            });
        }

        mainTable.row();
        mainTable.add().expandY();

        selectButton = new TextButton("Ready?", skin);
        selectButton.getLabel().setFontScale(1.5f);

        TextButton backButton = new TextButton("Go Back", skin);

        selectButton.setSize(500, 60);
        backButton.setSize(500, 60);

        Table buttonTable = new Table();
        buttonTable.add(selectButton).padBottom(10).width(300).height(60).padRight(250);
        buttonTable.add(backButton).padBottom(10).width(300).height(60).padRight(380);

        mainTable.add(buttonTable).center().padBottom(60).colspan(60).bottom();

        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedAnimalImage != null) {
                    logger.debug("Select button clicked with animal selected");
                    game.setScreen(new MainGameScreen(game));
                } else {
                    logger.debug("No animal selected");
                    showSelectionAlert();
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
            selectedAnimalImage.setColor(1, 1, 1, 1);
        }
        selectedAnimalImage = animalImage;
        selectedAnimalImage.setColor(1, 0, 0, 1);
        logger.debug("Animal selected: {}", animalImage.getName());
    }

    private void showSelectionAlert() {
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        Dialog dialog = new Dialog("Alert", skin) {
            @Override
            protected void result(Object object) {
            }
        };

        dialog.text("Please select an animal first.");
        dialog.button("OK", true);
        dialog.show(stage);
    }

    private void showAnimalDialog(int animalIndex) {
        String[] titles = {"Animal " + (animalIndex + 1), "Characteristics", "Abilities"};
        String[] content = {
                "You've selected Animal " + (animalIndex + 1) + ".",
                "This animal has unique characteristics.",
                "It possesses special abilities."
        };
        dialogHelper.displayDialog(titles, content);
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