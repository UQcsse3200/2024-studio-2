package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Texture;

public class AnimalSelectionDisplay {
    private final Stage stage;
    private final Skin skin;
    private final Image[] animalImages;
    private final TextButton[] animalButtons;
    private final TextButton selectButton;
    private final TextButton backButton;

    public AnimalSelectionDisplay(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
        this.animalImages = new Image[3];
        this.animalButtons = new TextButton[3];
        this.selectButton = new TextButton("Ready?", skin);
        this.backButton = new TextButton("Go Back", skin);
        initializeDisplay();
    }

    private void initializeDisplay() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().padTop(80);
        stage.addActor(mainTable);

        String[] animalImagePaths = {
                "images/dog.png",
                "images/croc.png",
                "images/bird.png"
        };

        for (int i = 0; i < 3; i++) {
            animalImages[i] = new Image(new Texture(animalImagePaths[i]));
            animalButtons[i] = new TextButton("Animal " + (i + 1), skin);

            Table animalTable = new Table();
            animalTable.add(animalImages[i]).pad(20).padLeft(180);
            animalTable.row();
            animalTable.add(animalButtons[i]).pad(10).padLeft(180);

            mainTable.add(animalTable).pad(10).expandX();
        }

        mainTable.row();
        mainTable.add().expandY();

        Table buttonTable = new Table();
        buttonTable.add(selectButton).padBottom(10).width(300).height(60).padRight(250);
        buttonTable.add(backButton).padBottom(10).width(300).height(60).padRight(380);

        mainTable.add(buttonTable).center().padBottom(60).colspan(60).bottom();
    }

    public Image[] getAnimalImages() {
        return animalImages;
    }

    public TextButton[] getAnimalButtons() {
        return animalButtons;
    }

    public TextButton getSelectButton() {
        return selectButton;
    }

    public TextButton getBackButton() {
        return backButton;
    }

    public Skin getSkin() {
        return skin;
    }

    public Stage getStage() {
        return stage;
    }
}
