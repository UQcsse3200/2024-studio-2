package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;

public class AnimalRouletteScreen extends ScreenAdapter {
    private final GdxGame game;
    private Stage stage;
    private Skin skin;
    private Image animalImage;
    private final String[] animalImages = {
            "images/dog.png",
            "images/croc.png",
            "images/bird.png"};
    private int currentAnimalIndex = 0;
    private PopUpHelper popUpHelper;

    public AnimalRouletteScreen(GdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        popUpHelper = new PopUpHelper(skin, stage);

        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        animalImage = new Image(new Texture(Gdx.files.internal(animalImages[currentAnimalIndex])));

        TextButton leftButton = new TextButton("<", skin);
        TextButton rightButton = new TextButton(">", skin);
        TextButton selectButton = new TextButton("Select", skin);

        leftButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentAnimalIndex = (currentAnimalIndex - 1 + animalImages.length) % animalImages.length;
                updateAnimalImage();
            }
        });

        rightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentAnimalIndex = (currentAnimalIndex + 1) % animalImages.length;
                updateAnimalImage();
            }
        });

        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showAnimalStats();
            }
        });

        table.add(leftButton).expandX().left();
        table.add(animalImage).center();
        table.add(rightButton).expandX().right();
        table.row();
        table.add(selectButton).colspan(3).center().padTop(20);
    }

    private void updateAnimalImage() {
        animalImage.setDrawable(new Image(new Texture(Gdx.files.internal(animalImages[currentAnimalIndex]))).getDrawable());
    }

    private void showAnimalStats() {
        String animalName = animalImages[currentAnimalIndex].replace(".png", "");
        popUpHelper.displayDialog(
                "Animal Stats",
                "Here are the stats for " + animalName,
                "images/animals/" + animalImages[currentAnimalIndex],
                400,
                300,
                currentAnimalIndex
        );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}