package com.csse3200.game.components.animal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.CustomButton;

public class AnimalRouletteDisplay1 {
    protected Image[] animalImages;
    private final Stage stage;
    private final Skin skin;
    private Image animalImage;
    private final CustomButton backButton;
    private final CustomButton leftButton;
    private final CustomButton rightButton;
    private Image backgroundImage;
    private final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final float SCREEN_HEIGHT = Gdx.graphics.getHeight();

    public AnimalRouletteDisplay1(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;

        this.leftButton = new CustomButton("<", skin);
        this.rightButton = new CustomButton(">", skin);
        this.backButton = new CustomButton("Go Back to Main Menu", skin);

        this.animalImages = new Image[getAnimalImagePaths().length];
        for (int i = 0; i < animalImages.length; i++) {
            animalImages[i] = new Image(new Texture(getAnimalImagePaths()[i]));
        }

        initializeDisplay();
    }

    private void initializeDisplay() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        String[] animalImagePaths = getAnimalImagePaths();
        animalImage = new Image(new Texture(animalImagePaths[0]));

        Table animalTable = new Table();
        animalTable.add(leftButton).size(SCREEN_WIDTH * 0.05f, SCREEN_HEIGHT * 0.05f).padRight(SCREEN_WIDTH * 0.02f);
        animalTable.add(animalImage).size(700, 700); // Keep original size
        animalTable.add(rightButton).size(SCREEN_WIDTH * 0.05f, SCREEN_HEIGHT * 0.05f).padLeft(SCREEN_WIDTH * 0.02f);

        mainTable.add(animalTable).expand().center();
        mainTable.row();

        mainTable.add(backButton).width(SCREEN_WIDTH * 0.2f).height(SCREEN_HEIGHT * 0.05f).padBottom(SCREEN_HEIGHT * 0.05f);

        updateBackground(0);
    }

    public void updateAnimalImage(String imagePath) {
        int index = java.util.Arrays.asList(getAnimalImagePaths()).indexOf(imagePath);
        animalImage.setDrawable(animalImages[index].getDrawable());
    }

    public void highlightAnimal(int index) {
        animalImages[index].setColor(1, 0, 0, 1);
    }

    public void resetAnimalColor(int index) {
        animalImages[index].setColor(1, 1, 1, 1);
    }

    public void updateBackground(int index) {
        if (backgroundImage != null) {
            backgroundImage.remove();
        }
        String backgroundPath = getBackgroundImagePath(index);
        backgroundImage = new Image(new Texture(backgroundPath));
        backgroundImage.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundImage);
        backgroundImage.toBack();
    }

    public String[] getAnimalImagePaths() {
        return new String[] {
                "images/dog.png",
                "images/croc.png",
                "images/bird.png"
        };
    }

    public String getBackgroundImagePath(int index) {
        String[] backgrounds = {
                "images/animal/JungleAnimalSelectionBG.jpeg",
                "images/animal/WaterAnimalSelectionBG.jpeg",
                "images/animal/SkyAnimalSelectionBG.jpeg"
        };
        return backgrounds[index];
    }

    public String getAnimalDescription(int index) {
        String[] descriptions = {
                "The dog is a loyal and brave companion, known for its agility and combat prowess.",
                "The crocodile is a powerful reptile with incredible strength and tough scales.",
                "The bird is a versatile creature with the ability to fly and survey the battlefield from above."
        };
        return descriptions[index];
    }

    public String getAnimalType(int index) {
        String[] types = {"Dog", "Croc", "Bird"};
        return types[index];
    }

    // Getters
    public Image getAnimalImage() { return animalImage; }
    public CustomButton getBackButton() { return backButton; }
    public CustomButton getLeftButton() { return leftButton; }
    public CustomButton getRightButton() { return rightButton; }
    public Skin getSkin() { return skin; }
    public Stage getStage() { return stage; }
}
