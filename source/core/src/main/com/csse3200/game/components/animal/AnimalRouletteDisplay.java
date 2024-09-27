package com.csse3200.game.components.animal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.Component;

public class AnimalRouletteDisplay extends Component {
    private Table table;
    private Image animalImage;
    private final String[] animalImages = {"dog.png", "crocodile.png", "bird.png"};
    private int currentAnimalIndex = 0;
    private Skin skin;
    private Stage stage;

    @Override
    public void create() {
        super.create();
        createRouletteUI();
    }

    /**
     * Creates the UI for the roulette screen.
     */
    private void createRouletteUI() {
        table = new Table();
        table.setFillParent(true);

        // Create animal image display
        animalImage = new Image(new Texture(animalImages[currentAnimalIndex]));

        // Add left and right buttons for navigation
        TextButton leftButton = new TextButton("<", skin);
        TextButton rightButton = new TextButton(">", skin);

        leftButton.addListener(event -> {
            currentAnimalIndex = (currentAnimalIndex - 1 + animalImages.length) % animalImages.length;
            animalImage.setDrawable(new TextureRegionDrawable(new Texture(animalImages[currentAnimalIndex])));
            return true;
        });

        rightButton.addListener(event -> {
            currentAnimalIndex = (currentAnimalIndex + 1) % animalImages.length;
            animalImage.setDrawable(new TextureRegionDrawable(new Texture(animalImages[currentAnimalIndex])));
            return true;
        });

        // Set up layout
        table.add(leftButton).expandX().left();
        table.add(animalImage).center();
        table.add(rightButton).expandX().right();

        stage.addActor(table);
    }

    public String getSelectedAnimal() {
        return animalImages[currentAnimalIndex];
    }

    public int getCurrentAnimalIndex() {
        return currentAnimalIndex;
    }

}
