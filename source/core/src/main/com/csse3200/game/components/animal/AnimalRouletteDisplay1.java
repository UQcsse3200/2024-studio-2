package com.csse3200.game.components.animal;

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

    public AnimalRouletteDisplay1(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;

        this.leftButton = new CustomButton("<", skin);
        this.rightButton = new CustomButton(">", skin);
        this.backButton = new CustomButton("Go Back to Main Menu", skin);

        initializeDisplay();
    }

    private void initializeDisplay() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        String[] animalImagePaths = getAnimalImagePaths();
        animalImage = new Image(new Texture(animalImagePaths[0]));

        Table animalTable = new Table();
        animalTable.add(leftButton).size(50, 50).padRight(20);
        animalTable.add(animalImage).size(700, 700);
        animalTable.add(rightButton).size(50, 50).padLeft(20);

        mainTable.add(animalTable).expand().center();
        mainTable.row();

        mainTable.add(backButton).width(200).height(50).padBottom(50);

        updateBackground(0);
    }

    public void updateAnimalImage(String imagePath) {
        animalImage.setDrawable(new Image(new Texture(imagePath)).getDrawable());
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
