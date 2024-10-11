package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.CustomButton;

public abstract class AnimalRouletteDisplay1 {
    protected Image[] animalImages;
    protected CustomButton[] animalButtons;
    private final Stage stage;
    private final Skin skin;
    private Image animalImage;
    private final CustomButton selectButton;
    private final CustomButton backButton;
    private final CustomButton leftButton;
    private final CustomButton rightButton;

    public AnimalRouletteDisplay1(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;

        // Replaced CustomButtons in place of TextButtons
        this.selectButton = new CustomButton("Ready?", skin);
        this.backButton = new CustomButton("Go Back", skin);
        this.leftButton = new CustomButton("<", skin);
        this.rightButton = new CustomButton(">", skin);

        initializeDisplay();
    }

    protected abstract String getBackgroundImagePath();

    private void initializeDisplay() {
        BackgroundImage backgroundImage = new BackgroundImage(getBackgroundImagePath());
        stage.addActor(backgroundImage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        String[] animalImagePaths = getAnimalImagePaths();
        animalImage = new Image(new Texture(animalImagePaths[0]));

        Table animalTable = new Table();
        animalTable.add(leftButton).padRight(20);
        animalTable.add(animalImage).size(300, 300);
        animalTable.add(rightButton).padLeft(20);

        mainTable.add(animalTable).expand().center();
        mainTable.row();

        Table buttonTable = new Table();
        buttonTable.add(selectButton).width(200).height(50).padRight(20);
        buttonTable.add(backButton).width(200).height(50);

        mainTable.add(buttonTable).padBottom(50);

        // Adding click listeners for the buttons using their addClickListener method
        leftButton.addClickListener(() -> updateAnimalImage(animalImagePaths[0]));
        rightButton.addClickListener(() -> updateAnimalImage(animalImagePaths[1]));
    }

    public void updateAnimalImage(String imagePath) {
        animalImage.setDrawable(new Image(new Texture(imagePath)).getDrawable());
    }

    protected void initializeAnimalImagesAndButtons() {
        String[] imagePaths = getAnimalImagePaths();
        animalImages = new Image[imagePaths.length];
        animalButtons = new CustomButton[imagePaths.length];  // Use CustomButton

        for (int i = 0; i < imagePaths.length; i++) {
            animalImages[i] = new Image(new Texture(imagePaths[i]));
            animalButtons[i] = new CustomButton(getAnimalType(i), getSkin());  // Create CustomButton

            // Optionally add click listeners to animalButtons
            final int index = i;
            animalButtons[i].addClickListener(() -> updateAnimalImage(imagePaths[index]));
        }
    }

    public String[] getAnimalImagePaths() {
        return new String[] {
                "images/dog.png",
                "images/croc.png",
                "images/bird.png"
        };
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

    public Image getAnimalImage() {
        return animalImage;
    }

    public CustomButton getSelectButton() {
        return selectButton;
    }

    public CustomButton getBackButton() {
        return backButton;
    }

    public CustomButton getLeftButton() {
        return leftButton;
    }

    public CustomButton getRightButton() {
        return rightButton;
    }

    public Skin getSkin() {
        return skin;
    }

    public Stage getStage() {
        return stage;
    }

    public Image[] getAnimalImages() {
        return animalImages;
    }

    public CustomButton[] getAnimalButtons() {
        return animalButtons;
    }
}
