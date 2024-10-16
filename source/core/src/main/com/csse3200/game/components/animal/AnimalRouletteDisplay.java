package com.csse3200.game.components.animal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.ui.CustomButton;
import com.csse3200.game.ui.UIComponent;

public class AnimalRouletteDisplay extends UIComponent {
    private Table table;
    private Image animalImage;
    private final String[] animalImages = {
            "images/dog.png",
            "images/croc.png",
            "images/bird.png"};
    private int currentAnimalIndex = 0;
    private CustomButton leftButton;
    private CustomButton rightButton;

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
        leftButton = new CustomButton("<", skin);
        leftButton.setButtonStyle(CustomButton.Style.SMALL, skin);
        rightButton = new CustomButton(">", skin);
        rightButton.setButtonStyle(CustomButton.Style.SMALL, skin);

        leftButton.addClickListener(() -> {
            currentAnimalIndex = (currentAnimalIndex - 1 + animalImages.length) % animalImages.length;
            animalImage.setDrawable(new TextureRegionDrawable(new Texture(animalImages[currentAnimalIndex])));
        });

        rightButton.addClickListener(() -> {
            currentAnimalIndex = (currentAnimalIndex + 1) % animalImages.length;
            animalImage.setDrawable(new TextureRegionDrawable(new Texture(animalImages[currentAnimalIndex])));
        });

        // Set up layout
        table.add(leftButton).size(80, 40).expandX().left();
        table.add(animalImage).center();
        table.add(rightButton).size(80, 40).expandX().right();

        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    public String getSelectedAnimal() {
        return animalImages[currentAnimalIndex];
    }

    public int getCurrentAnimalIndex() {
        return currentAnimalIndex;
    }

    /**
     * Retrieve the left roulette button
     *
     * @return the left button
     */
    public CustomButton getLeftButton() {
        return leftButton;
    }

    /**
     * Retrieve the right roulette button
     *
     * @return the right button
     */
    public CustomButton getRightButton() {
        return rightButton;
    }

}
