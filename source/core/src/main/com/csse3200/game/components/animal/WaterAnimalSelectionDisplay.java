package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Texture;

public class WaterAnimalSelectionDisplay extends LandAnimalSelectionDisplay {
    public WaterAnimalSelectionDisplay(Stage stage, Skin skin) {
        super(stage, skin);
        initializeDisplay();
    }


    protected void initializeDisplay() {
        // Initialize and add the background image to the stage
        BackgroundImage backgroundImage = new BackgroundImage("images/animal/WaterAnimalSelectionBG.jpeg");
        getStage().addActor(backgroundImage);

        // Create the main table layout for positioning UI elements
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().padTop(80);
        getStage().addActor(mainTable);

        // Paths to the images of the animals
        String[] animalImagePaths = {
                "images/croc.png",
                "images/croc.png",
                "images/croc.png"
        };

        // Add images and buttons for each animal
        for (int i = 0; i < 3; i++) {
            getAnimalImages()[i] = new Image(new Texture(animalImagePaths[i]));
            getAnimalButtons()[i] = new TextButton("Animal " + (i + 1), getSkin());

            Table animalTable = new Table();
            animalTable.add(getAnimalImages()[i]).pad(20).padLeft(180);
            animalTable.row();
            animalTable.add(getAnimalButtons()[i]).pad(10).padLeft(180);

            mainTable.add(animalTable).pad(10).expandX();
        }

        mainTable.row();
        mainTable.add().expandY();

        Table buttonTable = new Table();
        buttonTable.add(getSelectButton()).padBottom(10).width(300).height(60).padRight(250);
        buttonTable.add(getBackButton()).padBottom(10).width(300).height(60).padRight(380);

        mainTable.add(buttonTable).center().padBottom(60).colspan(60).bottom();
    }
}
