package com.csse3200.game.components.animal;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Texture;

public abstract class AnimalRouletteDisplay1 {
    protected Image[] animalImages;
    protected TextButton[] animalButtons;
    private final Stage stage;
    private final Skin skin;
    private Image animalImage;
    private final TextButton selectButton;
    private final TextButton backButton;
    private final TextButton leftButton;
    private final TextButton rightButton;

    public AnimalRouletteDisplay1(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;

        this.selectButton = new TextButton("Ready?", skin);
        this.backButton = new TextButton("Go Back", skin);
        this.leftButton = new TextButton("<", skin);
        this.rightButton = new TextButton(">", skin);

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
    }

    public void updateAnimalImage(String imagePath) {
        animalImage.setDrawable(new Image(new Texture(imagePath)).getDrawable());
    }
    
    protected void initializeAnimalImagesAndButtons() {
        String[] imagePaths = getAnimalImagePaths();
        animalImages = new Image[imagePaths.length];
        animalButtons = new TextButton[imagePaths.length];
        
        for (int i = 0; i < imagePaths.length; i++) {
            animalImages[i] = new Image(new Texture(imagePaths[i]));
            animalButtons[i] = new TextButton(getAnimalType(i), getSkin());
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

    public TextButton getSelectButton() {
        return selectButton;
    }

    public TextButton getBackButton() {
        return backButton;
    }

    public TextButton getLeftButton() {
        return leftButton;
    }

    public TextButton getRightButton() {
        return rightButton;
    }

    public Skin getSkin() {
        return skin;
    }

    public Stage getStage() {
        return stage;
    }
    
    //moving these here, but they are not used anyways so might delete later.
    public Image[] getAnimalImages() {
        return animalImages;
    }
    
    public TextButton[] getAnimalButtons() {
        return animalButtons;
    }
}